package com.sam.kladishoes.presentation.registration.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.sam.kladishoes.model.Response
import com.sam.kladishoes.presentation.components.*
import com.sam.kladishoes.services.LoginResponse
import com.sam.kladishoes.utils.UiEvents
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    navigate: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    LoginScreen(scaffoldState,viewModel,viewModel.loginResponse.value,navigate,onBackClicked = onBackClicked)
}

@Composable
private fun LoginScreen(
    scaffoldState: ScaffoldState,
    viewModel: LoginViewModel,
    response: LoginResponse,
    navigate:()->Unit,
    onBackClicked: () -> Unit
) {
    val visible = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Scaffold(scaffoldState = scaffoldState) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colors.background
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                AuthenticationTitle(title = "Welcome Back\nLog In to make A Purchase") {
                    onBackClicked()
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.75f),
                    verticalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = viewModel.email.value,
                        label = "Email",
                        keyboardType = KeyboardType.Email
                    ) { email ->
                        viewModel.onEmailChange(email)
                    }
                    PasswordTextField(
                        value = viewModel.password.value,
                        label = "Password",
                        visible = visible.value,
                        onVisibilityChange = { visible.value = !visible.value }
                    ) { password ->
                        viewModel.onPasswordChange(password)
                    }
                    when (response) {
                        is Response.Loading -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        is Response.Success -> {
                            if (response.data) {
                                AuthButton(text = "LOGIN") {
                                    val message = viewModel.login()
                                    message?.let { errorMessage ->
                                        showMessage(context, errorMessage)
                                    }
                                }
                                navigate()
                            } else {
                                AuthButton(text = "LOGIN") {
                                    val message = viewModel.login()
                                    message?.let { errorMessage ->
                                        showMessage(context, errorMessage)
                                    }
                                }
                            }
                        }
                        is Response.Failure -> {
                            AuthButton(text = "LOGIN") {
                                val message = viewModel.login()
                                message?.let { errorMessage ->
                                    showMessage(context, errorMessage)
                                }
                            }
                            showMessage(context, response.e.message!!)
                        }
                    }
                }
            }
        }
    }
}


