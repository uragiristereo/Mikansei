package com.uragiristereo.mikansei.feature.user.deactivation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.uragiristereo.mikansei.core.ui.animation.translateXFadeIn
import com.uragiristereo.mikansei.core.ui.animation.translateXFadeOut
import com.uragiristereo.mikansei.feature.user.deactivation.UserDeactivationViewModel
import com.uragiristereo.mikansei.feature.user.deactivation.agreement.UserDeactivationAgreementScreen
import com.uragiristereo.mikansei.feature.user.deactivation.in_app.UserDeactivationInAppConfirmationDialog
import com.uragiristereo.mikansei.feature.user.deactivation.in_app.UserDeactivationInAppScreen
import com.uragiristereo.mikansei.feature.user.deactivation.in_web.UserDeactivationInWebScreen
import com.uragiristereo.mikansei.feature.user.deactivation.methods.UserDeactivationMethodsScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserDeactivationNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val parentViewModelStoreOwner = requireNotNull(LocalViewModelStoreOwner.current)

    NavHost(
        navController = navController,
        startDestination = UserDeactivationRoute.Agreement,
        enterTransition = {
            translateXFadeIn(forward = true)
        },
        exitTransition = {
            translateXFadeOut(forward = true)
        },
        popEnterTransition = {
            translateXFadeIn(forward = false)
        },
        popExitTransition = {
            translateXFadeOut(forward = false)
        },
        modifier = modifier,
    ) {
        agreementRoute(navController, innerPadding, parentViewModelStoreOwner)
        methodsRoute(navController, innerPadding)
        inAppRoute(innerPadding, parentViewModelStoreOwner)
        inWebRoute(innerPadding, parentViewModelStoreOwner)
    }
}

private fun NavGraphBuilder.agreementRoute(
    navController: NavHostController,
    innerPadding: PaddingValues,
    parentViewModelStoreOwner: ViewModelStoreOwner,
) {
    composable<UserDeactivationRoute.Agreement> {
        val viewModel = koinViewModel<UserDeactivationViewModel>(
            viewModelStoreOwner = parentViewModelStoreOwner,
        )

        val activeUser by viewModel.activeUser.collectAsState()

        UserDeactivationAgreementScreen(
            innerPadding = innerPadding,
            activeUserId = activeUser.id,
            isConfirming = viewModel.isConfirming,
            onConfirmingChange = {
                viewModel.isConfirming = it
            },
            onProceedClick = {
                navController.navigate(UserDeactivationRoute.Methods)
            },
        )
    }
}

private fun NavGraphBuilder.methodsRoute(
    navController: NavHostController,
    innerPadding: PaddingValues,
) {
    composable<UserDeactivationRoute.Methods> {
        UserDeactivationMethodsScreen(
            innerPadding = innerPadding,
            onInAppClick = {
                navController.navigate(UserDeactivationRoute.InApp)
            },
            onInWebClick = {
                navController.navigate(UserDeactivationRoute.InWeb)
            },
        )
    }
}

private fun NavGraphBuilder.inAppRoute(
    innerPadding: PaddingValues,
    parentViewModelStoreOwner: ViewModelStoreOwner,
) {
    composable<UserDeactivationRoute.InApp> {
        val viewModel = koinViewModel<UserDeactivationViewModel>(
            viewModelStoreOwner = parentViewModelStoreOwner,
        )

        var passwordTextField by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue())
        }

        UserDeactivationInAppConfirmationDialog(
            isVisible = viewModel.showInAppConfirmationDialog,
            onDismissRequest = {
                viewModel.showInAppConfirmationDialog = false
            },
            onConfirm = {
                viewModel.deactivateAccount(passwordTextField.text)
            },
        )

        UserDeactivationInAppScreen(
            innerPadding = innerPadding,
            isLoading = viewModel.isLoading,
            password = passwordTextField,
            onPasswordChange = {
                passwordTextField = it
            },
            onDeactivateClick = {
                viewModel.showInAppConfirmationDialog = true
            },
        )
    }
}

private fun NavGraphBuilder.inWebRoute(
    innerPadding: PaddingValues,
    parentViewModelStoreOwner: ViewModelStoreOwner,
) {
    composable<UserDeactivationRoute.InWeb> {
        val context = LocalContext.current

        val viewModel = koinViewModel<UserDeactivationViewModel>(
            viewModelStoreOwner = parentViewModelStoreOwner,
        )

        UserDeactivationInWebScreen(
            innerPadding = innerPadding,
            onOpenWebClick = {
                viewModel.openDeactivationWeb(context)
            },
            onLogoutClick = viewModel::switchToAnonymousAndDelete,
        )
    }
}
