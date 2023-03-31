package com.uragiristereo.mikansei.feature.home.more

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.uragiristereo.safer.compose.navigation.core.NavRoute
import com.uragiristereo.mikansei.core.product.component.ProductSetSystemBarsColor
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.WindowSize
import com.uragiristereo.mikansei.core.ui.composable.SectionTitle
import com.uragiristereo.mikansei.core.ui.extension.plus
import com.uragiristereo.mikansei.core.ui.extension.versionName
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.navigation.UserRoute
import com.uragiristereo.mikansei.core.ui.rememberWindowSize
import com.uragiristereo.mikansei.feature.home.more.core.MoreTopAppBar
import com.uragiristereo.mikansei.feature.home.more.core.NavigationItem
import com.uragiristereo.mikansei.feature.home.more.core.UserHeader
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun MoreScreen(
    onNavigate: (NavRoute) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MoreViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val windowSize = rememberWindowSize()
    val gridSize = remember { 2 }
    val span: (LazyGridItemSpanScope.() -> GridItemSpan) = {
        GridItemSpan(currentLineSpan = gridSize)
    }

    ProductSetSystemBarsColor(
        navigationBarColor = Color.Transparent,
    )

    Scaffold(
        topBar = {
            MoreTopAppBar()
        },
        modifier = modifier.statusBarsPadding(),
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(gridSize),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = innerPadding + PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
            ),
        ) {
            item(span = span) {
                SectionTitle(text = "Account")
            }

            item(span = span) {
                Spacer(modifier = Modifier.height(8.dp))
            }

            viewModel.activeUser?.let { activeUser ->
                item(span = span) {
                    activeUser.apply {
                        UserHeader(
                            name = name,
                            nameAlias = nameAlias,
                            userId = id,
                            level = level,
                            onProfileClick = { },
                            onMoreClick = {
                                onNavigate(UserRoute.Manage)
                            },
                        )
                    }
                }
            }

            item(span = span) {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item(span = span) {
                SectionTitle(text = "Navigation")
            }

            item(span = span) {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                NavigationItem(
                    text = "Pools",
                    painter = painterResource(id = R.drawable.photo_library),
                    elevated = false,
                    onClick = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.feature_under_development),
                            Toast.LENGTH_SHORT,
                        ).show()
                    },
                    modifier = Modifier.padding(bottom = 12.dp),
                )
            }

            item {
                NavigationItem(
                    text = "Wiki",
                    painter = painterResource(id = R.drawable.lightbulb),
                    elevated = false,
                    onClick = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.feature_under_development),
                            Toast.LENGTH_SHORT,
                        ).show()
                    },
                    modifier = Modifier.padding(bottom = 12.dp),
                )
            }

            item {
                NavigationItem(
                    text = "Saved searches",
                    painter = painterResource(id = R.drawable.sell),
                    elevated = false,
                    onClick = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.feature_under_development),
                            Toast.LENGTH_SHORT,
                        ).show()
                    },
                    modifier = Modifier.padding(bottom = 12.dp),
                )
            }

            item {
                NavigationItem(
                    text = "Filters",
                    painter = painterResource(id = R.drawable.filter_list),
                    elevated = false,
                    onClick = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.feature_under_development),
                            Toast.LENGTH_SHORT,
                        ).show()
                    },
                    modifier = Modifier.padding(bottom = 12.dp),
                )
            }

            item {
                NavigationItem(
                    text = "Search history",
                    painter = painterResource(id = R.drawable.history),
                    elevated = false,
                    onClick = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.feature_under_development),
                            Toast.LENGTH_SHORT,
                        ).show()
                    },
                    modifier = Modifier.padding(bottom = 12.dp),
                )
            }

            item {
                NavigationItem(
                    text = "Settings",
                    painter = painterResource(id = R.drawable.settings),
                    onClick = {
                        onNavigate(MainRoute.Settings)
                    },
                    modifier = Modifier.padding(bottom = 12.dp),
                )
            }

            item {
                NavigationItem(
                    text = "About",
                    painter = painterResource(id = R.drawable.info),
                    onClick = {
                        onNavigate(MainRoute.About)
                    },
                    modifier = Modifier.padding(bottom = 12.dp),
                )
            }

            item(span = span) {
                val version = remember { context.versionName }

                Text(
                    text = "V$version",
                    style = MaterialTheme.typography.overline,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 4.dp)
                )
            }

            item(span = span) {
                Spacer(modifier = Modifier.padding(bottom = 8.dp))
            }

            item(span = span) {
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
