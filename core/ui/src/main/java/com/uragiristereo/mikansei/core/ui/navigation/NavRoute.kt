package com.uragiristereo.mikansei.core.ui.navigation

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

interface NavRoute

inline fun <reified T : NavRoute> routeOf(): Int {
    return routeOf(T::class)
}

@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
fun routeOf(route: KClass<out NavRoute>): Int {
    val serializer = route.serializer()
    var hash = serializer.descriptor.serialName.hashCode()
    for (i in 0 until serializer.descriptor.elementsCount) {
        hash = 31 * hash + serializer.descriptor.getElementName(i).hashCode()
    }
    return hash
}
