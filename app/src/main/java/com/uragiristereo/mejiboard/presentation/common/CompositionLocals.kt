package com.uragiristereo.mejiboard.presentation.common

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import com.uragiristereo.mejiboard.domain.entity.source.post.Post

val LocalMainNavController = compositionLocalOf<NavHostController> { error("no LocalMainNavController provided") }
val LocalHomeNavController = compositionLocalOf<NavHostController> { error("no LocalHomeNavController provided") }
val LocalPostsNavController = compositionLocalOf<NavHostController> { error("no LocalPostsNavController provided") }
val LocalLambdaOnDownload = compositionLocalOf<(Post) -> Unit> { error("no LocalLambdaOnDownload provided") }
