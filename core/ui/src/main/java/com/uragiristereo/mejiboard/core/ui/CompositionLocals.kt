package com.uragiristereo.mejiboard.core.ui

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import com.uragiristereo.mejiboard.core.model.ShareOption
import com.uragiristereo.mejiboard.core.model.booru.post.Post

val LocalMainNavController = compositionLocalOf<NavHostController> { error("no LocalMainNavController provided") }
val LocalLambdaOnDownload = compositionLocalOf<(Post) -> Unit> { error("no LocalLambdaOnDownload provided") }
val LocalLambdaOnShare = compositionLocalOf<(Post, ShareOption) -> Unit> { error("no LocalLambdaOnShare provided") }
val LocalNavigationRailPadding = compositionLocalOf<Dp> { error("no LocalNavigationRailPadding provided") }
