package com.agrafast.ui.screen.authetication.register

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.agrafast.AppState
import com.agrafast.R
import com.agrafast.data.firebase.model.User
import com.agrafast.domain.AuthState
import com.agrafast.rememberAppState
import com.agrafast.ui.component.InputText
import com.agrafast.ui.component.PrimaryButton
import com.agrafast.ui.navigation.Screen
import com.agrafast.ui.screen.AuthViewModel
import com.agrafast.ui.screen.authetication.component.AuthFooter
import com.agrafast.ui.screen.authetication.component.AuthHeader
import com.agrafast.ui.screen.authetication.component.AuthSubHeader
import com.agrafast.ui.screen.authetication.component.AuthType

@Composable
fun RegisterScreen(
  appState: AppState,
  authViewModel: AuthViewModel
) {
  // State
  val userState = authViewModel.userState.collectAsState()
  var errorAuthMessage: String? by rememberSaveable { mutableStateOf(null) }

  // SideEffects
  LaunchedEffect(Unit){
    authViewModel.resetUserState()
  }
  LaunchedEffect(userState.value) {
    if (userState.value is AuthState.Authenticated<User>) {
      appState.navController.navigate(Screen.Home.route) {
        popUpTo(Screen.Register.route) {
          inclusive = true
        }
      }
    }
    errorAuthMessage = authViewModel.getAuthErrorMessage(AuthType.Register)
  }

  LazyColumn() {
    item {
      AuthHeader()
    }
    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
    item {
      AuthSubHeader("Hi, Welcome!", "Register new account to use AgraFast.")
    }
    item {
      Spacer(modifier = Modifier.height(16.dp))
    }
    item {
      RegisterForm(
        isLoading = userState.value is AuthState.Loading,
        errorMessage = errorAuthMessage,
        onRegisterClick = { name, email, phone, password ->
          authViewModel.signUp(name, email, phone, password)
        })
    }
    item {
      AuthFooter(AuthType.Register, onActionClick = {
        appState.navController.navigate(Screen.Login.route) {
          popUpTo(Screen.Register.route) {
            inclusive = true
          }
        }
      })
    }
  }
}

@Composable
fun RegisterForm(
  onRegisterClick: (name: String, email: String, phone: String, password: String) -> Unit,
  isLoading: Boolean,
  errorMessage: String? = null
) {
  var name by rememberSaveable { mutableStateOf("") }
  var isNameError by rememberSaveable { mutableStateOf(false) }
  var email by rememberSaveable { mutableStateOf("") }
  var isEmailError by rememberSaveable { mutableStateOf(false) }
  var phone by rememberSaveable { mutableStateOf("") }
  var isPhoneError by rememberSaveable { mutableStateOf(false) }
  var password by rememberSaveable { mutableStateOf("") }
  var isPasswordError by rememberSaveable { mutableStateOf(false) }

  fun handleClick() {
    isNameError = name.trim().isEmpty() || name.trim().length < 6
    isEmailError =
      email.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    isPasswordError = password.isEmpty() || password.length < 8
    isPhoneError =
      phone.trim().isEmpty() || !android.util.Patterns.PHONE.matcher(phone.trim()).matches()
    if (!isNameError && !isEmailError && !isPhoneError && !isPasswordError) {
      onRegisterClick(name.trim(), email.trim(), phone.trim(), password)
    }
  }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
      .animateContentSize(),
    verticalArrangement = Arrangement.spacedBy(24.dp)
  ) {
    if (errorMessage?.isNotEmpty() == true) {
      Text(
        text = errorMessage,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.error
      )
    }
    InputText(
      value = name,
      label = "Nama",
      isError = isNameError,
      leadingRes = R.drawable.ic_user,
      onValueChange = { name = it })

    InputText(
      value = email,
      isError = isEmailError,
      label = "Email",
      leadingRes = R.drawable.ic_email,
      onValueChange = { email = it },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
    InputText(
      value = phone,
      isError = isPhoneError,
      label = "No. Telp",
      leadingRes = R.drawable.ic_phone,
      onValueChange = { phone = it },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
    )
    InputText(
      isError = isPasswordError,
      value = password,
      label = "Kata sandi",
      leadingRes = R.drawable.ic_password,
      onValueChange = { password = it },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
    PrimaryButton(isLoading = isLoading, text = "Register", onClick = {
      handleClick()
    })
  }
}

@Preview
@Composable
fun DefaultPreview() {
  Surface(
  ) {
    RegisterScreen(appState = rememberAppState(), authViewModel = hiltViewModel())
  }
}


