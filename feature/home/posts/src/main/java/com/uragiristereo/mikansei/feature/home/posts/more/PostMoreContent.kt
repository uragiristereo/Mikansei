package com.uragiristereo.mikansei.feature.home.posts.more

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.product.shared.postfavoritevote.PostFavoriteVoteViewModel
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.LocalSnackbarHostState
import com.uragiristereo.mikansei.core.ui.composable.ClickableSection
import com.uragiristereo.mikansei.core.ui.composable.PostHeader
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.bottomSheetContentPadding
import com.uragiristereo.mikansei.feature.home.posts.more.core.FavoriteSection
import com.uragiristereo.mikansei.feature.home.posts.more.core.ScoreSection
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Composable
internal fun PostMoreContent(
    onDismiss: suspend () -> Unit,
    onPostClick: (Post) -> Unit,
    onDownloadClick: (Post) -> Unit,
    onShareClick: (Post) -> Unit,
    onAddToFavoriteGroupClick: (Post) -> Unit,
    postFavoriteVoteViewModel: PostFavoriteVoteViewModel,
) {
    val context = LocalContext.current
    val snackbarHostState = LocalSnackbarHostState.current
    val scope = rememberCoroutineScope()

    val postState by postFavoriteVoteViewModel.post.collectAsState()
    val postFavoriteVoteState by postFavoriteVoteViewModel.favoriteVote.collectAsState()
    val favoriteCount by postFavoriteVoteViewModel.favoriteCount.collectAsState()
    val voteCount by postFavoriteVoteViewModel.voteCount.collectAsState()
    val post = postState

    LaunchedEffect(key1 = Unit) {
        postFavoriteVoteViewModel.notLoggedInEvent.collect {
            onDismiss()
            launch(SupervisorJob()) {
                snackbarHostState.showSnackbar(message = context.getString(R.string.please_login))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Vertical))
            .bottomSheetContentPadding(),
    ) {
        PostHeader(
            title = "#${post?.id ?: 0}",
            previewUrl = post?.medias?.preview?.url,
            aspectRatio = post?.aspectRatio,
            onClick = {
                if (post != null) {
                    onPostClick(post)
                }
            },
        )

        Divider()

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
        ) {
            FavoriteSection(
                checked = postFavoriteVoteState?.isInFavorites,
                onCheckedChange = postFavoriteVoteViewModel::onFavoriteChange,
                count = favoriteCount,
                enabled = true,
            )

            ScoreSection(
                score = voteCount,
                state = postFavoriteVoteState?.voteStatus,
                enabled = true,
                onVoteChange = postFavoriteVoteViewModel::onVoteChange,
            )

            ClickableSection(
                title = stringResource(id = R.string.add_to_action),
                onClick = {
                    scope.launch(SupervisorJob()) {
                        val activeUser = postFavoriteVoteViewModel.activeUser
                        if (activeUser.isNotAnonymous()) {
                            if (post != null) {
                                onAddToFavoriteGroupClick(post)
                            }
                        } else {
                            onDismiss()
                            snackbarHostState.showSnackbar(
                                message = context.getString(R.string.please_login),
                            )
                        }
                    }
                },
                icon = painterResource(id = R.drawable.add_to_photos),
            )

            Divider()

            ClickableSection(
                title = stringResource(id = R.string.download_action),
                onClick = {
                    scope.launch {
                        onDismiss()

                        if (post != null) {
                            onDownloadClick(post)
                        }
                    }
                },
                icon = painterResource(id = R.drawable.download),
            )

            ClickableSection(
                title = stringResource(id = R.string.share_action),
                onClick = {
                    if (post != null) {
                        onShareClick(post)
                    }
                },
                icon = painterResource(id = R.drawable.share),
            )
        }
    }
}
