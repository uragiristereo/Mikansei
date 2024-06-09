package com.uragiristereo.mikansei.feature.settings.core

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.uragiristereo.mikansei.core.product.component.ProductTopAppBar
import com.uragiristereo.mikansei.core.resources.R

@Composable
internal fun SettingsTopAppBar(
    onNavigateBack: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ProductTopAppBar(
        title = {
            Text(text = "Settings")
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
//        actions = {
//            IconButton(
//                onClick = onMoreClick,
//                content = {
//                    Icon(
//                        painter = painterResource(id = R.drawable.more_vert),
//                        contentDescription = null,
//                    )
//                },
//            )
//        },
        modifier = modifier,
    )
}
