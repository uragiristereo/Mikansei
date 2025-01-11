package com.uragiristereo.mikansei.feature.search

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.navigation.toRoute
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.domain.module.database.TagCategoryRepository
import com.uragiristereo.mikansei.core.domain.module.search.BrowseChipType
import com.uragiristereo.mikansei.core.domain.usecase.GenerateChipsFromTagsUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetTagsAutoCompleteUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetTagsUseCase
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.search.state.SearchWordIndex
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(SavedStateHandleSaveableApi::class, FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchViewModel(
    savedStateHandle: SavedStateHandle,
    private val tagCategoryRepository: TagCategoryRepository,
    private val getTagsAutoCompleteUseCase: GetTagsAutoCompleteUseCase,
    private val generateChipsFromTagsUseCase: GenerateChipsFromTagsUseCase,
    private val getTagsUseCase: GetTagsUseCase,
) : ViewModel() {
    private val tags = savedStateHandle.toRoute<MainRoute.Search>().tags

    var query by savedStateHandle.saveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(
                text = tags,
                selection = TextRange(index = tags.length),
            )
        )
    }

    var searchAllowed by mutableStateOf(true)
    var boldWord by mutableStateOf("")

    val searches = mutableStateListOf<Tag>()

    var loading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)

    var searchWord by mutableStateOf(SearchWordIndex())
        private set

    var delimiter by mutableStateOf("")
        private set

    private var job: Job? = null
    private val keywords = arrayOf(' ', '{', '}', '~')

    val parsedQuery by derivedStateOf {
        query.text
            .trim()
            .replace(regex = "\\s+".toRegex(), replacement = " ")
            .replace(oldValue = "{ ", newValue = "{")
            .replace(oldValue = " }", newValue = "}")
            .let {
                val notEmptyAndNoEndSpace = it.isNotEmpty() && it[it.length - 1] != ' '

                when {
                    notEmptyAndNoEndSpace -> "$it "
                    else -> it
                }
            }
            .let {
                when (it) {
                    " " -> ""
                    else -> it
                }
            }
    }

    private var tagCategoriesJob: Job? = null

    val browseChips = snapshotFlow { parsedQuery }
        .distinctUntilChanged()
        .flatMapLatest {
            val tags = generateChipsFromTagsUseCase(it, emptyMap())
            val flattenedTags = flattenTags(tags)

            tagCategoryRepository.getCategories(flattenedTags).map { tagsMap ->
                tags.map { tag ->
                    when (tag) {
                        is BrowseChipType.Single -> {
                            when (tag) {
                                is BrowseChipType.Single.Regular -> {
                                    tag.copy(category = tagsMap[tag.tag])
                                }

                                is BrowseChipType.Single.Qualifier -> tag
                            }
                        }

                        is BrowseChipType.Or -> {
                            tag.copy(
                                tags = tag.tags.map { orTag ->
                                    if (orTag is BrowseChipType.Single.Regular) {
                                        orTag.copy(category = tagsMap[orTag.tag])
                                    } else {
                                        orTag
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    init {
        collectBrowseChips()
    }

    private fun getTags(term: String) {
        job?.cancel()

        job = viewModelScope.launch {
            loading = true

            when (val result = getTagsAutoCompleteUseCase(query = term)) {
                is Result.Success -> {
                    searches.apply {
                        clear()
                        addAll(result.data)
                    }
                    cacheTags(result.data)

                    errorMessage = null
                }

                is Result.Failed -> {
                    searches.clear()
                    errorMessage = result.message
                    Timber.d(errorMessage)
                }

                is Result.Error -> {
                    if (result.t !is CancellationException) {
                        searches.clear()
                        errorMessage = result.t.toString()
                        Timber.d(errorMessage)
                    }
                }
            }

            loading = false
        }
    }

    fun searchTerm() {
        val text = query.text
        val cursorIndex = query.selection.end

        if (cursorIndex > 0 && searchAllowed) {
            val match = keywords.filter { keyword ->
                keyword in text[cursorIndex - 1].toString()
            }

            if (match.isEmpty()) {
                searchWord = getWordInPosition(
                    text = text,
                    position = cursorIndex,
                )

                var word = searchWord.word

                if (searchWord.word.isNotEmpty() && word != "-") {
                    when {
                        word.take(n = 1) == "-" -> {
                            delimiter = "-"
                            word = word.substring(startIndex = 1)
                        }

                        else -> delimiter = ""
                    }

                    getTags(word)
                }
            } else clearSearches()
        } else clearSearches()
    }

    private fun clearSearches() {
        job?.cancel()
        searches.clear()
        errorMessage = null
        loading = false
        searchWord = SearchWordIndex()
    }

    private fun getWordInPosition(text: String, position: Int): SearchWordIndex {
        val delimiters = arrayOf(' ', '{', '}')

        val prevIndexes = IntArray(delimiters.size)
        val nextIndexes = IntArray(delimiters.size)

        delimiters.forEachIndexed { index, delimiter ->
            prevIndexes[index] = text.lastIndexOf(char = delimiter, startIndex = position - 1)
        }

        delimiters.forEachIndexed { index, delimiter ->
            nextIndexes[index] = when {
                text.indexOf(char = delimiter, startIndex = position) != -1 -> {
                    text.indexOf(delimiter, position)
                }

                else -> text.length
            }
        }

        val prevDelimiterIndex = prevIndexes.maxOrNull()!!

        val nextDelimiterIndex = when {
            nextIndexes.minOrNull()!! != text.length -> nextIndexes.minOrNull()!!
            else -> -1
        }

        val endIndex = when {
            nextDelimiterIndex < 0 -> text.length
            else -> nextDelimiterIndex
        }

        val startIndex = when {
            prevDelimiterIndex < 0 -> 0
            else -> prevDelimiterIndex + 1
        }.let {
            when {
                it >= endIndex -> 0
                else -> it
            }
        }

        val word = text.substring(startIndex = startIndex, endIndex = endIndex)

        return SearchWordIndex(word, startIndex, endIndex)
    }

    private fun collectBrowseChips() {
        viewModelScope.launch {
            browseChips
                .filterNotNull()
                .filter { it.isNotEmpty() }
                .debounce(timeoutMillis = 500L)
                .collect(::getTagsFromBrowseChips)
        }
    }

    private fun getTagsFromBrowseChips(tags: List<BrowseChipType>) {
        tagCategoriesJob?.cancel()

        tagCategoriesJob = viewModelScope.launch {
            val flattenedTags = flattenTags(tags)
            val result = getTagsUseCase(flattenedTags)

            if (result is Result.Success) {
                cacheTags(result.data)
            }
        }
    }

    private suspend fun cacheTags(tags: List<Tag>) {
        val tagsMap = tags.associate { it.name to it.category }
        tagCategoryRepository.update(tagsMap)
    }

    private fun flattenTags(tags: List<BrowseChipType>): List<String> {
        return tags
            .filter { it !is BrowseChipType.Single.Qualifier }
            .flatMap { browseChipType ->
                when (browseChipType) {
                    is BrowseChipType.Single -> {
                        when (browseChipType) {
                            is BrowseChipType.Single.Regular -> listOf(browseChipType.tag)
                            is BrowseChipType.Single.Qualifier -> emptyList()
                        }
                    }

                    is BrowseChipType.Or -> {
                        browseChipType.tags
                            .filterIsInstance<BrowseChipType.Single.Regular>()
                            .map { it.tag }
                    }
                }
            }
    }
}
