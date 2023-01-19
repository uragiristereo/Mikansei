package com.uragiristereo.mejiboard.core.product.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.core.common.ui.extension.backgroundElevation
import com.uragiristereo.mejiboard.core.product.theme.ScrimColor
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
