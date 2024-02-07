package com.uragiristereo.mikansei.core.updater.config

import kotlinx.serialization.SerialName

data class UpdaterConfig(
    @SerialName("last_version_installed")
    val lastVersionInstalled: Int = 0,

    @SerialName("update_enforced")
    val updateEnforced: Boolean = false,

    @SerialName("remind_later_counter")
    val remindLaterCounter: Int = 0,

    @SerialName("hide_notification")
    val hideNotification: Boolean = false,
)
