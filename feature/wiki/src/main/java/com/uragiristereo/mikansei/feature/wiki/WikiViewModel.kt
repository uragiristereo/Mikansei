package com.uragiristereo.mikansei.feature.wiki

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.WikiData
import com.uragiristereo.mikansei.core.domain.usecase.GetPostUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetWikiUseCase
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.ui.navigation.WikiRoute
import com.uragiristereo.serializednavigationextension.runtime.navArgsOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class WikiViewModel(
    savedStateHandle: SavedStateHandle,
    private val danbooruRepository: DanbooruRepository,
    private val getWikiUseCase: GetWikiUseCase,
    private val getPostUseCase: GetPostUseCase,
) : ViewModel() {
    private val navArgs = savedStateHandle.navArgsOf(WikiRoute.Index())
    val wikiName = navArgs.tag

    private val channel = Channel<Event>()
    val event = channel.receiveAsFlow()

    val baseUrl: String get() = danbooruRepository.host.getBaseUrl()

    var wikiLoading by mutableStateOf(true); private set
    var postLoading by mutableStateOf(false); private set
    var wikiData by mutableStateOf<WikiData?>(null); private set

    private var postJob: Job? = null

    init {
        refresh()
    }

    fun refresh() {
        getWiki()
    }

    private fun getWiki() {
        viewModelScope.launch {
            wikiLoading = true

            when (val result = getWikiUseCase.invoke(tag = navArgs.tag)) {
                is Result.Success -> wikiData = result.data
                is Result.Failed -> channel.send(Event.OnError(result.message))
                is Result.Error -> {
                    if (result.t !is CancellationException) {
                        channel.send(Event.OnError(result.t.toString()))
                    }
                }
            }

            wikiLoading = false
        }
    }

    fun getPost(postId: Int) {
        postJob = viewModelScope.launch {
            postLoading = true

            when (val result = getPostUseCase.invoke(postId)) {
                is Result.Success -> channel.send(Event.OnPostSuccess(result.data))
                is Result.Failed -> channel.send(Event.OnError(result.message))
                is Result.Error -> {
                    if (result.t !is CancellationException) {
                        channel.send(Event.OnError(result.t.toString()))
                    }
                }
            }

            postLoading = false
        }
    }

    fun cancelGetPost() {
        postJob?.cancel()
        postLoading = false
    }

    sealed interface Event {
        data class OnPostSuccess(val post: Post) : Event
        data class OnError(val message: String) : Event
    }
}
