package com.uragiristereo.mikansei.core.domain.usecase

import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.roundToLong

class ConvertFileSizeUseCase {
    operator fun invoke(sizeBytes: Long): String {
        if (sizeBytes == 0L)
            return "0 B"

        val sizeName = listOf("B", "KB", "MB", "GB")

        val i = floor(ln(sizeBytes.toDouble()) / ln(1024.0))
        val p = 1024.0.pow(i)
        val s = (sizeBytes / p * 100).roundToLong() / 100.0

        return "$s ${sizeName[i.toInt()]}"
    }
}
