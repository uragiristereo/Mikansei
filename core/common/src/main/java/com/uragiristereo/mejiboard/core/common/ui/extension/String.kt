package com.uragiristereo.mejiboard.core.common.ui.extension

fun String.strip(
    splitter: String = "",
): String {
    return this
        .replace(regex = "\\s+".toRegex(), replacement = " ")
        .replace(oldValue = "\r", newValue = splitter)
        .replace(oldValue = "\n", newValue = splitter)
        .trim()
}
