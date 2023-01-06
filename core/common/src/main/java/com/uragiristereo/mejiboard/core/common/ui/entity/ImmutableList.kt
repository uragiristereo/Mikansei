package com.uragiristereo.mejiboard.core.common.ui.entity

import androidx.compose.runtime.Immutable
import kotlin.reflect.KProperty

@Immutable
data class ImmutableList<T>(
    val value: List<T>,
) {
    operator fun <T> ImmutableList<T>.getValue(
        thisObj: Any?,
        property: KProperty<*>,
    ): List<T> {
        return this.value
    }
}

fun <T> immutableListOf(value: List<T> = listOf()): ImmutableList<T> {
    return ImmutableList(value)
}
