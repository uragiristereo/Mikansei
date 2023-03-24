package com.uragiristereo.mikansei.core.ui.entity

import androidx.compose.runtime.Immutable
import kotlin.reflect.KProperty

@Immutable
data class ImmutableList<T>(val value: List<T>) {
    operator fun <T> ImmutableList<T>.getValue(thisObj: Any?, property: KProperty<*>): List<T> = this.value
}

fun <T> immutableListOf(value: List<T> = listOf()): ImmutableList<T> = ImmutableList(value)
