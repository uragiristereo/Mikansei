package com.uragiristereo.mikansei.feature.home.more

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.product.component.ProductSetSystemBarsColor
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.WindowSize
import com.uragiristereo.mikansei.core.ui.composable.ClickableSection
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.rememberWindowSize
import com.uragiristereo.mikansei.feature.home.more.core.MoreTopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun MoreScreen(
    onNavigate: (MainRoute) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MoreViewModel = koinViewModel(),
) {
    val windowSize = rememberWindowSize()

    ProductSetSystemBarsColor(
        navigationBarColor = Color.Transparent,
    )

    Scaffold(
        topBar = {
            MoreTopAppBar()
        },
        modifier = modifier.statusBarsPadding(),
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
        ) {
            item {
                Icon(
                    painter = painterResource(id = R.drawable.meji),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp),
                )
            }

            item {
                ClickableSection(
                    title = stringResource(id = R.string.selected_booru),
                    subtitle = stringResource(id = viewModel.selectedBooru.nameResId),
                    icon = painterResource(id = R.drawable.public_globe),
                    verticalPadding = 16.dp,
                    onClick = {
                        onNavigate(MainRoute.Settings)
                    },
                )
            }

            item {
                ClickableSection(
                    title = stringResource(id = R.string.search_history_label),
                    icon = painterResource(id = R.drawable.history),
                    verticalPadding = 16.dp,
                    onClick = {
                        onNavigate(MainRoute.SearchHistory)
                    },
                )
            }

            item {
                ClickableSection(
                    title = stringResource(id = R.string.saved_searches_label),
                    icon = painterResource(id = R.drawable.sell),
                    verticalPadding = 16.dp,
                    onClick = {
                        onNavigate(MainRoute.SavedSearches)
                    },
                )
            }

            item {
                ClickableSection(
                    title = stringResource(id = R.string.filters_label),
                    icon = painterResource(id = R.drawable.filter_list),
                    subtitle = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(
                                text = stringResource(
                                    id = when {
                                        viewModel.preferences.filtersEnabled -> R.string.enabled_action
                                        else -> R.string.disabled_action
                                    },
                                ),
                            )
                        }

                        val count = stringResource(id = R.string.filters_n_tags_enabled, viewModel.enabledFiltersCount)

                        append(text = " - $count")
                    },
                    verticalPadding = 16.dp,
                    onClick = {
                        onNavigate(MainRoute.Filters)
                    },
                )
            }

            item {
                Divider()
            }

            item {
                ClickableSection(
                    title = stringResource(id = R.string.settings_label),
                    icon = painterResource(id = R.drawable.settings),
                    verticalPadding = 16.dp,
                    onClick = {
                        onNavigate(MainRoute.Settings)
                    },
                )
            }

            item {
                ClickableSection(
                    title = stringResource(id = R.string.about_label),
                    icon = painterResource(id = R.drawable.info),
                    verticalPadding = 16.dp,
                    onClick = {
                        onNavigate(MainRoute.About)
                    },
                )
            }

            item {
                Text(
                    text = "V1.0.0",
                    style = MaterialTheme.typography.overline,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 4.dp),
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .windowInsetsPadding(
                            insets = WindowInsets.navigationBars.only(sides = WindowInsetsSides.Bottom),
                        )
                        .padding(
                            bottom = when (windowSize) {
                                WindowSize.COMPACT -> 56.dp
                                else -> 0.dp
                            } + 8.dp + 1.dp,
                        ),
                )
            }
        }
    }
}
