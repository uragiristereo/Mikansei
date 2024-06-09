package com.uragiristereo.mikansei.feature.about

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.model.Environment
import com.uragiristereo.mikansei.core.product.component.ProductStatusBarSpacer
import com.uragiristereo.mikansei.core.product.component.ProductTopAppBar
import com.uragiristereo.mikansei.core.product.preference.PreferenceCategory
import com.uragiristereo.mikansei.core.product.preference.RegularPreference
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.composable.NavigationBarSpacer
import com.uragiristereo.mikansei.core.ui.extension.versionCode
import com.uragiristereo.mikansei.core.ui.extension.versionName
import com.uragiristereo.mikansei.feature.about.contibutor.ContributorItem
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun AboutScreen(
    onNavigateBack: () -> Unit,
    viewModel: AboutViewModel = koinViewModel(),
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            ProductStatusBarSpacer {
                ProductTopAppBar(
                    title = {
                        Text(text = "About")
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
            NavigationBarSpacer()
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
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 24.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mikansei_logo),
                        contentDescription = null,
                        modifier = Modifier.size(128.dp),
                    )
                }
            }

            item {
                val versionName = context.versionName
                val versionCode = context.versionCode
                val flavor = viewModel.environment.flavor.name.lowercase()

                RegularPreference(
                    title = "Version",
                    subtitle = "$versionName ($versionCode) [$flavor]",
                    icon = painterResource(id = R.drawable.android),
                    onClick = {
                        val url = when (viewModel.environment.flavor) {
                            Environment.Flavor.OSS -> "https://github.com/uragiristereo/Mikansei/releases"
                            Environment.Flavor.PLAY -> "https://play.google.com/store/apps/details?id=com.uragiristereo.mikansei"
                        }.toUri()

                        val intent = Intent().apply {
                            action = Intent.ACTION_VIEW
                            data = url
                        }

                        context.startActivity(intent)
                    },
                )
            }

            item {
                RegularPreference(
                    title = "GitHub repository",
                    subtitle = "Star, report bugs, request features, and contribute to the project",
                    icon = painterResource(id = R.drawable.github_icon),
                    onClick = {
                        val intent = Intent().apply {
                            action = Intent.ACTION_VIEW
                            data = "https://github.com/uragiristereo/Mikansei/releases".toUri()
                        }

                        context.startActivity(intent)
                    },
                )
            }

            item {
                RegularPreference(
                    title = "Discord server",
                    subtitle = "Chat, get help, and get announcements",
                    icon = painterResource(id = R.drawable.discord_icon),
                    onClick = {
                        val intent = Intent().apply {
                            action = Intent.ACTION_VIEW
                            data = "https://discord.gg/YMyVNsFvpC".toUri()
                        }

                        context.startActivity(intent)
                    },
                )
            }

            item {
                RegularPreference(
                    title = "Danbooru",
                    subtitle = "Made possible by the Danbooru API",
                    imageIcon = painterResource(id = R.drawable.danbooru),
                    onClick = {
                        val intent = Intent().apply {
                            action = Intent.ACTION_VIEW
                            data = "${viewModel.baseUrl}/wiki_pages/help:api".toUri()
                        }

                        context.startActivity(intent)
                    },
                )
            }

            item {
                RegularPreference(
                    title = "Privacy policy",
                    subtitle = null,
                    onClick = {
                        val intent = Intent().apply {
                            action = Intent.ACTION_VIEW
                            data =
                                "https://github.com/uragiristereo/Mikansei/blob/main/PRIVACY_POLICY.md".toUri()
                        }

                        context.startActivity(intent)
                    },
                )
            }

            item {
                PreferenceCategory(title = "Contributor(s)")
            }

            if (viewModel.isLoading) {
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 12.dp,
                            ),
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                items(items = viewModel.contributors) { contributor ->
                    ContributorItem(
                        item = contributor,
                        onClick = {
                            val intent = Intent().apply {
                                action = Intent.ACTION_VIEW
                                data = contributor.htmlUrl.toUri()
                            }

                            context.startActivity(intent)
                        },
                    )
                }
            }

            if (viewModel.isFailedToLoadContributors) {
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 12.dp,
                            ),
                    ) {
                        Text(
                            text = "Failed to load other contributors",
                            style = MaterialTheme.typography.body2,
                        )
                    }
                }
            }
        }
    }
}
