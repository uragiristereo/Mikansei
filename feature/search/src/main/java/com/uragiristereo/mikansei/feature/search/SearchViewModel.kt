package com.uragiristereo.mikansei.feature.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.uragiristereo.safer.compose.navigation.core.getData
import com.uragiristereo.mikansei.core.domain.entity.tag.TagAutoComplete
import com.uragiristereo.mikansei.core.domain.usecase.GetTagsAutoCompleteUseCase
import com.uragiristereo.mikansei.core.model.booru.BooruSource
import com.uragiristereo.mikansei.core.model.booru.getBooruByKey
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.preferences.PreferencesRepository
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.search.state.SearchWordIndex
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchViewModel(
    savedStateHandle: SavedStateHandle,
    preferencesRepository: PreferencesRepository,
    private val getTagsAutoCompleteUseCase: GetTagsAutoCompleteUseCase,
) : ViewModel() {
    val tags = savedStateHandle.getData(MainRoute.Search()).tags

    var preferences by mutableStateOf(preferencesRepository.data)
        private set

    var selectedBooru by mutableStateOf(BooruSource.values().getBooruByKey(preferences.booru) ?: BooruSource.Gelbooru)
        private set

    var actionsRowExpanded by mutableStateOf(true)
    var searchAllowed by mutableStateOf(true)
    var boldWord by mutableStateOf("")

    var parsedQuery by mutableStateOf("")

    val searches = mutableStateListOf<TagAutoComplete>()

    var loading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)

    var searchWord by mutableStateOf(SearchWordIndex())
        private set

    var delimiter by mutableStateOf("")
        private set

    private var job: Job? = null
    private val keywords = arrayOf(' ', '{', '}', '~')

    init {
        preferencesRepository.flowData
            .onEach {
                preferences = it
                selectedBooru = BooruSource.values().getBooruByKey(it.booru) ?: BooruSource.Gelbooru
            }
            .launchIn(viewModelScope)
    }

    fun parseQuery(query: String) {
        parsedQuery = query
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

    private fun getTags(term: String) {
        job?.cancel()

        job = viewModelScope.launch {
            getTagsAutoCompleteUseCase(query = term)
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            searches.apply {
                                clear()
                                addAll(result.data)
                            }

                            errorMessage = null
                        }
                        is Result.Failed -> {
                            searches.clear()
                            errorMessage = result.message
                            Timber.d(errorMessage)
                        }

                        is Result.Error -> {
                            searches.clear()
                            errorMessage = result.t.toString()
                            Timber.d(errorMessage)
                        }
                    }
                }
        }
    }

    fun searchTerm(text: String, cursorIndex: Int) {
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

    fun cancelSearch() {
        job?.cancel()
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
                text.indexOf(char = delimiter, startIndex = position) != -1 -> text.indexOf(delimiter, position)
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

    fun onActionsRowExpandedChange(value: Boolean) {
        actionsRowExpanded = value
    }
}
