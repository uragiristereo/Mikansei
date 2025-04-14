package com.uragiristereo.mikansei.feature.wiki

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.getCategoryColor
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.product.component.ProductNavigationBarSpacer
import com.uragiristereo.mikansei.core.product.component.ProductPullRefreshContainer
import com.uragiristereo.mikansei.core.ui.LocalScaffoldState
import com.uragiristereo.mikansei.feature.wiki.core.HtmlContent
import com.uragiristereo.mikansei.feature.wiki.core.HtmlContentState
import com.uragiristereo.mikansei.feature.wiki.core.OtherNamesRow
import com.uragiristereo.mikansei.feature.wiki.core.WikiTitle
import com.uragiristereo.mikansei.feature.wiki.core.WikiTopAppBar
import com.uragiristereo.mikansei.feature.wiki.core.rememberHtmlContentState
import com.uragiristereo.mikansei.feature.wiki.posts.PostsSection
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WikiScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToWiki: (tag: String) -> Unit,
    onNavigateToViewer: (Post) -> Unit,
    onNavigateToPosts: (tags: String) -> Unit,
    viewModel: WikiViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val scaffoldState = LocalScaffoldState.current
    val wikiData = viewModel.wikiData
    val wiki = wikiData?.wiki
    val posts = wikiData?.posts
    val hasPosts = !posts.isNullOrEmpty()
    val colors = MaterialTheme.colors

    val customTabsIntent = remember { CustomTabsIntent.Builder().build() }

    val tagColor = remember(wikiData, colors) {
        wikiData?.tagCategory?.getCategoryColor(colors.isLight)
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.wikiLoading,
        onRefresh = viewModel::refresh,
    )

    val htmlContentState = rememberHtmlContentState { event ->
        when (event) {
            is HtmlContentState.ClickEvent.WikiItem -> {
                onNavigateToWiki(event.tag)
            }

            is HtmlContentState.ClickEvent.Post -> {
                viewModel.getPost(event.postId)
            }

            is HtmlContentState.ClickEvent.Browse -> {
                onNavigateToPosts(event.tags)
            }

            is HtmlContentState.ClickEvent.DanbooruUrl -> {
                val url = viewModel.baseUrl + event.path
                customTabsIntent.launchUrl(context, url.toUri())
            }

            is HtmlContentState.ClickEvent.ExternalUrl -> {
                customTabsIntent.launchUrl(context, event.url)
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is WikiViewModel.Event.OnPostSuccess -> onNavigateToViewer(event.post)

                is WikiViewModel.Event.OnError -> {
                    launch(SupervisorJob()) {
                        scaffoldState.snackbarHostState.showSnackbar("Error: ${event.message}")
                    }
                }
            }
        }
    }

    LaunchedEffect(wikiData, colors) {
        if (wiki != null && wiki.body.isNotBlank()) {
            htmlContentState.loadContent(wiki.body, colors)
        }
    }

    if (viewModel.postLoading) {
        Dialog(
            onDismissRequest = {
                viewModel.cancelGetPost()
            },
            content = {
                CircularProgressIndicator()
            },
        )
    }

    Scaffold(
        topBar = {
            WikiTopAppBar(
                wikiName = viewModel.wikiName,
                hasPosts = hasPosts,
                onNavigateBack = onNavigateBack,
                onSearchClick = onNavigateToSearch,
                onRefreshClick = viewModel::refresh,
                onBrowseClick = {
                    onNavigateToPosts(viewModel.wikiName)
                },
                onOpenExternallyClick = {
                    val url = "${viewModel.baseUrl}/wiki_pages/${viewModel.wikiName}"
                    customTabsIntent.launchUrl(context, url.toUri())
                },
            )
        },
        bottomBar = {
            ProductNavigationBarSpacer()
        },
        contentPadding = PaddingValues(0.dp),
        scaffoldState = scaffoldState,
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
            .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal)),
    ) { innerPadding ->
        ProductPullRefreshContainer(
            refreshing = viewModel.wikiLoading,
            state = pullRefreshState,
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize(),
        ) {
            AnimatedVisibility(
                visible = wikiData != null,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.fillMaxSize(),
            ) {
                if (wikiData != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(innerPadding)
                            .padding(vertical = 8.dp),
                    ) {
                        WikiTitle(
                            title = wikiData.tag,
                            color = tagColor,
                        )

                        if (wiki != null) {
                            if (wiki.otherNames.isNotEmpty()) {
                                OtherNamesRow(
                                    names = wiki.otherNames,
                                    onItemClick = { name ->
                                        val url = "https://www.pixiv.net/tags/$name/artworks"
                                        customTabsIntent.launchUrl(context, url.toUri())
                                    },
                                )
                            }
                        }

                        if (wiki != null) {
                            if (wiki.body.isNotBlank()) {
                                HtmlContent(
                                    state = htmlContentState,
                                    modifier = Modifier
                                        .animateContentSize()
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp),
                                )
                            } else {
                                Text(
                                    text = "This wiki page is empty.",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            horizontal = 16.dp,
                                            vertical = 8.dp,
                                        ),
                                )
                            }
                        } else {
                            Text(
                                text = "This wiki page does not exist.",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp,
                                        vertical = 8.dp,
                                    ),
                            )
                        }

                        if (posts != null && hasPosts) {
                            PostsSection(
                                posts = posts,
                                postCount = wikiData.postCount,
                                onPostClick = onNavigateToViewer,
                                onPostLabelClick = {
                                    onNavigateToPosts(viewModel.wikiName)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
