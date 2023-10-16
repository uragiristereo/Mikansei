package com.uragiristereo.mikansei.core.product.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.product.theme.ScrimColor
import com.uragiristereo.mikansei.core.ui.composable.DragHandle
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation
import com.uragiristereo.mikansei.core.ui.extension.plus
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.ModalBottomSheetLayout2
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.ModalBottomSheetState2
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.rememberModalBottomSheetState2
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductModalBottomSheetLayout(
    sheetContent: @Composable (navigationBarsPadding: PaddingValues) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState2 = rememberModalBottomSheetState2(initialValue = ModalBottomSheetValue.Hidden),
    showDragHandle: Boolean = true,
    content: @Composable () -> Unit,
) {
    ModalBottomSheetLayout2(
        sheetState = sheetState,
        sheetShape = RectangleShape,
        sheetElevation = 0.dp,
        scrimColor = ScrimColor,
        sheetBackgroundColor = Color.Transparent,
        modifier = modifier,
        sheetContent = {
            ProductModalBottomSheetContent(
                sheetState = sheetState,
                showDragHandle = showDragHandle,
                sheetContent = sheetContent,
            )
        },
        content = content,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ProductModalBottomSheetContent(
    sheetState: ModalBottomSheetState2,
    showDragHandle: Boolean,
    sheetContent: @Composable (navigationBarsPadding: PaddingValues) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    BackHandler(
        enabled = sheetState.isVisible,
        onBack = {
            scope.launch {
                sheetState.hide()
            }
        },
    )

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal)),
    ) {
        Surface(
            color = MaterialTheme.colors.background.backgroundElevation(),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
            ),
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars.only(WindowInsetsSides.Vertical))
                .widthIn(max = 540.dp)
                .align(Alignment.BottomCenter),
        ) {
            val dragHandlePadding = PaddingValues(
                top = when {
                    showDragHandle -> 16.dp
                    else -> 0.dp
                },
            )

            sheetContent(
                WindowInsets.navigationBars.only(WindowInsetsSides.Vertical)
                    .asPaddingValues() + dragHandlePadding
            )

            if (showDragHandle) {
                DragHandle()
            }
        }
    }
}
