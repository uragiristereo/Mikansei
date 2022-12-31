package com.uragiristereo.mejiboard.presentation.about

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.uragiristereo.mejiboard.R
import com.uragiristereo.mejiboard.presentation.common.composable.product.ProductTopAppBar

@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            ProductTopAppBar(
                title = {
                    Text(text = "About")
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
            )
        },
        modifier = modifier.systemBarsPadding(),
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier,
        ) {
            // TODO: about screen contents
        }
    }
}
