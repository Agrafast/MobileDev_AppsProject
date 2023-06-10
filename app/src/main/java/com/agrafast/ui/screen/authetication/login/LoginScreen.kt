package com.agrafast.ui.screen.authetication.login

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.agrafast.ui.theme.AgraFastTheme
import com.agrafast.util.Helper

@Composable
fun LoginScreen(
  appState: AppState,
  authViewModel: AuthViewModel,
  context: Context = LocalContext.current
) {


  val userState = authViewModel.userState.collectAsState()

  LaunchedEffect(userState.value) {
    if (userState.value is AuthState.Authenticated<User>) {
      val user = (userState.value as AuthState.Authenticated).data!!
      appState.setUser(user)
      appState.navController.navigate(Screen.Home.route) {
        popUpTo(Screen.Login.route) {
          inclusive = true
        }
      }
    }
  }

  LazyColumn {
    item {
      AuthHeader()
    }
    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
    item {
      AuthSubHeader("Hi, Welcome Back!", "Sign in to your AgraFast account.")
    }
    item {
      Spacer(modifier = Modifier.height(16.dp))
    }

    item {
      val noInternet = stringResource(id = R.string.no_internet)
      LoginForm(
        isLoading = userState.value is AuthState.Loading,
        onLoginClick = { email, password ->
        if (Helper.isOnline(context)) {
          authViewModel.signIn(email, password)
        } else {
          appState.showSnackbar(noInternet)
        }
      })
    }

    item {
      AuthFooter(AuthType.Login, onActionClick = {
        appState.navController.navigate(Screen.Register.route) {
          popUpTo(Screen.Login.route) {
            inclusive = true
          }
        }
      })
    }
  }

}

@Composable
fun LoginForm(onLoginClick: (email: String, password: String) -> Unit, isLoading: Boolean) {
  var email by remember { mutableStateOf("") }
  var isEmailError by remember { mutableStateOf(false) }
  var password by remember { mutableStateOf("") }
  var isPasswordError by remember { mutableStateOf(false) }

  fun handleClick() {
    isEmailError = email.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    isPasswordError = password.isEmpty() || password.length < 8
    if (!isEmailError && !isPasswordError) {
      onLoginClick(email.trim(), password)
    }
  }
  //Email TextField
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(24.dp)
  ) {
    InputText(
      value = email,
      isError = isEmailError,
      label = "Email",
      leadingRes = R.drawable.ic_profile,
      onValueChange = { email = it })
    InputText(
      value = password,
      isError = isPasswordError,
      label = "Kata sandi",
      leadingRes = R.drawable.ic_password,
      onValueChange = { password = it },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
    PrimaryButton(
      isLoading = isLoading,
      text = stringResource(R.string.login),
      onClick = {
        handleClick()
      })

  }
}

@Preview
@Composable
fun DefaultPreview() {
  AgraFastTheme {
    val authViewModel: AuthViewModel = hiltViewModel()
    LoginScreen(rememberAppState(), authViewModel = authViewModel)
  }
}




