package com.uragiristereo.mejiboard.feature.home.collections

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.uragiristereo.mejiboard.core.product.component.ProductTopAppBar
import com.uragiristereo.mejiboard.core.resources.R
import com.uragiristereo.mejiboard.core.ui.composable.Banner

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
