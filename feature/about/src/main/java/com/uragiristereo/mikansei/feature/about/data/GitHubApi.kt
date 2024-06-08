package com.uragiristereo.mikansei.feature.about.data

import com.uragiristereo.mikansei.feature.about.data.model.contributor.GitHubContributorResponse
import retrofit2.Response
import retrofit2.http.GET

interface GitHubApi {
    @GET("/repos/uragiristereo/Mikansei/contributors")
    suspend fun getContributors(): Response<List<GitHubContributorResponse>>
}
