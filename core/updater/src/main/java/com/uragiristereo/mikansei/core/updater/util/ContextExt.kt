package com.uragiristereo.mikansei.core.updater.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build

internal fun Context.getPackageInfo(): PackageInfo {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        else -> packageManager.getPackageInfo(packageName, 0)
    }
}

@Suppress("DEPRECATION")
internal fun Context.getVersionCode(): Int {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> getPackageInfo().longVersionCode.toInt()
        else -> getPackageInfo().versionCode
    }
}
