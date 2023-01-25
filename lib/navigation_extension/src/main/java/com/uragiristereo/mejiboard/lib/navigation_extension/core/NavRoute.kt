package com.uragiristereo.mejiboard.lib.navigation_extension.core

import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlin.reflect.KClass

@NoArg
interface NavRoute {
    val route: String
}

val <T : NavRoute> KClass<T>.route: String
    get() = this.java.getConstructor().newInstance().route


internal fun NavRoute.parseData(): String {
    val encoded = Serializer.encode(value = this)

    return route.replaceFirst(oldValue = "{data}", newValue = encoded)
}

val namedNavArg = navArgument(name = "data") {
    type = NavType.StringType
    nullable = true
    defaultValue = null
}
