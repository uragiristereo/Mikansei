package com.uragiristereo.mikansei.feature.user.settings

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.model.preferences.user.DetailSizePreference
import com.uragiristereo.mikansei.core.product.component.ProductNavigationBarSpacer
import com.uragiristereo.mikansei.core.product.component.ProductStatusBarSpacer
import com.uragiristereo.mikansei.core.product.component.ProductTopAppBar
import com.uragiristereo.mikansei.core.product.preference.BottomSheetPreference
import com.uragiristereo.mikansei.core.product.preference.DropDownPreference
import com.uragiristereo.mikansei.core.product.preference.RegularPreference
import com.uragiristereo.mikansei.core.product.preference.SwitchPreference
import com.uragiristereo.mikansei.core.product.preference.getTitleString
import com.uragiristereo.mikansei.core.product.preference.rememberBottomSheetPreferenceState
import com.uragiristereo.mikansei.core.product.preference.rememberDropDownPreferenceState
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.composable.SettingTip
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
internal fun UserSettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: UserSettingsViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val activeUser by viewModel.activeUser.collectAsState()

    val bottomSheetPreferenceState = rememberBottomSheetPreferenceState(
        onItemSelected = viewModel::setBottomSheetPreferenceState,
    )

    Scaffold(
        topBar = {
            ProductStatusBarSpacer {
                ProductTopAppBar(
                    title = {
                        Column {
                            Text(text = "Account settings")

                            Text(
                                text = activeUser.name,
                                fontSize = 14.sp,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.74f),
                            )
                        }
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
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                SwitchPreference(
                    title = "Safe mode",
                    subtitle = "Show only safe (general) images. Hide sensitive, questionable and explicit images.",
                    selected = activeUser.danbooru.safeMode,
                    onSelectedChange = viewModel::onSafeModeChange,
                    icon = null,
                    enabled = !viewModel.loading,
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
                            subtitle = viewModel.ratingFilters.selectedItem?.getTitleString().orEmpty(),
                            onClick = {
                                scope.launch {
                                    bottomSheetPreferenceState.navigate(data = viewModel.ratingFilters)
                                }
                            },
                            enabled = !viewModel.loading,
                        )
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
                    enabled = !viewModel.loading,
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
                    enabled = !viewModel.loading,
                )
            }

            item {
                Divider()
            }

            item {
                SettingTip(text = "(*) indicates a Mikansei feature and won't be synced with Danbooru.")
            }
        }
    }

    BottomSheetPreference(bottomSheetPreferenceState)
}
