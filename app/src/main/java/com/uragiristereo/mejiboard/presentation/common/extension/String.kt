package com.uragiristereo.mejiboard.presentation.common.extension

fun String.strip(
    splitter: String = "",
): String {
    return this
        .replace(regex = "\\s+".toRegex(), replacement = " ")
        .replace(oldValue = "\r", newValue = splitter)
        .replace(oldValue = "\n", newValue = splitter)
        .trim()
}