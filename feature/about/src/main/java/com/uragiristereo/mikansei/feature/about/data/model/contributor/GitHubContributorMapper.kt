package com.uragiristereo.mikansei.feature.about.data.model.contributor

import com.uragiristereo.mikansei.feature.about.domain.entity.GitHubContributor

fun GitHubContributorResponse.toGitHubContributor(): GitHubContributor {
    return GitHubContributor(
        login = login,
        id = id,
        avatarUrl = avatarUrl,
        htmlUrl = htmlUrl,
        contributions = contributions,
    )
}

fun List<GitHubContributorResponse>.toGitHubContributorList(): List<GitHubContributor> {
    return map { it.toGitHubContributor() }.sortedByDescending { it.contributions }
}
