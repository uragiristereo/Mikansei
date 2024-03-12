package com.uragiristereo.mikansei.feature.user.settings

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.model.preferences.user.DetailSizePreference
import com.uragiristereo.mikansei.core.product.component.ProductNavigationBarSpacer
import com.uragiristereo.mikansei.core.product.component.ProductPullRefreshIndicator
import com.uragiristereo.mikansei.core.product.component.ProductStatusBarSpacer
import com.uragiristereo.mikansei.core.product.preference.BottomSheetPreference
import com.uragiristereo.mikansei.core.product.preference.DropDownPreference
import com.uragiristereo.mikansei.core.product.preference.RegularPreference
import com.uragiristereo.mikansei.core.product.preference.SwitchPreference
import com.uragiristereo.mikansei.core.product.preference.getTitleString
import com.uragiristereo.mikansei.core.product.preference.rememberBottomSheetPreferenceState
import com.uragiristereo.mikansei.core.product.preference.rememberDropDownPreferenceState
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.LocalScaffoldState
import com.uragiristereo.mikansei.core.ui.composable.Scaffold2
import com.uragiristereo.mikansei.core.ui.composable.SettingTip
import com.uragiristereo.mikansei.core.ui.extension.forEach
import com.uragiristereo.mikansei.feature.user.settings.core.UserSettingsTopAppBar
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun UserSettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: UserSettingsViewModel = koinViewModel(),
) {
    val scaffoldState = LocalScaffoldState.current
    val scope = rememberCoroutineScope()
    val activeUser by viewModel.activeUser.collectAsState()

    val bottomSheetPreferenceState = rememberBottomSheetPreferenceState(
        onItemSelected = viewModel::setBottomSheetPreferenceState,
    )

    val shouldEnableSettings = !viewModel.loading && !viewModel.failed

    LaunchedEffect(key1 = Unit) {
        viewModel.snackbarChannel.forEach {
            scaffoldState.snackbarHostState.showSnackbar(message = it)
        }
    }

    Scaffold2(
        scaffoldState = scaffoldState,
        topBar = {
            ProductStatusBarSpacer {
                UserSettingsTopAppBar(
                    name = activeUser.name,
                    onNavigateBack = onNavigateBack,
                    onRefreshClick = viewModel::requestSync,
                )
            }
        },
        bottomBar = {
            ProductNavigationBarSpacer()
        },
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
            .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal)),
    ) { innerPadding ->
        val pullRefreshState = rememberPullRefreshState(
            refreshing = viewModel.loading,
            onRefresh = viewModel::requestSync,
        )

        Box(
            modifier = Modifier.pullRefresh(pullRefreshState),
        ) {
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier.fillMaxSize(),
            ) {
                if (!viewModel.safeModeEnvironment) {
                    item {
                        SwitchPreference(
                            title = "Safe mode",
                            subtitle = "Show only safe (general) images. Hide sensitive, questionable and explicit images.",
                            selected = activeUser.danbooru.safeMode,
                            onSelectedChange = viewModel::onSafeModeChange,
                            icon = null,
                            enabled = shouldEnableSettings,
                        )
                    }

                    item {
                        AnimatedContent(
                            targetState = !activeUser.danbooru.safeMode,
                            label = "PostRatingFilter",
                        ) { state ->
                            if (state) {
                                RegularPreference(
                                    title = "Posts rating listing filters (*)",
                                    subtitle = viewModel.ratingFilters.selectedItem?.getTitleString()
                                        .orEmpty(),
                                    onClick = {
                                        scope.launch {
                                            bottomSheetPreferenceState.navigate(data = viewModel.ratingFilters)
                                        }
                                    },
                                    enabled = shouldEnableSettings,
                                )
                            }
                        }
                    }
                }

                item {
                    SwitchPreference(
                        title = "Show deleted posts",
                        subtitle = null,
                        selected = activeUser.danbooru.showDeletedPosts,
                        onSelectedChange = viewModel::onShowDeletedPostsChange,
                        icon = null,
                        enabled = shouldEnableSettings,
                    )
                }

                item {
                    DropDownPreference(
                        state = rememberDropDownPreferenceState(
                            items = DetailSizePreference.entries.toTypedArray(),
                            selectedItem = activeUser.danbooru.defaultImageSize,
                            onItemSelected = viewModel::onDetailSizeChange,
                        ),
                        title = stringResource(id = R.string.settings_image_detail_size),
                        icon = null,
                        enabled = shouldEnableSettings,
                    )
                }

                item {
                    Divider()
                }

                item {
                    SettingTip(text = "(*) indicates a Mikansei feature and won't be synced with Danbooru.")
                }
            }

            ProductPullRefreshIndicator(
                refreshing = viewModel.loading,
                state = pullRefreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(innerPadding),
            )
        }
    }

    BottomSheetPreference(bottomSheetPreferenceState)
}
