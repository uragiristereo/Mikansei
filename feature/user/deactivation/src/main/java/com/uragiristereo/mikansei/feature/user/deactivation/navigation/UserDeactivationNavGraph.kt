package com.uragiristereo.mikansei.feature.user.deactivation.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.uragiristereo.mikansei.core.ui.animation.translateXFadeIn
import com.uragiristereo.mikansei.core.ui.animation.translateXFadeOut
import com.uragiristereo.mikansei.feature.user.deactivation.UserDeactivationViewModel
import com.uragiristereo.mikansei.feature.user.deactivation.agreement.UserDeactivationAgreementScreen
import com.uragiristereo.mikansei.feature.user.deactivation.in_app.UserDeactivationInAppScreen
import com.uragiristereo.mikansei.feature.user.deactivation.in_web.UserDeactivationInWebScreen
import com.uragiristereo.mikansei.feature.user.deactivation.methods.UserDeactivationMethodsScreen

@Composable
fun UserDeactivationNavGraph(
    innerPadding: PaddingValues,
    viewModel: UserDeactivationViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val activeUser by viewModel.activeUser.collectAsState()

    AnimatedContent(
        targetState = viewModel.currentPage,
        transitionSpec = {
            val forward = initialState < targetState
            val transition = translateXFadeIn(forward) togetherWith
                    translateXFadeOut(forward)

            transition.apply {
                targetContentZIndex = when {
                    forward -> 0f
                    else -> -1f
                }
            }
        },
        label = "UserDeactivationPage",
        modifier = modifier.fillMaxSize(),
    ) { currentPageState ->
        when (currentPageState) {
            Page.AGREEMENT -> {
                UserDeactivationAgreementScreen(
                    innerPadding = innerPadding,
                    activeUserId = activeUser.id,
                    isConfirming = viewModel.isConfirming,
                    onConfirmingChange = {
                        viewModel.isConfirming = it
                    },
                    onProceedClick = {
                        viewModel.currentPage = Page.METHODS
                    },
                )
            }

            Page.METHODS -> {
                UserDeactivationMethodsScreen(
                    innerPadding = innerPadding,
                    onInAppClick = {
                        viewModel.currentPage = Page.IN_APP
                    },
                    onInWebClick = {
                        viewModel.currentPage = Page.IN_BROWSER
                    },
                )
            }

            Page.IN_APP -> {
                UserDeactivationInAppScreen(
                    innerPadding = innerPadding,
                    isLoading = viewModel.isLoading,
                    password = viewModel.passwordTextField,
                    onPasswordChange = {
                        viewModel.passwordTextField = it
                    },
                    onDeactivateClick = {
                        viewModel.showInAppConfirmationDialog = true
                    },
                )
            }

            Page.IN_BROWSER -> {
                UserDeactivationInWebScreen(
                    innerPadding = innerPadding,
                    onOpenWebClick = {
                        viewModel.openDeactivationWeb(context)
                    },
                    onLogoutClick = viewModel::switchToAnonymousAndDelete,
                )
            }
        }
    }
}
