package com.uragiristereo.mikansei.core.ui.extension

import kotlinx.coroutines.channels.Channel

suspend inline fun <T> Channel<T>.forEach(action: (T) -> Unit) {
    for (element in this) action(element)
}
