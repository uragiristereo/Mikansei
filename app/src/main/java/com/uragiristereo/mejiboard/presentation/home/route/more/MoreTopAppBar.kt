package com.uragiristereo.mejiboard.presentation.home.route.more

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.uragiristereo.mejiboard.R
import com.uragiristereo.mejiboard.presentation.common.composable.product.ProductTopAppBar

@Composable
fun MoreTopAppBar(
    modifier: Modifier = Modifier,
) {
    ProductTopAppBar(
        title = {
            Text(text = stringResource(id = R.string.more_label))
        },
        modifier = modifier,
    )
}
