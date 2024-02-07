package com.uragiristereo.mikansei.core.updater

import com.uragiristereo.mikansei.core.updater.config.UpdaterConfig
import com.uragiristereo.mikansei.core.updater.config.UpdaterConfigRepository
import com.uragiristereo.mikansei.core.updater.model.Release

class MockMikanseiUpdater(
    currentVersionCode: Int,
    private val releases: List<Release>,
    private val configRepository: UpdaterConfigRepository,
    override val TAG: String = "MockMikanseiUpdater",
) : MikanseiUpdater(
    currentVersionCode = currentVersionCode,
    logger = SystemLogger,
) {
    override suspend fun getReleases(): List<Release> = releases

    override suspend fun readConfig() = configRepository.readConfig()

    override suspend fun writeConfig(block: (UpdaterConfig) -> UpdaterConfig) = configRepository.writeConfig(block)
}
