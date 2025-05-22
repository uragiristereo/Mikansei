package com.uragiristereo.mikansei.core.ui.extension

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.widget.Toast

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

tailrec fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}

fun Context.copyToClipboard(
    text: String,
    message: String,
    length: Int = Toast.LENGTH_SHORT,
) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(text, text)
    clipboard.setPrimaryClip(clip)

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        Toast.makeText(this, message, length).show()
    }
}
