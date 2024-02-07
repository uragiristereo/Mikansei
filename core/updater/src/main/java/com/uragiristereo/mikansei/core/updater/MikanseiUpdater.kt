package com.uragiristereo.mikansei.core.updater

import com.uragiristereo.mikansei.core.updater.config.UpdaterConfigRepository
import com.uragiristereo.mikansei.core.updater.model.Release
import com.uragiristereo.mikansei.core.updater.model.UpdaterResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class MikanseiUpdater(
    currentVersionCode: Int,
    logger: UpdaterLogger,
) : UpdaterConfigRepository {
    abstract val TAG: String
    abstract suspend fun getReleases(): List<Release>

    open val remindLaterThreshold = 10
    open val showAllReleasesOnChangelogs = false

    val result: Flow<UpdaterResult> = flow {
        try {
            logger.d(TAG, "==============================================")
            logger.d(TAG, "Checking if an update is enforced...")
            val updaterConfig = readConfig()

            if (updaterConfig.updateEnforced) {
                logger.d(TAG, "The update is enforced.")
                emit(UpdaterResult.UpdateEnforced)
            } else {
                logger.d(TAG, "The update is NOT enforced.")
            }

            logger.d(TAG, "Checking for app updates...")
            val releases = getReleases()
            val newReleases = releases
                .filter { it.version > currentVersionCode }
                .sortedByDescending { it.version }

            if (newReleases.isNotEmpty()) {
                val latestRelease = newReleases.maxBy { it.version }
                val forcedRelease = newReleases
                    .sortedBy { it.version }
                    .firstOrNull { it.forceUpdate }

                logger.d(TAG, "New update is available! the latest version is v${latestRelease.name} (${latestRelease.version})")

                if (forcedRelease != null) {
                    logger.d(TAG, "The update is enforced since v${forcedRelease.name} (${forcedRelease.version})")
                }

                writeConfig {
                    it.copy(
                        lastVersionInstalled = currentVersionCode,
                        updateEnforced = forcedRelease != null,
                    )
                }

                val shouldShowNotification = updaterConfig.remindLaterCounter >= remindLaterThreshold || !updaterConfig.hideNotification

                emit(UpdaterResult.UpdatesAvailable(latestRelease, newReleases, shouldShowNotification))
            } else {
                if (updaterConfig.lastVersionInstalled < currentVersionCode) {
                    logger.d(TAG, "App is successfully updated! will showing changelogs dialog to user.")

                    val releasesSincePreviousVersionInstalled = when {
                        showAllReleasesOnChangelogs -> releases
                        else -> releases.filter { it.version > updaterConfig.lastVersionInstalled }
                    }

                    emit(UpdaterResult.ShowChangelog(releasesSincePreviousVersionInstalled))
                } else {
                    logger.d(TAG, "No updates have found, you're on the latest version!")
                    emit(UpdaterResult.NoUpdates)
                }
            }

        } catch (t: Throwable) {
            logger.w(t)
        }
    }

    suspend fun incrementRemindLaterCounter() {
        writeConfig {
            it.copy(remindLaterCounter = it.remindLaterCounter + 1)
        }
    }

    suspend fun hideNotification() {
        writeConfig {
            it.copy(hideNotification = true)
        }
    }
}


