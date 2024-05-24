package com.uragiristereo.mikansei.feature.user.delegation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.product.component.ProductNavigationBarSpacer
import com.uragiristereo.mikansei.feature.user.delegation.core.UserDelegationTopAppBar
import com.uragiristereo.mikansei.feature.user.delegation.list.UserDelegationList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserDelegationSettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: UserDelegationSettingsViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val activeUser by viewModel.activeUser.collectAsState()
    val users by viewModel.users.collectAsState()

    Scaffold(
        topBar = {
            UserDelegationTopAppBar(
                activeUserName = activeUser.name,
                onNavigateBack = onNavigateBack,
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
        UserDelegationList(
            items = users,
            contentPadding = innerPadding,
            onItemClick = { user ->
                viewModel.setDelegation(user.userId)

                scope.launch {
                    delay(timeMillis = 100L)
                    onNavigateBack()
                }
            },
        )
    }
}
