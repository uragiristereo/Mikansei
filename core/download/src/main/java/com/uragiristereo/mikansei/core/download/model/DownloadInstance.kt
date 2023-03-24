package com.uragiristereo.mikansei.core.download.model

import kotlinx.coroutines.Job

data class DownloadInstance(
    val info: DownloadInfo,
    val job: Job,
)
