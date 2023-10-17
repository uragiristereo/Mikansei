package com.uragiristereo.mikansei.core.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.danbooru.ShareOption
import kotlinx.coroutines.channels.Channel

val LocalLambdaOnDownload = compositionLocalOf<(Post) -> Unit> { error("no LocalLambdaOnDownload provided") }
val LocalLambdaOnShare = compositionLocalOf<(Post, ShareOption) -> Unit> { error("no LocalLambdaOnShare provided") }
val LocalScrollToTopChannel = compositionLocalOf<Channel<String>> { error("no LocalScrollToTopChannel provided") }
val LocalMainScaffoldPadding = compositionLocalOf { PaddingValues(0.dp) }
val LocalScaffoldState = compositionLocalOf<ScaffoldState> { error("No LocalScaffoldState provided") }
val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> { error("No LocalSnackbarHostState provided") }
