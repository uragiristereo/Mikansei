package com.uragiristereo.mikansei.core.updater.github

import retrofit2.Response
import retrofit2.http.GET

internal interface GithubUpdaterApi {
    @GET("/releases")
    suspend fun getReleases(): Response<List<GithubRelease>>
}
