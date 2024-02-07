package com.uragiristereo.mikansei.core.updater

import com.uragiristereo.mikansei.core.updater.config.UpdaterConfig
import com.uragiristereo.mikansei.core.updater.config.UpdaterConfigRepository

class MockUpdaterConfigRepository(initial: UpdaterConfig) : UpdaterConfigRepository {
    private var config = initial

    override suspend fun readConfig(): UpdaterConfig = config

    override suspend fun writeConfig(block: (UpdaterConfig) -> UpdaterConfig) {
        config = block(config)
    }
}
