package com.uragiristereo.mejiboard.presentation.home.route.collections

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.uragiristereo.mejiboard.R
import com.uragiristereo.mejiboard.presentation.common.composable.Banner
import com.uragiristereo.mejiboard.presentation.common.composable.product.ProductTopAppBar

@Composable
fun CollectionsScreen(
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            ProductTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.collections_label))
                },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        Banner(
            icon = painterResource(id = R.drawable.construction),
            text = {
                Text(
                    text = stringResource(id = R.string.feature_under_development),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6,
                )
            },
            modifier = Modifier.padding(innerPadding),
        )
    }
}
