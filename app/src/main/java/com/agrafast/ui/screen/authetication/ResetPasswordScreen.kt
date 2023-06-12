package com.agrafast.ui.screen.authetication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.agrafast.AppState
import com.agrafast.R
import com.agrafast.domain.AuthState
import com.agrafast.ui.component.InputText
import com.agrafast.ui.component.PrimaryButton
import com.agrafast.ui.component.SimpleActionBar
import com.agrafast.ui.screen.AuthViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ResetPasswordScreen(
  appState: AppState,
  authViewModel: AuthViewModel,
  viewModel: ResetPasswordViewModel = hiltViewModel()
) {
  // States
  var email by remember { mutableStateOf("") }
  var isEmailError by remember { mutableStateOf(false) }
  val resetState = viewModel.resetState.collectAsState()

  val keyBoardController = LocalSoftwareKeyboardController.current


  LaunchedEffect(resetState.value) {
    if (resetState.value is AuthState.Success) {
      appState.showSnackbar("Email atur ulang kata sandi berhasil dikirim.")
      viewModel.resetState.value = AuthState.Default
    } else if (resetState.value is AuthState.Error) {
      appState.showSnackbar("Gagal mengirim email atur ulang kata sandi.")
      viewModel.resetState.value = AuthState.Default
    }

  }
  Surface() {
    Column() {
      SimpleActionBar(
        title = stringResource(id = R.string.reset_password),
        onBackClicked = { appState.navController.navigateUp() },
      )
      Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
      ) {
        Text(
          modifier = Modifier,
          text = "Masukkan email untuk reset ulang kata sandi anda.",
          style = MaterialTheme.typography.bodyMedium,
        )
        InputText(
          value = email,
          isError = isEmailError,
          label = "Email",
          leadingRes = R.drawable.ic_profile,
          onValueChange = { email = it })
        PrimaryButton(
          isLoading = resetState.value is AuthState.Loading,
          text = stringResource(R.string.send),
          onClick = {
            isEmailError =
              email.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim())
                .matches()
            if (!isEmailError) {
              keyBoardController?.hide()
              viewModel.resetPassword(appState, email)
            }
          })
      }
    }
  }
}