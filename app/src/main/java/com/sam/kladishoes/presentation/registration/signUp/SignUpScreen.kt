package com.sam.kladishoes.presentation.registration.signUp

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sam.kladishoes.model.Response
import com.sam.kladishoes.presentation.components.*
import com.sam.kladishoes.services.SignUpResponse

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
    onLogInClicked: () -> Unit,
    onNavigate: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val response = viewModel.signUpResponse.value
    SignUpScreen(
        scaffoldState,
        modifier,
        viewModel,
        response,
        context,
        onNavigate,
        onLogInClicked
    )
}

@Composable
private fun SignUpScreen(
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel,
    response: SignUpResponse,
    context: Context,
    navigate: () -> Unit,
    onLogInClicked: () -> Unit
) {
    val passwordVisible = remember {
        mutableStateOf(false)
    }
    val confirmPasswordVisible = remember {
        mutableStateOf(false)
    }
    Scaffold(scaffoldState = scaffoldState) { paddingValues ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                AuthenticationTitle(title = "Create Account \nto Make A Purchase") {

                }
                TextField(
                    value = viewModel.userName.value,
                    modifier = Modifier,
                    label = "Name"
                ) {
                    viewModel.onUserNameChange(it)
                }
                TextField(
                    value = viewModel.userEmail.value,
                    modifier = Modifier,
                    label = "Email",
                    keyboardType = KeyboardType.Email
                ) {
                    viewModel.onUserEmailChange(it)
                }
                TextField(
                    value = viewModel.phone.value,
                    modifier = Modifier,
                    label = "Phone",
                    keyboardType = KeyboardType.Phone
                ) {
                    viewModel.onUserPhoneChange(it)
                }
                TextField(
                    value = viewModel.location.value,
                    modifier = Modifier,
                    label = "Location"
                ) {
                    viewModel.onLocationChange(it)
                }
                PasswordTextField(
                    value = viewModel.password.value,
                    modifier = Modifier,
                    label = "Password",
                    onVisibilityChange = {
                        passwordVisible.value = !passwordVisible.value
                    },
                    visible = passwordVisible.value
                ) {
                    viewModel.onUserPasswordChange(it)
                }
                PasswordTextField(
                    value = viewModel.confirmPassword.value,
                    modifier = Modifier,
                    label = "Confirm Password",
                    onVisibilityChange = {
                        confirmPasswordVisible.value = !confirmPasswordVisible.value
                    },
                    visible = confirmPasswordVisible.value
                ) {
                    viewModel.onConfirmPasswordChange(it)
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
                            AuthButton {
                                val message = viewModel.signUp()
                                message?.let {
                                    showMessage(context, it)
                                }
                            }
                            navigate()
                        } else {
                            AuthButton {
                                val message = viewModel.signUp()
                                message?.let {
                                    showMessage(context, it)
                                }
                            }
                        }
                    }
                    is Response.Failure -> {
                        AuthButton {
                            val message = viewModel.signUp()
                            message?.let {
                                showMessage(context, it)
                            }
                        }
                        showMessage(context, response.e.message!!)
                    }
                }
                SwitchAuthText {
                    onLogInClicked()
                }
            }
        }
    }
}


@Composable
fun SwitchAuthText(
    modifier: Modifier = Modifier,
    message: String = "Already have an account? ",
    clickable: String = "Log In",
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = message)
        Text(
            text = clickable,
            modifier = Modifier
                .clickable { onClick() },
            style = MaterialTheme.typography.body1.copy(
                color = MaterialTheme.colors.primaryVariant
            )
        )
    }
}









