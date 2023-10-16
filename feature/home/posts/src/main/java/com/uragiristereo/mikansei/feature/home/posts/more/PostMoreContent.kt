package com.uragiristereo.mikansei.feature.home.posts.more

import android.widget.Toast
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.composable.ClickableSection
import com.uragiristereo.mikansei.feature.home.posts.more.core.FavoriteSection
import com.uragiristereo.mikansei.feature.home.posts.more.core.PostMoreHeader
import com.uragiristereo.mikansei.feature.home.posts.more.core.ScoreSection
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun PostMoreContent(
    onDismiss: suspend () -> Unit,
    onPostClick: (Post) -> Unit,
    onDownloadClick: (Post) -> Unit,
    onShareClick: (Post) -> Unit,
    onAddToFavoriteGroupClick: (Post) -> Unit,
    viewModel: PostMoreViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val post = viewModel.post

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Vertical))
            .padding(top = 16.dp),
    ) {
        PostMoreHeader(
            postId = post.id,
            previewUrl = post.medias.preview.url,
            aspectRatio = post.aspectRatio,
            onClick = {
                onPostClick(post)
            },
        )

        Divider()

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
        ) {
            FavoriteSection(
                checked = viewModel.isPostInFavorites,
                onCheckedChange = viewModel::toggleFavorite,
                count = viewModel.favoriteCount,
                enabled = viewModel.favoriteButtonEnabled && viewModel.isPostUpdated,
            )

            ScoreSection(
                score = viewModel.score,
                state = viewModel.scoreState,
                enabled = viewModel.voteButtonEnabled && viewModel.isPostUpdated,
                onVoteChange = viewModel::onVoteChange,
            )

            ClickableSection(
                title = stringResource(id = R.string.add_to_action),
                onClick = {
                    scope.launch {
                        if (viewModel.activeUser.value.isNotAnonymous()) {
                            onDismiss()
                            onAddToFavoriteGroupClick(post)
                        } else {
                            Toast.makeText(context, "Please Login to use this feature!", Toast.LENGTH_SHORT).show()
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
                        onDownloadClick(post)
                    }
                },
                icon = painterResource(id = R.drawable.download),
            )

            ClickableSection(
                title = stringResource(id = R.string.share_action),
                onClick = {
                    scope.launch {
                        onDismiss()
                        onShareClick(post)
                    }
                },
                icon = painterResource(id = R.drawable.share),
            )
        }
    }
}
