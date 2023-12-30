package com.uragiristereo.mikansei.feature.home.favorites.favgroup.more

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.composable.ClickableSection
import com.uragiristereo.mikansei.core.ui.composable.PostHeader
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavGroupMoreContent(
    onDismiss: suspend () -> Unit,
    favoriteGroup: Favorite,
    onFavGroupClick: () -> Unit,
    onEditFavGroupClick: () -> Unit,
    onDeleteFavGroupClick: () -> Unit,
    viewModel: FavGroupMoreViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val customTabsIntent = remember { CustomTabsIntent.Builder().build() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Vertical))
            .padding(
                top = 16.dp,
                bottom = 8.dp,
            ),
    ) {
        PostHeader(
            title = stringResource(id = R.string.favorite_group),
            subtitle = favoriteGroup.name,
            previewUrl = favoriteGroup.thumbnailUrl,
            aspectRatio = 1f,
            onClick = onFavGroupClick,
        )

        Divider()

        Spacer(modifier = Modifier.height(8.dp))

        ClickableSection(
            title = stringResource(id = R.string.edit),
            icon = painterResource(id = R.drawable.edit),
            onClick = onEditFavGroupClick,
        )

        ClickableSection(
            title = stringResource(id = R.string.delete),
            icon = painterResource(id = R.drawable.delete),
            onClick = onDeleteFavGroupClick,
        )

        Divider()

        ClickableSection(
            title = stringResource(id = R.string.open_externally_action),
            icon = painterResource(id = R.drawable.open_in_browser),
            onClick = {
                scope.launch {
                    val uri = "${viewModel.getBaseUrl()}/posts?tags=favgroup:${favoriteGroup.id}".toUri()

                    onDismiss()
                    customTabsIntent.launchUrl(context, uri)
                }
            },
        )
    }
}
