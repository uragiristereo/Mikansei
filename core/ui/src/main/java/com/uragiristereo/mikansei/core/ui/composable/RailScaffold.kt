package com.uragiristereo.mikansei.core.ui.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.LocalScaffoldPadding

@Composable
fun RailScaffold(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    topBar: @Composable () -> Unit = {},
    startBar: @Composable () -> Unit = {},
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    backgroundColor: Color = MaterialTheme.colors.background,
    contentColor: Color = contentColorFor(backgroundColor),
    contentPadding: PaddingValues = LocalScaffoldPadding.current,
    content: @Composable (PaddingValues) -> Unit
) {
    Surface(
        modifier = modifier,
        color = backgroundColor,
        contentColor = contentColor,
    ) {
        RailScaffoldLayout(
            topBar = topBar,
            startBar = startBar,
            snackbar = {
                snackbarHost(scaffoldState.snackbarHostState)
            },
            contentPadding = contentPadding,
            content = content,
        )
    }
}

@Composable
private fun RailScaffoldLayout(
    topBar: @Composable () -> Unit,
    startBar: @Composable () -> Unit,
    snackbar: @Composable () -> Unit,
    contentPadding: PaddingValues,
    content: @Composable (PaddingValues) -> Unit,
) {
    val innerPadding = remember { MutablePaddingValues() }

    SubcomposeLayout { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight

        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        layout(layoutWidth, layoutHeight) {
            val topBarPlaceables = subcompose(RailScaffoldLayoutContent.TopBar, topBar).map {
                it.measure(looseConstraints)
            }

            val topBarHeight = topBarPlaceables.maxByOrNull { it.height }?.height ?: 0

            val snackbarPlaceables = subcompose(RailScaffoldLayoutContent.Snackbar, snackbar).map {
                it.measure(looseConstraints)
            }

            val snackbarHeight = snackbarPlaceables.maxByOrNull { it.height }?.height ?: 0

            val startBarPlaceables = subcompose(RailScaffoldLayoutContent.StartBar, startBar).map {
                it.measure(looseConstraints)
            }

            val startBarWidth = startBarPlaceables.maxByOrNull { it.width }?.width ?: 0

            // Update the inner padding
            innerPadding.apply {
                start = startBarWidth.toDp() + contentPadding.calculateStartPadding(LayoutDirection.Ltr)
                top = topBarHeight.toDp() + contentPadding.calculateTopPadding()
                end = contentPadding.calculateEndPadding(LayoutDirection.Ltr)
                bottom = contentPadding.calculateBottomPadding()
            }

            val bodyContentPlaceables = subcompose(RailScaffoldLayoutContent.MainContent) {
                CompositionLocalProvider(LocalScaffoldPadding provides innerPadding) {
                    content(innerPadding)
                }
            }.map { it.measure(looseConstraints.copy(maxHeight = layoutHeight)) }

            // Placing to control drawing order to match default elevation of each placeable

            bodyContentPlaceables.forEach { it.place(0, 0) }
            topBarPlaceables.forEach { it.place(0, 0) }
            snackbarPlaceables.forEach {
                it.place(startBarWidth, layoutHeight - snackbarHeight)
            }
            startBarPlaceables.forEach {
                it.place(0, topBarHeight)
            }
        }
    }
}

private enum class RailScaffoldLayoutContent { TopBar, MainContent, Snackbar, StartBar }

@Stable
internal class MutablePaddingValues : PaddingValues {
    var start: Dp by mutableStateOf(0.dp)
    var top: Dp by mutableStateOf(0.dp)
    var end: Dp by mutableStateOf(0.dp)
    var bottom: Dp by mutableStateOf(0.dp)

    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp {
        return when (layoutDirection) {
            LayoutDirection.Ltr -> start
            LayoutDirection.Rtl -> end
        }
    }

    override fun calculateTopPadding(): Dp = top

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp {
        return when (layoutDirection) {
            LayoutDirection.Ltr -> end
            LayoutDirection.Rtl -> start
        }
    }

    override fun calculateBottomPadding(): Dp = bottom
}
