package com.uragiristereo.mikansei.feature.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.product.component.ProductStatusBarSpacer
import com.uragiristereo.mikansei.core.product.preference.LocalIconPadding
import com.uragiristereo.mikansei.feature.settings.core.SettingsTopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    Scaffold(
        topBar = {
            ProductStatusBarSpacer {
                SettingsTopAppBar(
                    onNavigateBack = onNavigateBack,
                    onMoreClick = { /*TODO*/ },
                )
            }
        },
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
            .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal)),
    ) { innerPadding ->
        CompositionLocalProvider(
            values = arrayOf(
                LocalIconPadding provides true,
            ),
            content = {
                SettingsColumn(
                    contentPadding = innerPadding,
                    viewModel = viewModel,
                )
            },
        )
    }
}
