package com.uragiristereo.mikansei.core.domain.module.network.entity

import kotlinx.coroutines.Job

data class DownloadInstance(
    val info: DownloadInfo,
    val job: Job,
)
