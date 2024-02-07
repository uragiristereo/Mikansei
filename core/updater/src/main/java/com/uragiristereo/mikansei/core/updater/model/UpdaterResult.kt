package com.uragiristereo.mikansei.core.updater.model

sealed interface UpdaterResult {
    data object NoUpdates : UpdaterResult

    data object UpdateEnforced : UpdaterResult

    data class UpdatesAvailable(
        val latestRelease: Release,
        val releases: List<Release>,
        val shouldShowNotification: Boolean,
    ) : UpdaterResult

    data class ShowChangelog(
        val releases: List<Release>,
    ) : UpdaterResult
}
