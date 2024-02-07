package com.uragiristereo.mikansei.core.updater

object SystemLogger : UpdaterLogger {
    override fun d(tag: String, message: String) {
        print(message)
    }

    override fun w(tag: String, message: String) {
        print(message)
    }

    override fun w(t: Throwable) {
        t.printStackTrace()
    }
}
