package com.uragiristereo.mejiboard.feature.home.more

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.uragiristereo.mejiboard.core.resources.R

@Composable
fun MoreTopAppBar(
    modifier: Modifier = Modifier,
) {
    com.uragiristereo.mejiboard.core.product.component.ProductTopAppBar(
        title = {
            Text(text = stringResource(id = R.string.more_label))
        },
        modifier = modifier,
    )
}
