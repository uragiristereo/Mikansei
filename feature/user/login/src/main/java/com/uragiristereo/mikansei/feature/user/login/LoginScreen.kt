package com.uragiristereo.mikansei.feature.user.login

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.product.component.ProductTopAppBar
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.extension.defaultPaddings
import com.uragiristereo.mikansei.feature.user.login.core.DanbooruLogo
import com.uragiristereo.mikansei.feature.user.login.dialog.LoginDialog
import com.uragiristereo.mikansei.feature.user.login.dialog.LoginState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun LoginScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

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
            Toast.makeText(context, "Login success!", Toast.LENGTH_SHORT).show()

            onNavigateBack()
        }
    }

    LaunchedEffect(key1 = scrollState.isScrollInProgress) {
        if (scrollState.isScrollInProgress) {
            keyboardController?.hide()
        }
    }

    LoginDialog(
        state = viewModel.loginState,
        onDismiss = viewModel::onDialogDismiss,
        onCancelClick = viewModel::cancelLogin,
    )

    Scaffold(
        topBar = {
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
        },
        modifier = modifier.defaultPaddings(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
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
                    autoCorrect = false,
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
                    .padding(
                        vertical = 18.dp,
                        horizontal = 16.dp,
                    ),
            )
        }
    }
}
