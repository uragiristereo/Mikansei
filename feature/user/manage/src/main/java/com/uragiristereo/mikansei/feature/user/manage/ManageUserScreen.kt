package com.uragiristereo.mikansei.feature.user.manage

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.uragiristereo.safer.compose.navigation.core.NavRoute
import com.uragiristereo.mikansei.core.product.component.ProductTopAppBar
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.composable.NavigationBarSpacer
import com.uragiristereo.mikansei.core.ui.composable.SectionTitle
import com.uragiristereo.mikansei.core.ui.extension.defaultPaddings
import com.uragiristereo.mikansei.core.ui.extension.plus
import com.uragiristereo.mikansei.core.ui.navigation.UserRoute
import com.uragiristereo.mikansei.feature.user.manage.core.ProfileItem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ManageUserScreen(
    onNavigate: (NavRoute) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ManageUserViewModel = koinViewModel(),
) {
    val activeUser by viewModel.activeUser.collectAsState()
    val inactiveUsers by viewModel.inactiveUsers.collectAsState()

    Scaffold(
        topBar = {
            ProductTopAppBar(
                title = {
                    Text(text = "Manage accounts")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.arrow_back),
                                contentDescription = null,
                            )
                        },
                    )
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onNavigate(UserRoute.Login)
                },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.add),
                        contentDescription = null,
                    )
                },
                modifier = Modifier
                    .windowInsetsPadding(
                        WindowInsets.navigationBars.only(sides = WindowInsetsSides.Bottom),
                    ),
            )
        },
        modifier = modifier.defaultPaddings(),
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding + PaddingValues(vertical = 16.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            item(key = "active_title") {
                SectionTitle(
                    text = "Active",
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }

            item(key = activeUser.id) {
                ProfileItem(
                    user = activeUser,
                    onSettingsClick = {
                        onNavigate(UserRoute.Settings)
                    },
                    onActivateClick = { },
                    onLogoutClick = { },
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .animateItemPlacement(),
                )

            }

            item(key = "inactive_title") {
                SectionTitle(
                    text = "Inactive",
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }

            items(
                items = inactiveUsers,
                key = { it.id },
            ) { user ->
                ProfileItem(
                    user = user,
                    onSettingsClick = {
                        onNavigate(UserRoute.Settings)
                    },
                    onActivateClick = viewModel::switchActiveUser,
                    onLogoutClick = viewModel::logoutUser,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .animateItemPlacement(),
                )
            }

            item {
                NavigationBarSpacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = 56.dp + 32.dp,
                        ),
                )
            }
        }
    }
}
