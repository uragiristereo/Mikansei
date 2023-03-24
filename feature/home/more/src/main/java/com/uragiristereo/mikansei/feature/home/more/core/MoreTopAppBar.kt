package com.uragiristereo.mikansei.feature.home.more.core

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.uragiristereo.mikansei.core.product.component.ProductTopAppBar
import com.uragiristereo.mikansei.core.resources.R

@Composable
internal fun MoreTopAppBar(
    modifier: Modifier = Modifier,
) {
    ProductTopAppBar(
        title = {
            Text(text = stringResource(id = R.string.more_label))
        },
        modifier = modifier,
    )
}
