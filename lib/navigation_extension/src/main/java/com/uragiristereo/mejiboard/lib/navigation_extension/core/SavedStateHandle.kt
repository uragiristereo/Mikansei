package com.uragiristereo.mejiboard.lib.navigation_extension.core

import androidx.lifecycle.SavedStateHandle

inline fun <reified T> SavedStateHandle.getData(): T? {
    return get<String>("data")?.let { Serializer.decode(it) }
}

inline fun <reified T> SavedStateHandle.getData(defaultValue: T): T {
    return get<String>("data")?.let { Serializer.decode(it) } ?: defaultValue
}
