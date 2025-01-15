package com.uragiristereo.mikansei.core.ui.extension

import android.content.Context
import android.os.Build

val Context.versionName: String
    get() = packageManager
        .getPackageInfo(packageName, 0)
        .versionName
        .orEmpty()

@Suppress("DEPRECATION")
val Context.versionCode: Long
    get() = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {
            packageManager
                .getPackageInfo(packageName, 0)
                .longVersionCode
        }

        else -> {
            packageManager
                .getPackageInfo(packageName, 0)
                .versionCode.toLong()
        }
    }
