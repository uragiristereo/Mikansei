package com.uragiristereo.mikansei.feature.about.domain.entity

data class GitHubContributor(
    val login: String,
    val id: Int,
    val avatarUrl: String,
    val htmlUrl: String,
    val contributions: Int,
)
