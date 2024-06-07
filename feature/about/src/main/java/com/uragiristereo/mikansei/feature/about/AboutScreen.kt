package com.uragiristereo.mikansei.feature.about

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.model.dagger.feature.rememberContainer
import com.uragiristereo.mikansei.core.model.dagger.viewmodel.assistedViewModel
import com.uragiristereo.mikansei.core.product.component.ProductStatusBarSpacer
import com.uragiristereo.mikansei.core.product.component.ProductTopAppBar
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.feature.about.di.AboutContainer
import timber.log.Timber

@Composable
internal fun AboutScreen(
    onNavigateBack: () -> Unit,
) {
    val container = rememberContainer(::AboutContainer)
    val viewModel: AboutViewModel = assistedViewModel { container.viewModelFactory }

    LaunchedEffect(key1 = Unit) {
        Timber.d(viewModel.random.value)
    }

    Scaffold(
        topBar = {
            ProductStatusBarSpacer {
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
            }
        },
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
            .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal)),
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier,
        ) {
            // TODO: about screen contents
        }
    }
}
