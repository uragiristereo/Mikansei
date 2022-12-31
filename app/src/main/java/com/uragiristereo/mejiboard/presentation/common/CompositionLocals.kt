package com.uragiristereo.mejiboard.presentation.common

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalMainNavController = compositionLocalOf<NavHostController> { error("no LocalMainNavController provided") }
val LocalHomeNavController = compositionLocalOf<NavHostController> { error("no LocalHomeNavController provided") }
val LocalPostsNavController = compositionLocalOf<NavHostController> { error("no LocalPostsNavController provided") }