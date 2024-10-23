package com.uragiristereo.mikansei.feature.user.deactivation.core

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.product.component.ProductStatusBarSpacer
import com.uragiristereo.mikansei.core.product.component.ProductTopAppBar
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.feature.user.deactivation.navigation.UserDeactivationRoute

@Composable
fun UserDeactivationTopAppBar(
    activeUserName: String,
    currentPage: UserDeactivationRoute,
    isLoading: Boolean,
    isNavigationButtonEnabled: Boolean,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val progress by animateFloatAsState(
        targetValue = when (currentPage) {
            UserDeactivationRoute.Agreement -> 0.33f
            UserDeactivationRoute.Methods -> 0.66f
            UserDeactivationRoute.InApp, UserDeactivationRoute.InWeb -> 1f
        },
        label = "ProgressSteps",
    )

    val currentStep = when (currentPage) {
        UserDeactivationRoute.Agreement -> 1
        UserDeactivationRoute.Methods -> 2
        UserDeactivationRoute.InApp, UserDeactivationRoute.InWeb -> 3
    }

    val totalSteps = UserDeactivationRoute.TOTAL_STEPS

    ProductStatusBarSpacer {
        ProductTopAppBar(
            title = {
                Column {
                    Text(text = "Deactivate account")

                    Text(
                        text = "$activeUserName (Step $currentStep/$totalSteps)",
                        fontSize = 14.sp,
                        color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
                    )
                }
            },
            navigationIcon = {
                IconButton(
                    enabled = isNavigationButtonEnabled,
                    onClick = onNavigateBack,
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = null,
                        )
                    },
                )
            },
            modifier = modifier,
        )

        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background),
            )
        } else {
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background),
            )
        }
    }
}
