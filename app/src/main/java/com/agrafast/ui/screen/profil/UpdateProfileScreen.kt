package com.agrafast.ui.screen.profil

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.agrafast.AppState
import com.agrafast.R
import com.agrafast.data.firebase.model.User
import com.agrafast.domain.UIState
import com.agrafast.rememberAppState
import com.agrafast.ui.component.InputText
import com.agrafast.ui.component.PrimaryButton
import com.agrafast.ui.component.SimpleActionBar
import com.agrafast.ui.screen.AuthViewModel
import com.agrafast.ui.theme.AgraFastTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class UpdateType {
  PHOTO,
  PASSWORD,
  EMAIL,
  PROFILE
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UpdateProfileScreen(
  appState: AppState,
  authViewModel: AuthViewModel,
  updateType: UpdateType,
  viewModel: UpdateProfileViewModel = hiltViewModel()
) {
  // State
  val updateState = viewModel.updateState.collectAsState()
  val errorMessage = viewModel.errorMessage.collectAsState()

  val keyBoardController = LocalSoftwareKeyboardController.current

  var pageTitle = ""
  var typeString = ""

  when (updateType) {
    UpdateType.EMAIL -> {
      typeString = "Email"
      pageTitle = stringResource(id = R.string.update_email)
    }

    UpdateType.PASSWORD -> {
      typeString = "Kata sandi"
      pageTitle = stringResource(id = R.string.update_password)
    }

    UpdateType.PHOTO -> {
      typeString = "Photo"
      pageTitle = stringResource(id = R.string.update_photo)
    }

    else -> {
      typeString = "Profile"
      pageTitle = stringResource(id = R.string.update_profile)
    }
  }

  // Side Effects
  LaunchedEffect(updateState.value) {
    if (updateState.value is UIState.Success<Nothing>) {
      authViewModel.updateCurrentAuthUser()
      appState.showSnackbar("$typeString berhasil diperbarui")
      appState.coroutineScope.launch {
        delay(500)
        appState.navController.navigateUp()
      }
    } else if (updateState.value is UIState.Error) {
      viewModel.getErrorMessage()
      appState.showSnackbar("Gagal memperbarui ${typeString.lowercase()}")
      viewModel.updateState.value = UIState.Default
    }
  }

  Surface {
    LazyColumn() {
      item {
        SimpleActionBar(
          title = pageTitle,
          onBackClicked = { appState.navController.navigateUp() },
        )
      }
      item {
        when (updateType) {
          UpdateType.EMAIL -> {
            UpdateEmailForm(
              user = authViewModel.getUser(),
              isLoading = updateState.value is UIState.Loading,
              errorMessage = errorMessage.value,
              onUpdateClick = { newEmail, password ->
                keyBoardController?.hide()
                viewModel.updateEmail(newEmail, password)
              })
          }

          UpdateType.PASSWORD -> {
            UpdatePasswordForm(
              isLoading = updateState.value is UIState.Loading,
              errorMessage = errorMessage.value,
              onUpdateClick = { old, new->
                keyBoardController?.hide()
                viewModel.updatePassword( oldPassword =  old, newPassword = new)
              })
          }

          else -> {
            UpdateProfileForm(
              user = authViewModel.getUser(),
              isLoading = updateState.value is UIState.Loading,
              onUpdateClick = { name, phone ->
                keyBoardController?.hide()
                viewModel.updateProfil(name, phone)
              })
          }
        }
      }
    }
  }
}

@Composable
fun UpdateEmailForm(
  user: User,
  onUpdateClick: (newEmail: String, password: String) -> Unit,
  isLoading: Boolean,
  errorMessage: String? = null
) {
  var email by rememberSaveable { mutableStateOf(user.email) }
  var isEmailError by rememberSaveable { mutableStateOf(false) }

  var password by remember { mutableStateOf("") }
  var isPasswordError by remember { mutableStateOf(false) }

  fun handleClick() {
    isEmailError =
      email.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    isPasswordError = password.isEmpty() || password.length < 8
    if (!isEmailError && !isPasswordError) {
      onUpdateClick(email.trim(), password)
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
      value = email,
      isError = isEmailError,
      label = stringResource(id = R.string.email),
      leadingRes = R.drawable.ic_email,
      onValueChange = { email = it },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
    InputText(
      value = password,
      isError = isPasswordError,
      label = "Kata sandi",
      leadingRes = R.drawable.ic_password,
      onValueChange = { password = it },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
    PrimaryButton(isLoading = isLoading, text = stringResource(R.string.update), onClick = {
      handleClick()
    })
  }
}

@Composable
fun UpdatePasswordForm(
  onUpdateClick: (oldPassword:String, newPassword: String) -> Unit,
  isLoading: Boolean,
  errorMessage: String? = null
) {
  var oldPassword by remember { mutableStateOf("") }
  var isOldPasswordError by remember { mutableStateOf(false) }
  var newPassword by remember { mutableStateOf("") }
  var isNewPasswordError by remember { mutableStateOf(false) }
  var confirmationPassword by remember { mutableStateOf("") }
  var isConfirmationPasswordError by remember { mutableStateOf(false) }

  fun handleClick() {
    isOldPasswordError = oldPassword.isEmpty() || oldPassword.length < 8
    isNewPasswordError = newPassword.isEmpty() || newPassword.length < 8
    isConfirmationPasswordError = newPassword != confirmationPassword
    if (!isOldPasswordError && !isNewPasswordError && !isConfirmationPasswordError) {
      onUpdateClick(oldPassword, newPassword)
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
      value = oldPassword,
      isError = isOldPasswordError,
      label = "Kata sandi lama",
      leadingRes = R.drawable.ic_password,
      onValueChange = { oldPassword = it },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
    InputText(
      value = newPassword,
      isError = isNewPasswordError,
      label = "Kata sandi baru",
      leadingRes = R.drawable.ic_password,
      onValueChange = { newPassword = it },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
    InputText(
      value = confirmationPassword,
      isError = isConfirmationPasswordError,
      label = "Konfirmasi kata sandi",
      leadingRes = R.drawable.ic_password,
      onValueChange = { confirmationPassword = it },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
    PrimaryButton(isLoading = isLoading, text = stringResource(R.string.update), onClick = {
      handleClick()
    })
  }
}

@Composable
fun UpdateProfileForm(
  user: User,
  onUpdateClick: (name: String, phone: String) -> Unit,
  isLoading: Boolean,
  errorMessage: String? = null
) {
  var name by rememberSaveable { mutableStateOf(user.name) }
  var isNameError by rememberSaveable { mutableStateOf(false) }
  var phone by rememberSaveable { mutableStateOf(user.phone) }
  var isPhoneError by rememberSaveable { mutableStateOf(false) }

  fun handleClick() {
    isNameError = name.trim().isEmpty() || name.trim().length < 6
    isPhoneError =
      phone.trim().isEmpty() || !android.util.Patterns.PHONE.matcher(phone.trim()).matches()
    if (!isNameError && !isPhoneError) {
      onUpdateClick(name.trim(), phone.trim())
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
      label = stringResource(id = R.string.name),
      isError = isNameError,
      leadingRes = R.drawable.ic_user,
      onValueChange = { name = it })

    InputText(
      value = phone,
      isError = isPhoneError,
      label = stringResource(id = R.string.phone),
      leadingRes = R.drawable.ic_phone,
      onValueChange = { phone = it },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
    )
    PrimaryButton(isLoading = isLoading, text = stringResource(R.string.update), onClick = {
      handleClick()
    })
  }
}

@Composable
fun UpdateProfileScreenPreview() {
  AgraFastTheme {
    UpdateProfileScreen(
      appState = rememberAppState(),
      authViewModel = hiltViewModel(),
      UpdateType.PROFILE
    )
  }
}