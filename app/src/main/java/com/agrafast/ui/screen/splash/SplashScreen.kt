package com.agrafast.ui.screen.splash

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.agrafast.AppState
import com.agrafast.R
import com.agrafast.domain.AuthState
import com.agrafast.rememberAppState
import com.agrafast.ui.navigation.Screen
import com.agrafast.ui.screen.AuthViewModel
import com.agrafast.ui.theme.AgraFastTheme
import com.agrafast.ui.theme.Poppins
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlin.math.log

@Composable
fun SplashScreen(
  appState: AppState,
  viewModel: AuthViewModel = hiltViewModel()
) {
  // State
  val userState = viewModel.userState.collectAsState()
  val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.logo_splash))
  val logoAnimationState =
    animateLottieCompositionAsState(composition = composition)
  val lottieFinishedState = remember { mutableStateOf(false) }
  var routeState = remember { mutableStateOf(Screen.Profil.route) }


  // SideEffects
  LaunchedEffect(Unit) {
    viewModel.signIn()
  }
  LaunchedEffect(userState.value) {
    if (userState.value is AuthState.Authenticated) {
      val user = (userState.value as AuthState.Authenticated).data!!
      appState.setUser(user)
      routeState.value = Screen.Home.route
    }
  }
  // Launch when animation Finished and fetchUserDataFinished
  LaunchedEffect(lottieFinishedState.value, routeState.value){
    if(lottieFinishedState.value && userState.value !is AuthState.Loading){
      appState.navController.navigate(routeState.value) {
        popUpTo(Screen.Splash.route) {
          inclusive = true
        }
      }
    }
  }
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.onTertiaryContainer)
  ) {
    Column(
      Modifier
        .fillMaxWidth()
        .align(Alignment.BottomCenter)
        .padding(bottom = 22.dp),
      verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        color = Color.White,
        text = "AGRAFAST",
        fontFamily = Poppins,
        style = MaterialTheme.typography.titleLarge,
        fontSize = 28.sp
      )
      Text(
        modifier = Modifier.offset(y = (-12).dp),
        color = Color.White,
        text = "DISCOVER, DIAGNOSE, SUCCEED",
        style = MaterialTheme.typography.titleLarge,
        fontSize = 16.sp
      )
    }
    LottieAnimation(
      composition = composition,
      progress = logoAnimationState.progress
    )


    // Placed here because isAtEnd Only occurs one time, and its it milisecond
    // In that one moment, setThe isFinished
    if (logoAnimationState.isAtEnd && logoAnimationState.isPlaying) {
      lottieFinishedState.value = true
    }
  }
}

@Preview()
@Composable
fun DefaultPreview() {
  AgraFastTheme {
    SplashScreen(appState = rememberAppState())
  }
}