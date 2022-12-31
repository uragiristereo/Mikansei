package com.uragiristereo.mejiboard.presentation.common.extension

fun <T> MutableList<T>.clearAndAddAll(elements: Collection<T>) {
    clear()
    addAll(elements)
}
