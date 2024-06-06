package com.uragiristereo.mikansei.feature.user.deactivation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.product.component.ProductNavigationBarSpacer
import com.uragiristereo.mikansei.core.ui.LocalScaffoldState
import com.uragiristereo.mikansei.feature.user.deactivation.core.UserDeactivationTopAppBar
import com.uragiristereo.mikansei.feature.user.deactivation.in_app.UserDeactivationInAppConfirmationDialog
import com.uragiristereo.mikansei.feature.user.deactivation.navigation.Page
import com.uragiristereo.mikansei.feature.user.deactivation.navigation.UserDeactivationNavGraph
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserDeactivationScreen(
    onNavigateBack: () -> Unit,
    onNavigateMore: () -> Unit,
    viewModel: UserDeactivationViewModel = koinViewModel(),
) {
    val scaffoldState = LocalScaffoldState.current
    val activeUser by viewModel.activeUser.collectAsState()
    val scope = rememberCoroutineScope()

    val popPage: () -> Unit = {
        if (!viewModel.isLoading) {
            when (viewModel.currentPage) {
                Page.AGREEMENT -> onNavigateBack()
                Page.METHODS -> viewModel.currentPage = Page.AGREEMENT

                Page.IN_APP -> {
                    viewModel.currentPage = Page.METHODS

                    scope.launch {
                        delay(timeMillis = 500L)
                        viewModel.passwordTextField = TextFieldValue()
                    }
                }

                Page.IN_BROWSER -> viewModel.currentPage = Page.METHODS
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is UserDeactivationViewModel.Event.OnDeactivateSuccess -> {
                    launch(SupervisorJob()) {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "Your account has successfully deactivated.",
                            duration = SnackbarDuration.Long,
                        )
                    }

                    onNavigateMore()
                }

                is UserDeactivationViewModel.Event.OnDeactivateFailed -> {
                    launch(SupervisorJob()) {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "Error: ${event.message}",
                        )
                    }
                }

                is UserDeactivationViewModel.Event.OnNavigateMore -> {
                    onNavigateMore()
                }
            }
        }
    }

    BackHandler(
        enabled = viewModel.currentPage != Page.AGREEMENT,
        onBack = popPage,
    )

    UserDeactivationInAppConfirmationDialog(
        isVisible = viewModel.showInAppConfirmationDialog,
        onDismissRequest = {
            viewModel.showInAppConfirmationDialog = false
        },
        onConfirm = viewModel::deactivateAccount,
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            UserDeactivationTopAppBar(
                activeUserName = activeUser.name,
                currentPage = viewModel.currentPage,
                isNavigationButtonEnabled = !viewModel.isLoading,
                isLoading = viewModel.isLoading,
                onNavigateBack = popPage,
            )
        },
        bottomBar = {
            ProductNavigationBarSpacer()
        },
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
            .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal)),
    ) { innerPadding ->
        UserDeactivationNavGraph(
            innerPadding = innerPadding,
            viewModel = viewModel,
        )
    }
}
