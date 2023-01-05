package com.uragiristereo.mejiboard.core.download.model

import kotlinx.coroutines.Job

data class DownloadInstance(
    val info: DownloadInfo,
    val job: Job,
)
