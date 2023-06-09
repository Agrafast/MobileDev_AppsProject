package com.agrafast.ui.screen.authetication.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.agrafast.AppState
import com.agrafast.R
import com.agrafast.data.firebase.model.User
import com.agrafast.domain.AuthState
import com.agrafast.ui.navigation.Screen
import com.agrafast.ui.screen.AuthViewModel
import com.agrafast.ui.screen.authetication.component.BannerAuth
import com.agrafast.ui.screen.authetication.component.HeadlineText
import com.agrafast.ui.screen.authetication.component.TextLoginWith

@Composable
fun RegisterScreen(
  appState: AppState,
  authViewModel: AuthViewModel
) {
  // State
  val userState = authViewModel.userState.collectAsState()
  var name by rememberSaveable { mutableStateOf("") }
  var email by rememberSaveable { mutableStateOf("") }
  var phone by rememberSaveable { mutableStateOf("") }
  var password by rememberSaveable { mutableStateOf("") }
  var passwordVisible by rememberSaveable { mutableStateOf(false) }

  // SideEffects
  LaunchedEffect(userState.value) {
    if (userState.value is AuthState.Authenticated<User>) {
      val user = (userState.value as AuthState.Authenticated).data!!
      appState.setUser(user)
      appState.navController.navigate(Screen.Home.route) {
        popUpTo(Screen.Register.route) {
          inclusive = true
        }
      }
    }
  }

  Column() {
    BannerAuth()
    HeadlineText(
      text1 = "Signup to start with",
      text2 = "Ultimate Farming App"
    )
    Spacer(modifier = Modifier.height(16.dp))

    //Textfield Nama
    OutlinedTextField(
      value = name, onValueChange = { name = it },
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .padding(start = 12.dp, end = 12.dp),
      label = { Text(text = "Name") },
      keyboardOptions = KeyboardOptions.Default,
      leadingIcon = {
        Icon(
          painter = painterResource(R.drawable.ic_profile),
          contentDescription = ""
        )
      },
    )

    //Email TextField
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
      value = email, onValueChange = { email = it },
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .padding(start = 12.dp, end = 12.dp),
      label = { Text(text = "Email") },
      keyboardOptions = KeyboardOptions.Default,
      leadingIcon = {
        Icon(
          painter = painterResource(R.drawable.ic_profile),
          contentDescription = ""
        )
      },
    )


    //Telp TextField
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
      value = phone, onValueChange = { phone = it },
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .padding(start = 12.dp, end = 12.dp),
      label = { Text(text = "Telp. Number") },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
      leadingIcon = {
        Icon(
          painter = painterResource(R.drawable.ic_phone),
          contentDescription = ""
        )
      },
    )

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
      value = password,
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .padding(start = 12.dp, end = 12.dp),
      onValueChange = { password = it },
      label = { Text("Enter password") },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
      leadingIcon = {
        Icon(
          painter = painterResource(id = R.drawable.ic_password),
          contentDescription = ""
        )

      },
      trailingIcon = {
        val iconImage = if (passwordVisible) {
          painterResource(id = R.drawable.ic_visibility_on)
        } else {
          painterResource(id = R.drawable.ic_visibility_off)
        }

        IconButton(onClick = { passwordVisible = !passwordVisible }) {
          Icon(painter = iconImage, contentDescription = null)
        }
      },
      visualTransformation = if (passwordVisible) {
        VisualTransformation.None
      } else {
        PasswordVisualTransformation()
      }
    )
    Spacer(modifier = Modifier.height(16.dp))

    // Register Button
    Button(
      onClick = {
        authViewModel.signUp(
          name = name,
          email = email,
          phone = phone,
          password = password
        )
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 12.dp, end = 12.dp)
        .height(48.dp),
      shape = RoundedCornerShape(12.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
      )
    ) {
      Text("Register")
    }

    Spacer(modifier = Modifier.height(16.dp))


//    GoogleComponent(
//      text = "Signup in with Google", loadingText = "Create Account..."
//    )
    TextLoginWith(
      text = "Already have an account ? Login",
      changeTextColor = true,
      colorText = "Login",
      plainText = "Already have an account ? ",
      onRegisterClick = {
        appState.navController.navigate(Screen.Login.route) {
          popUpTo(Screen.Register.route) {
            inclusive = true
          }
        }
      }
    )


  }
//    AgraFastTheme {
//        // A surface container using the 'background' color from the theme
//        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//            Column() {
//                BannerAuth()
//                HeadlineText(text1 = "Signup to start with",
//                    text2 = "Ultimate Farming App")
//                Spacer(modifier = Modifier.height(16.dp))
//                TextField(labelValue = "Nama")
//                Spacer(modifier = Modifier.height(8.dp))
//                TextField(labelValue = "Email")
//                Spacer(modifier = Modifier.height(8.dp))
//                TextField(labelValue = "No.Telp", icon = R.drawable.ic_phone)
//                Spacer(modifier = Modifier.height(8.dp))
//                PasswordTextField()
//                ButtonAuth(text = "Register", false, navController = navController, loginPage = false)
//                GoogleComponent(
//                    text= "Signup in with Google", loadingText = "Create Account..."
//                )
//                TextLoginWith(
//                    text= "Already have an account ? Login", changeTextColor = true,
//                    colorText = "Login", plainText = "Already have an account ? ", onRegisterClick = {navController.navigate(Screen.Login.route)}
//                )
//            }
//        }
//    }
}


////@Preview
////@Composable
////fun RegisterScreenPreview() {
////    Surface(
////        color = Color.White,
////        modifier = Modifier.fillMaxSize()
////    ) {
////        RegisterScreen(appState = AppState(), navController = rememberNavController(), authViewModel = null)
////    }
////}


