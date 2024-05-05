package com.uragiristereo.mikansei.core.model

import android.content.Context
import android.os.StatFs
import okhttp3.Cache
import java.io.File

object CacheUtil {
    private const val DISK_CACHE_PERCENTAGE = 0.02
    private const val MIN_DISK_CACHE_SIZE_BYTES = 10L * 1024 * 1024 // 10MB
    private const val MAX_DISK_CACHE_SIZE_BYTES = 250L * 1024 * 1024 // 250MB

    fun createDefaultCache(context: Context, path: String): Cache {
        val cacheDirectory = getCacheDir(context, path)
        val cacheSize = calculateDiskCacheSize(cacheDirectory)

        return Cache(cacheDirectory, cacheSize)
    }

    private fun getCacheDir(context: Context, path: String): File {
        return File(context.cacheDir, path).apply { mkdirs() }
    }

    private fun calculateDiskCacheSize(cacheDirectory: File): Long {
        return try {
            val cacheDir = StatFs(cacheDirectory.absolutePath)
            val size = DISK_CACHE_PERCENTAGE * cacheDir.blockCountLong * cacheDir.blockSizeLong

            return size.toLong().coerceIn(MIN_DISK_CACHE_SIZE_BYTES, MAX_DISK_CACHE_SIZE_BYTES)
        } catch (_: Exception) {
            MIN_DISK_CACHE_SIZE_BYTES
        }
    }
}
