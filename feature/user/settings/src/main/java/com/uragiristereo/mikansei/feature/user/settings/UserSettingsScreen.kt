package com.uragiristereo.mikansei.feature.user.settings

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.model.user.preference.DetailSizePreference
import com.uragiristereo.mikansei.core.product.component.ProductTopAppBar
import com.uragiristereo.mikansei.core.product.preference.*
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.composable.NavigationBarSpacer
import com.uragiristereo.mikansei.core.ui.composable.SettingTip
import com.uragiristereo.mikansei.core.ui.extension.defaultPaddings
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
internal fun UserSettingsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserSettingsViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()

    val bottomSheetPreferenceState = rememberBottomSheetPreferenceState(
        onItemSelected = viewModel::setBottomSheetPreferenceState,
    )

    Scaffold(
        topBar = {
            ProductTopAppBar(
                title = {
                    Column {
                        Text(text = "Account settings")

                        Text(
                            text = viewModel.activeUser?.name.orEmpty(),
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
        },
        modifier = modifier.defaultPaddings(),
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                SwitchPreference(
                    title = "Safe mode",
                    subtitle = "Show only safe (general) images. Hide sensitive, questionable and explicit images.",
                    selected = viewModel.activeUser?.safeMode ?: false,
                    onSelectedChange = viewModel::onSafeModeChange,
                    icon = null,
                    enabled = !viewModel.loading,
                )
            }

            item {
                val safeMode = viewModel.activeUser?.safeMode

                val showPreference = when {
                    safeMode == null -> false
                    safeMode -> false
                    else -> true
                }

                AnimatedContent(targetState = showPreference) { state ->
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
                    selected = viewModel.activeUser?.showDeletedPosts ?: false,
                    onSelectedChange = viewModel::onShowDeletedPostsChange,
                    icon = null,
                    enabled = !viewModel.loading,
                )
            }

            item {
                DropDownPreference(
                    state = rememberDropDownPreferenceState(
                        items = DetailSizePreference.values(),
                        selectedItem = viewModel.activeUser?.defaultImageSize,
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

            item {
                NavigationBarSpacer()
            }
        }
    }

    BottomSheetPreference(bottomSheetPreferenceState)
}
