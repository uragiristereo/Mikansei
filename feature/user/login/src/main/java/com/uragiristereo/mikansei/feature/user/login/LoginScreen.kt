package com.uragiristereo.mikansei.feature.user.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.product.component.ProductNavigationBarSpacer
import com.uragiristereo.mikansei.core.product.component.ProductStatusBarSpacer
import com.uragiristereo.mikansei.core.product.component.ProductTopAppBar
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.LocalScaffoldState
import com.uragiristereo.mikansei.feature.user.login.core.DanbooruLogo
import com.uragiristereo.mikansei.feature.user.login.dialog.LoginDialog
import com.uragiristereo.mikansei.feature.user.login.dialog.LoginState
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun LoginScreen(
    onNavigateBack: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val scaffoldState = LocalScaffoldState.current

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var name by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue()) }
    var apiKey by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue()) }

    val isButtonEnabled by remember {
        derivedStateOf {
            name.text.isNotBlank() && apiKey.text.isNotBlank()
        }
    }

    LaunchedEffect(key1 = viewModel.loginState) {
        if (viewModel.loginState == LoginState.Success) {
            scope.launch(SupervisorJob()) {
                scaffoldState.snackbarHostState.showSnackbar(message = "Login success!")
            }

            onNavigateBack()
        }
    }

    LoginDialog(
        state = viewModel.loginState,
        onDismiss = viewModel::onDialogDismiss,
        onCancelClick = viewModel::cancelLogin,
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ProductStatusBarSpacer {
                ProductTopAppBar(
                    title = {
                        Text(text = "Add an account")
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
        bottomBar = {
            ProductNavigationBarSpacer()
        },
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
            .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal)),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding()
                .padding(innerPadding),
        ) {
            DanbooruLogo()

            Text(
                text = "Login to Danbooru",
                style = MaterialTheme.typography.subtitle1,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary.copy(alpha = 0.87f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 8.dp,
                    ),
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    autoCorrectEnabled = false,
                ),
                label = {
                    Text(text = "Name")
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        vertical = 4.dp,
                        horizontal = 16.dp,
                    ),
            )

            OutlinedTextField(
                value = apiKey,
                onValueChange = { apiKey = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                label = {
                    Text(text = "API Key")
                },
                visualTransformation = when {
                    viewModel.isApiKeyVisible -> VisualTransformation.None
                    else -> PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(
                        onClick = viewModel::toggleApiKeyVisible,
                        content = {
                            Icon(
                                painter = painterResource(
                                    id = when {
                                        viewModel.isApiKeyVisible -> R.drawable.visibility_off_fill
                                        else -> R.drawable.visibility_fill
                                    },
                                ),
                                contentDescription = null,
                            )
                        },
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        vertical = 4.dp,
                        horizontal = 16.dp,
                    ),
            )

            Button(
                enabled = isButtonEnabled,
                onClick = {
                    viewModel.performLogin(name.text, apiKey.text)
                },
                content = {
                    Text(text = "Login".uppercase())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp)
                    .padding(horizontal = 16.dp),
            )
        }
    }
}
