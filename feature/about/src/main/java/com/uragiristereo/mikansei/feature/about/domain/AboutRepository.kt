package com.uragiristereo.mikansei.feature.about.domain

import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.feature.about.domain.entity.GitHubContributor

interface AboutRepository {
    suspend fun getContributors(): Result<List<GitHubContributor>>
}
