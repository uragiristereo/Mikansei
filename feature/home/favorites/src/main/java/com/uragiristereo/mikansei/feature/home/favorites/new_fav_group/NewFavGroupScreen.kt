package com.uragiristereo.mikansei.feature.home.favorites.new_fav_group

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.product.component.ProductSetSystemBarsColor
import com.uragiristereo.mikansei.core.product.component.ProductTopAppBar
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.composable.SectionTitle
import com.uragiristereo.mikansei.core.ui.composable.SettingTip
import com.uragiristereo.mikansei.core.ui.extension.defaultPaddings
import com.uragiristereo.mikansei.core.ui.extension.forEach
import com.uragiristereo.mikansei.feature.home.favorites.new_fav_group.core.LoadingFab
import com.uragiristereo.mikansei.feature.home.favorites.new_fav_group.core.NewFavGroupState
import com.uragiristereo.mikansei.feature.home.favorites.new_fav_group.core.SubmitFab
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun NewFavGroupScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NewFavGroupViewModel = koinViewModel(),
) {
    val context = LocalContext.current

    var textField by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue()) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = viewModel) {
        viewModel.channel.forEach { state ->
            when (state) {
                NewFavGroupState.Success -> {
                    Toast.makeText(context, "Favorite group is successfully created", Toast.LENGTH_SHORT).show()

                    onNavigateBack()
                }

                is NewFavGroupState.Failed -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    ProductSetSystemBarsColor()

    Scaffold(
        topBar = {
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
            )
        },
        floatingActionButton = {
            AnimatedContent(
                targetState = viewModel.isLoading,
                transitionSpec = {
                    scaleIn() with scaleOut()
                },
            ) { state ->
                when {
                    !state -> SubmitFab(
                        onClick = {
                            when {
                                textField.text.isBlank() -> Toast.makeText(context, "Please enter a name!", Toast.LENGTH_SHORT).show()
                                else -> viewModel.createNewFavoriteGroup(name = textField.text)
                            }
                        },
                    )

                    else -> LoadingFab()
                }
            }
        },
        modifier = modifier
            .defaultPaddings()
            .navigationBarsPadding()
            .imePadding(),
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (viewModel.postId != null) {
                SettingTip(text = "Post #${viewModel.postId} will be added to this new favorite group.")

                Divider()
            }

            SectionTitle(
                text = "Name",
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp,
                    ),
            )

            OutlinedTextField(
                value = textField,
                onValueChange = {
                    if (!viewModel.isLoading) {
                        textField = it
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(focusRequester),
            )

            DisposableEffect(key1 = viewModel) {
                focusRequester.requestFocus()

                onDispose {
                    focusRequester.freeFocus()
                }
            }
        }
    }
}
