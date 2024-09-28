package com.uragiristereo.mikansei.feature.about.data

import com.uragiristereo.mikansei.core.domain.module.network.NetworkRepository
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import com.uragiristereo.mikansei.core.model.result.resultOf
import com.uragiristereo.mikansei.feature.about.data.model.contributor.toGitHubContributorList
import com.uragiristereo.mikansei.feature.about.domain.AboutRepository
import com.uragiristereo.mikansei.feature.about.domain.entity.GitHubContributor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class AboutRepositoryImpl(
    networkRepository: NetworkRepository,
) : AboutRepository {
    private val githubClient: GitHubApi = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .client(networkRepository.okHttpClient)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(GitHubApi::class.java)

    override suspend fun getContributors(): Result<List<GitHubContributor>> {
        return resultOf {
            githubClient.getContributors()
        }.mapSuccess {
            it.toGitHubContributorList()
        }
    }
}
