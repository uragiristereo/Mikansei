package com.uragiristereo.mikansei.core.product.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.product.theme.ScrimColor
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductModalBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
    elevation: Dp = 2.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    val scope = rememberCoroutineScope()

    var mainSheetContentHeight by remember { mutableStateOf(0.dp) }

    ModalBottomSheetLayout(
        scrimColor = ScrimColor,
        sheetState = sheetState,
        sheetShape = RectangleShape,
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 0.dp,
        sheetContent = {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsTopHeight(insets = WindowInsets.statusBars)
                    .pointerInput(key1 = Unit) {
                        detectTapGestures {
                            scope.launch {
                                sheetState.hide()
                            }
                        }
                    },
            )

            MainSheetContent(
                elevation = elevation,
                currentMainSheetContentHeight = mainSheetContentHeight,
                onMainSheetContentHeightChange = { mainSheetContentHeight = it },
                onSheetHide = {
                    scope.launch {
                        sheetState.hide()
                    }
                },
                content = content,
            )
        },
        modifier = modifier.displayCutoutPadding(),
        content = { },
    )
}

@Composable
private fun MainSheetContent(
    elevation: Dp,
    currentMainSheetContentHeight: Dp,
    onMainSheetContentHeightChange: (Dp) -> Unit,
    onSheetHide: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val density = LocalDensity.current

    Row(
        horizontalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(currentMainSheetContentHeight)
                .pointerInput(key1 = Unit) {
                    detectTapGestures {
                        onSheetHide()
                    }
                },
        )

        Surface(
            shape = RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp,
            ),
            color = MaterialTheme.colors.background.backgroundElevation(elevation),
            modifier = modifier
                .onSizeChanged { size ->
                    val newHeight = with(density) { size.height.toDp() }

                    if (newHeight != currentMainSheetContentHeight) {
                        onMainSheetContentHeightChange(newHeight)
                    }
                },
        ) {
            Column(
                modifier = Modifier.widthIn(max = 540.dp),
                content = content,
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(currentMainSheetContentHeight)
                .pointerInput(key1 = Unit) {
                    detectTapGestures {
                        onSheetHide()
                    }
                },
        )
    }
}
