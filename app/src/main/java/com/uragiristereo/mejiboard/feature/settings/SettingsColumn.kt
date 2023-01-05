package com.uragiristereo.mejiboard.feature.settings

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.uragiristereo.mejiboard.core.resources.R
import com.uragiristereo.mejiboard.core.common.ui.composable.NavigationBarSpacer
import com.uragiristereo.mejiboard.core.preferences.model.DetailSizePreference
import com.uragiristereo.mejiboard.core.preferences.model.ThemePreference
import com.uragiristereo.mejiboard.feature.settings.preference.BottomSheetPreferenceState
import com.uragiristereo.mejiboard.feature.settings.preference.DropDownPreference
import com.uragiristereo.mejiboard.feature.settings.preference.PreferenceCategory
import com.uragiristereo.mejiboard.feature.settings.preference.RegularPreference
import com.uragiristereo.mejiboard.feature.settings.preference.SwitchPreference
import com.uragiristereo.mejiboard.feature.settings.preference.rememberDropDownPreferenceState
import com.uragiristereo.mejiboard.feature.settings.preference.rememberSwitchPreferenceState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsColumn(
    columnState: LazyListState,
    bottomSheetPreferenceState: BottomSheetPreferenceState,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()

//    val languageState = rememberDropDownPreferenceState(
//        items = PreferenceLanguage.items,
//        selectedItem = PreferenceItem(key = PreferenceLanguage.KEY_SYSTEM_DEFAULT, title = "System default - English (US)"),
//        onItemSelected = {
//            Timber.i(it.key)
//        },
//    )

    val themeState = rememberDropDownPreferenceState(
        preference = ThemePreference,
        selectedItemKey = viewModel.preferences.theme,
        onItemSelected = remember {
            { theme ->
                viewModel.updatePreferences { it.copy(theme = theme.key) }
            }
        }
    )

    val dohState = rememberSwitchPreferenceState(
        selected = viewModel.preferences.dohEnabled,
        onSelectedChange = remember {
            { selected ->
                viewModel.updatePreferences { it.copy(dohEnabled = selected) }
            }
        },
    )

    LazyColumn(
        state = columnState,
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        item {
            PreferenceCategory(title = stringResource(id = R.string.settings_category_interface))
        }

//        item {
//            DropDownPreference(
//                state = languageState,
//                title = "Language",
//                icon = painterResource(id = R.drawable.translate),
//            )
//        }

        item {
            DropDownPreference(
                title = stringResource(id = R.string.settings_theme),
                state = themeState,
                icon = painterResource(
                    id = when (viewModel.preferences.theme) {
                        ThemePreference.KEY_LIGHT -> R.drawable.dark_mode
                        ThemePreference.KEY_DARK -> R.drawable.dark_mode_fill
                        else -> when {
                            isSystemInDarkTheme() -> R.drawable.dark_mode_fill
                            else -> R.drawable.dark_mode
                        }
                    }
                ),
            )
        }

        item {
            SwitchPreference(
                title = stringResource(id = R.string.settings_black_dark_theme),
                subtitle = null,
                selected = viewModel.preferences.blackTheme,
                onSelectedChange = remember {
                    { selected ->
                        viewModel.updatePreferences { data -> data.copy(blackTheme = selected) }
                    }
                },
                enabled = when {
                    viewModel.preferences.theme == ThemePreference.KEY_LIGHT -> false
                    viewModel.preferences.theme == ThemePreference.KEY_DARK -> true
                    isSystemInDarkTheme() -> true
                    else -> false
                },
            )
        }

        item {
            SwitchPreference(
                title = stringResource(id = R.string.settings_dynamic_accent_colors),
                subtitle = stringResource(id = R.string.settings_dynamic_accent_colors_desc),
                selected = viewModel.preferences.monetEnabled,
                onSelectedChange = remember {
                    { selected ->
                        viewModel.updatePreferences { data -> data.copy(monetEnabled = selected) }
                    }
                },
                icon = painterResource(id = R.drawable.format_color_fill),
                enabled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
            )
        }

        item {
            Divider()
        }

        item {
            PreferenceCategory(title = stringResource(id = R.string.settings_category_behavior))
        }

        item {
            RegularPreference(
                title = stringResource(id = R.string.settings_default_booru_source),
                subtitle = viewModel.booruSources.selectedItem?.titleResId
                    ?.let { stringResource(id = it) } ?: stringResource(id = R.string.none),
                icon = painterResource(id = R.drawable.public_globe),
                onClick = {
                    scope.launch {
                        bottomSheetPreferenceState.navigate(data = viewModel.booruSources)
                    }
                },
            )
        }

        item {
            RegularPreference(
                title = stringResource(id = R.string.settings_booru_listing_mode),
                subtitle = viewModel.ratingFilters.selectedItem?.titleResId
                    ?.let { stringResource(id = it) } ?: stringResource(id = R.string.none),
                onClick = {
                    scope.launch {
                        bottomSheetPreferenceState.navigate(data = viewModel.ratingFilters)
                    }
                },
            )
        }

        item {
            RegularPreference(
                title = stringResource(id = R.string.settings_manage_booru_api_keys),
                subtitle = stringResource(id = R.string.settings_manage_booru_api_keys_desc),
                icon = painterResource(id = R.drawable.api),
                onClick = {
                    // TODO
                },
            )
        }

        item {
            DropDownPreference(
                state = rememberDropDownPreferenceState(
                    preference = DetailSizePreference,
                    selectedItemKey = viewModel.preferences.detailSize,
                    onItemSelected = remember {
                        { detailSize ->
                            viewModel.updatePreferences { it.copy(detailSize = detailSize.key) }
                        }
                    },
                ),
                title = stringResource(id = R.string.settings_image_detail_size),
                icon = painterResource(id = R.drawable.aspect_ratio),
            )
        }

        item {
            Divider()
        }

        item {
            PreferenceCategory(title = stringResource(id = R.string.settings_category_advanced))
        }

        item {
            SwitchPreference(
                state = dohState,
                title = stringResource(id = R.string.settings_doh),
                subtitle = buildAnnotatedString {
                    append(text = "${stringResource(id = R.string.settings_doh_desc)} ")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(text = "(${stringResource(id = R.string.settings_doh_desc_recommended)})")
                    }
                },
                icon = painterResource(id = R.drawable.vpn_lock),
            )
        }

        item {
            SwitchPreference(
                state = dohState,
                title = stringResource(id = R.string.settings_block_content_from_recent_apps),
                subtitle = null,
            )
        }

        item {
            Divider()
        }

        item {
            PreferenceCategory(title = stringResource(id = R.string.settings_category_miscellaneous))
        }

        item {
            SwitchPreference(
                state = dohState,
                title = stringResource(id = R.string.settings_auto_clean_cache),
                subtitle = stringResource(id = R.string.settings_auto_clean_cache_desc),
                icon = painterResource(id = R.drawable.mop),
            )
        }

        item {
            RegularPreference(
                title = stringResource(id = R.string.settings_clear_cache_now),
                subtitle = buildAnnotatedString {
                    append(text = "${stringResource(id = R.string.settings_clear_cache_now_desc)} ")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(text = "40.12 MB")
                    }

                    append(text = " (${stringResource(id = R.string.settings_clear_cache_now_desc_estimated)})")
                },
                icon = painterResource(id = R.drawable.cached),
                onClick = { /*TODO*/ },
            )
        }

        item {
            RegularPreference(
                title = stringResource(id = R.string.settings_check_for_update),
                subtitle = buildAnnotatedString {
                    append(text = "${stringResource(id = R.string.settings_current_version)} ")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(text = "v2.0.0")
                    }

                    append(text = "\n${stringResource(id = R.string.settings_latest_version)} ")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(text = "v2.0.0")
                        append(text = "\n\n${stringResource(id = R.string.settings_using_latest_version)}")
                    }
                },
                icon = painterResource(id = R.drawable.update),
                onClick = { /*TODO*/ },
            )
        }

        item {
            NavigationBarSpacer()
        }
    }
}
