package com.uragiristereo.mikansei.core.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun SidesGradient2(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.background,
    content: @Composable () -> Unit,
) {
    ConstraintLayout(
        modifier = modifier.fillMaxWidth(),
    ) {
        val (startSideRef, endSideRef, contentRef) = createRefs()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(contentRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        ) {
            content()
        }

        Box(
            modifier = Modifier
                .width(16.dp)
                .constrainAs(startSideRef) {
                    width = Dimension.value(16.dp)
                    height = Dimension.fillToConstraints

                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            color,
                            Color.Transparent,
                        ),
                    ),
                )
        )

        Box(
            modifier = Modifier
                .constrainAs(endSideRef) {
                    width = Dimension.value(16.dp)
                    height = Dimension.fillToConstraints

                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            color,
                        )
                    ),
                ),
        )
    }
}
