package com.uragiristereo.mikansei.core.updater.github

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.uragiristereo.mikansei.core.updater.MikanseiUpdater
import com.uragiristereo.mikansei.core.updater.config.UpdaterConfigRepository
import com.uragiristereo.mikansei.core.updater.datastore.DataStoreUpdaterConfigRepository
import com.uragiristereo.mikansei.core.updater.model.Release
import com.uragiristereo.mikansei.core.updater.util.getVersionCode
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@OptIn(ExperimentalSerializationApi::class)
class MikanseiGithubUpdater(
    context: Context,
) : MikanseiUpdater(
    currentVersionCode = context.getVersionCode(),
    logger = AndroidLogger,
), UpdaterConfigRepository by DataStoreUpdaterConfigRepository(context) {
    override val TAG = "GithubUpdater"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val client = Retrofit.Builder()
        .baseUrl("https://api.github.com/repos/uragiristereo/Mikansei")
        .addConverterFactory(json.asConverterFactory(contentType = "application/json".toMediaType()))
        .build()
        .create(GithubUpdaterApi::class.java)

    override suspend fun getReleases(): List<Release> {
        val response = client.getReleases()

        if (response.isSuccessful) {
            return response.body()!!.toReleaseList()
        } else {
            throw Exception("Error while getting releases! ${response.errorBody()}")
        }
    }

    private fun GithubRelease.toRelease(): Release {
        return Release(
            version = getVersionCodeFromName(name) ?: 0,
            name = name,
            url = htmlUrl,
            forceUpdate = getForceUpdateFromName(name),
            description = body,
        )
    }

    private fun List<GithubRelease>.toReleaseList(): List<Release> {
        return filter { !it.draft }.map { it.toRelease() }
    }

    private fun getVersionCodeFromName(name: String): Int? {
        val regex = "\\(([^)]+)\\)".toRegex()
        val match = regex.find(name)

        return match?.groupValues?.getOrNull(1)?.toInt()
    }

    private fun getForceUpdateFromName(name: String): Boolean {
        return name.last() == '*'
    }
}
