package com.uragiristereo.mikansei.core.updater

interface UpdaterLogger {
    fun d(tag: String, message: String)

    fun w(tag: String, message: String)

    fun w(t: Throwable)
}
