package com.uragiristereo.mikansei.feature.about

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.model.Environment
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.feature.about.domain.AboutRepository
import com.uragiristereo.mikansei.feature.about.domain.entity.GitHubContributor
import kotlinx.coroutines.launch
import org.koin.androidx.scope.ScopeViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
class AboutViewModel(
    danbooruRepository: DanbooruRepository,
    val environment: Environment,
) : ScopeViewModel() {
    private val repository: AboutRepository by scope.inject()
    val baseUrl = danbooruRepository.host.getBaseUrl()

    var isLoading by mutableStateOf(false); private set
    var isFailedToLoadContributors by mutableStateOf(false); private set
    var contributors by mutableStateOf(emptyList<GitHubContributor>()); private set

    private val me = GitHubContributor(
        login = "uragiristereo",
        id = 52477630,
        avatarUrl = "https://avatars.githubusercontent.com/u/52477630?v=4",
        htmlUrl = "https://github.com/uragiristereo",
        contributions = 0,
    )

    init {
        getContributors()
    }

    private fun getContributors() {
        viewModelScope.launch {
            isLoading = true
            val result = repository.getContributors()

            contributors = when (result) {
                is Result.Success -> result.data
                is Result.Failed, is Result.Error -> {
                    isFailedToLoadContributors = true
                    listOf(me)
                }
            }

            isLoading = false
        }
    }
}
