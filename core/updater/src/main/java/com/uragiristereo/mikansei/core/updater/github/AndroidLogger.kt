package com.uragiristereo.mikansei.core.updater.github

import android.util.Log
import com.uragiristereo.mikansei.core.updater.UpdaterLogger

object AndroidLogger : UpdaterLogger {
    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun w(tag: String, message: String) {
        Log.w(tag, message)
    }

    override fun w(t: Throwable) {
        t.printStackTrace()
    }
}
