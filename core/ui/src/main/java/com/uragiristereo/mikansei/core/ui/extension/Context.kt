package com.uragiristereo.mikansei.core.ui.extension

import android.content.Context

@Suppress("DEPRECATION")
val Context.versionName: String
    get() = packageManager
        .getPackageInfo(this.packageName, 0)
        .versionName
