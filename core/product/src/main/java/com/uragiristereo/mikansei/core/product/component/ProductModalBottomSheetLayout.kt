package com.uragiristereo.mikansei.core.product.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.product.theme.ScrimColor
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun ProductModalBottomSheetLayout(
    sheetState: ModalBottomSheetState,
    sheetContent: @Composable (navigationBarsPadding: PaddingValues) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RectangleShape,
        sheetElevation = 0.dp,
        scrimColor = ScrimColor,
        sheetBackgroundColor = Color.Transparent,
        modifier = modifier,
        sheetContent = {
            Card(
                elevation = 0.dp,
                backgroundColor = MaterialTheme.colors.background.backgroundElevation(),
                shape = RoundedCornerShape(
                    topStart = 12.dp,
                    topEnd = 12.dp,
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(
                        WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal)
                    ),
                content = {
                    sheetContent(
                        WindowInsets.navigationBars.only(WindowInsetsSides.Vertical).asPaddingValues()
                    )
                },
            )
        },
        content = content,
    )
}
