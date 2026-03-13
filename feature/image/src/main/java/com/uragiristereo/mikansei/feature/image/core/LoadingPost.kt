package com.uragiristereo.mikansei.feature.image.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.ui.LocalScaffoldState

@Composable
@Suppress("DEPRECATION")
fun LoadingPost(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        scaffoldState = LocalScaffoldState.current,
        topBar = {
            ViewerTopAppBar(
                postId = null,
                onNavigateBack = onNavigateBack,
            )
        },
        contentPadding = PaddingValues(0.dp),
        backgroundColor = Color.Black,
        contentColor = Color.White,
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            CircularProgressIndicator()
        }
    }
}
