package com.uragiristereo.mikansei.feature.home.favorites.new_fav_group.core

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.uragiristereo.mikansei.core.product.component.ProductTopAppBar
import com.uragiristereo.mikansei.core.resources.R

@Composable
fun NewFavGroupTopAppBar(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ProductTopAppBar(
        title = {
            Text(text = "New Favorite Group")
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
        modifier = modifier,
    )
}
