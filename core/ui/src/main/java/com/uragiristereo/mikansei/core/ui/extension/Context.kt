package com.uragiristereo.mikansei.core.ui.extension

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build

val Context.versionName: String
    get() = packageManager
        .getPackageInfo(packageName, 0)
        .versionName

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

tailrec fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}
