package com.uragiristereo.mikansei.core.updater.config

interface UpdaterConfigRepository {
    suspend fun readConfig(): UpdaterConfig
    suspend fun writeConfig(block: (UpdaterConfig) -> UpdaterConfig)
}
