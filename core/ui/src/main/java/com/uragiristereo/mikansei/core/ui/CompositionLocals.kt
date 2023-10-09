package com.uragiristereo.mikansei.core.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.danbooru.ShareOption
import kotlinx.coroutines.channels.Channel

val LocalLambdaOnDownload = compositionLocalOf<(Post) -> Unit> { error("no LocalLambdaOnDownload provided") }
val LocalLambdaOnShare = compositionLocalOf<(Post, ShareOption) -> Unit> { error("no LocalLambdaOnShare provided") }
val LocalScrollToTopChannel = compositionLocalOf<Channel<String>> { error("no LocalScrollToTopChannel provided") }
val LocalMainScaffoldPadding = compositionLocalOf { PaddingValues(0.dp) }
