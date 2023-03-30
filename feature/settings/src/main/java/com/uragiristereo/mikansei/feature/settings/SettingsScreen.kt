package com.uragiristereo.mikansei.feature.settings

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.uragiristereo.mikansei.core.product.component.ProductSetSystemBarsColor
import com.uragiristereo.mikansei.core.product.preference.LocalIconPadding
import com.uragiristereo.mikansei.core.ui.extension.defaultPaddings
import com.uragiristereo.mikansei.feature.settings.core.SettingsTopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SettingsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    ProductSetSystemBarsColor()

    Scaffold(
        topBar = {
            SettingsTopAppBar(
                onNavigateBack = onNavigateBack,
                onMoreClick = { /*TODO*/ },
            )
        },
        modifier = modifier.defaultPaddings(),
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
