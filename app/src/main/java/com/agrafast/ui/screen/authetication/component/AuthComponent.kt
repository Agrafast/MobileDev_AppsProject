package com.agrafast.ui.screen.authetication.component

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.agrafast.R
import com.agrafast.ui.navigation.Screen
import kotlinx.coroutines.delay


//@Composable
//fun LoginAuth() {
//    AgraFastTheme {
//        // A surface container using the 'background' color from the theme
//        Column {
//            BannerAuth()
//            BottomShadow()
//            HeadlineText()
//            Spacer(modifier = Modifier.height(16.dp))
//            TextField("Email", R.drawable.ic_profile)
//            Spacer(modifier = Modifier.height(8.dp))
//            PasswordTextField()
//            ButtonAuth(navController = rememberNavController())
//            TextLoginWith(changeTextColor = false, useDivider = true)
//            Spacer(modifier = Modifier.height(16.dp))
////            GoogleComponent()
//            TextLoginWith(
//                "Doesn`t have an account?  Register", changeTextColor = true,
//                colorText = "Register", plainText = "Doesn`t have an account?",
//            )
//
//
//        }
//    }
//}

@Composable
fun BannerAuth(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
        .fillMaxWidth()
        .shadow(6.dp),
    contentAlignment = Alignment.CenterStart
  ) {
    Image(
      painter = painterResource(id = R.drawable.authetication_banner),
      contentScale = ContentScale.Crop,
      contentDescription = null,
      modifier = Modifier
          .sizeIn(maxHeight = 180.dp, minWidth = 360.dp)
          .fillMaxWidth()
    )
    Column {
      Text(
        text = stringResource(id = R.string.welcome_text).uppercase(),
        color = Color.White,
        style = MaterialTheme.typography.displaySmall,
        modifier = Modifier.padding(start = 16.dp),

        )
    }
  }
}

@Composable
fun BottomShadow(alpha: Float = 0.1f, height: Dp = 8.dp) {
  Box(
    modifier = Modifier
        .fillMaxWidth()
        .height(height)
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Black.copy(alpha = alpha),
                    Color.Transparent,
                )
            )
        )
  )
}

@Composable
fun HeadlineText(
  modifier: Modifier = Modifier, text1: String = "Login to start with",
  text2: String = "Ultimate Farming App"
) {
  Column {
    Text(
      text = text1, style = MaterialTheme.typography.titleMedium,
      modifier = Modifier.padding(top = 16.dp, start = 16.dp)
    )
    Text(
      text = text2, style = MaterialTheme.typography.titleMedium,
      modifier = Modifier.padding(start = 16.dp)
    )

  }
}

//private var emailTextFieldGlobal = ""
//
//@Composable
//fun TextField(labelValue: String, icon: Int = R.drawable.ic_profile) {
//  var textFieldValue by rememberSaveable { mutableStateOf("") }
//  emailTextFieldGlobal = textFieldValue
//  OutlinedTextField(
//    value = textFieldValue, onValueChange = { textFieldValue = it },
//    modifier = Modifier
//        .fillMaxWidth()
//        .clip(RoundedCornerShape(12.dp))
//        .padding(start = 12.dp, end = 12.dp),
//    label = { Text(text = labelValue) },
//    keyboardOptions = KeyboardOptions.Default,
//    leadingIcon = {
//      Icon(
//        painter = painterResource(icon),
//        contentDescription = ""
//      )
//    },
//  )
//}

private var passwordGlobal = ""

@Composable
fun PasswordTextField() {
  var password by rememberSaveable { mutableStateOf("") }
  var passwordVisible by remember { mutableStateOf(false) }

  passwordGlobal = password

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
}

////@Composable
////fun ButtonAuth(
////  text: String = "Login",
////  forgetPasswordFeature: Boolean = true,
////  loginPage: Boolean = true,
////  navController: NavController
////) {
////  val authViewModel: AuthViewModel = hiltViewModel()
////  val loginFlow = authViewModel.loginFlow.collectAsState()
////  val signupFlow = authViewModel.signupFlow.collectAsState()
////  val context = LocalContext.current
////  Log.d("TAG", loginFlow.toString())
////
////  val loadingState = if (loginPage) loginFlow.value else signupFlow.value
////
////  Spacer(modifier = Modifier.height(16.dp))
////  Box(
////    modifier = Modifier
////        .fillMaxWidth()
////        .sizeIn(minHeight = 100.dp)
////  ) {
////    Button(
////      onClick = { authViewModel.loginUser(emailTextFieldGlobal, passwordGlobal) },
////      modifier = Modifier
////          .fillMaxWidth()
////          .padding(start = 12.dp, end = 12.dp)
////          .align(Alignment.Center),
////      contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
////      colors = ButtonDefaults.buttonColors(
////        containerColor = colorResource(id = R.color.Button)
////      )
////    ) {
////      Text(text)
////    }
////    Column(
////      modifier = Modifier
////          .align(Alignment.BottomEnd)
////          .padding(end = 12.dp)
////    ) {
////      if (forgetPasswordFeature) {
////        Text(
////          "Forget Password ?",
////          style = MaterialTheme.typography.bodySmall,
////          textAlign = TextAlign.Right,
////          color = colorResource(id = R.color.Button),
////          fontWeight = FontWeight.Bold,
////          fontSize = 14.sp
////        )
////      }
////    }
////  }
//
//  if (loginPage) {
//    when (loadingState) {
//      FetchStatus.Loading -> {
//        // Show circular progress bar
//        CircularProgressIndicator(
//          modifier = Modifier
//            .size(24.dp),
//          color = MaterialTheme.colorScheme.primary
//        )
//      }
//
//      is FetchStatus.Success -> {
//        LaunchedEffect(Unit) {
//          navController.navigate(Screen.Home.route) {
//            popUpTo(Screen.Home.route) { inclusive = true }
//          }
//        }
//      }
//
//      else -> Log.d("Error", "Error")
//    }
//  } else {
//    when (loadingState) {
//      FetchStatus.Loading -> {
//        // Show circular progress bar
//        CircularProgressIndicator(
//          modifier = Modifier
//            .size(24.dp),
//          color = MaterialTheme.colorScheme.primary
//        )
//      }
//
//      is FetchStatus.Error -> {
//        Toast.makeText(context, loadingState.Exception.message, Toast.LENGTH_LONG).show()
//      }
//
//      is FetchStatus.Success -> {
//        LaunchedEffect(Unit) {
//          navController.navigate(Screen.Home.route) {
//            popUpTo(Screen.Home.route) { inclusive = true }
//          }
//        }
//      }
//
//      else -> {}
//    }
//  }
//}

@Composable
fun TextLoginWith(
  text: String = "Or login with",
  changeTextColor: Boolean = false,
  colorText: String = "login with",
  plainText: String = "Or ",
  onRegisterClick: () -> Unit = {},
  tag: String = "Login",
  useDivider: Boolean = false
) {

  val annotatedText = buildAnnotatedString {
    if (changeTextColor) {
      append(plainText)
      withStyle(style = SpanStyle(color = Color.Green)) {
        pushStringAnnotation(tag, colorText)
        append(" $colorText")
        pop()

      }
    } else {
      append(text)
    }

  }



  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.fillMaxWidth()
  ) {
    Spacer(modifier = Modifier.height(8.dp))
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      if (useDivider) {
        Divider(
          modifier = Modifier
              .fillMaxWidth()
              .weight(1f),
          color = Color.Gray,
          thickness = 1.dp
        )

        Text(
          annotatedText, style = MaterialTheme.typography.bodySmall,
          textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp)
        )

        Divider(
          modifier = Modifier
              .fillMaxWidth()
              .weight(1f),
          color = Color.Gray,
          thickness = 1.dp
        )
      } else {
        Text(
          annotatedText, style = MaterialTheme.typography.bodySmall,
          textAlign = TextAlign.Center, modifier = Modifier
                .clickable(onClick = onRegisterClick)
                .padding(16.dp)
        )
      }
    }

  }
}


//@Composable
//fun GoogleComponent(
//  text: String = "Sign in with Google", loadingText: String = "Login to Account...",
////    state: SignInState,
////    onSignInClick: () -> Unit
//) {
//  val context = LocalContext.current
////    LaunchedEffect(key1 = state.signInError) {
////        state.signInError?.let {error ->
////            Toast.makeText(
////                context,
////                error,
////                Toast.LENGTH_LONG
////            ).show()
////        }
////    }
//
//
//  var clicked by remember {
//    mutableStateOf(false)
//  }
//  Surface(
//    modifier = Modifier
//      .padding(start = 40.dp, end = 12.dp),
//    shape = RoundedCornerShape(8.dp),
//    border = BorderStroke(width = 1.dp, color = Color.LightGray),
//    color = MaterialTheme.colorScheme.surface,
//    onClick = { clicked = !clicked },
//
//
//    ) {
//    Row(
//      verticalAlignment = Alignment.CenterVertically,
//      horizontalArrangement = Arrangement.Center,
//      modifier = Modifier
//          .padding(
//              start = 12.dp, end = 16.dp,
//              top = 12.dp, bottom = 12.dp
//          )
//          .width(width = 280.dp)
//          .animateContentSize(
//              animationSpec = tween(
//                  durationMillis = 300,
//                  easing = LinearOutSlowInEasing
//              )
//          ),
//    ) {
//      Icon(
//        painter = painterResource(id = R.drawable.google_ic),
//        contentDescription = null,
//        Modifier.size(24.dp, 24.dp),
//        tint = Color.Unspecified
//      )
//      Spacer(modifier = Modifier.width(8.dp))
//      Text(if (clicked) loadingText else text, style = MaterialTheme.typography.labelLarge)
//      if (clicked) {
//        Spacer(modifier = Modifier.width(16.dp))
//        CircularProgressIndicator(
//          modifier = Modifier
//              .height(16.dp)
//              .width(16.dp),
//          strokeWidth = 2.dp,
//          color = MaterialTheme.colorScheme.primary
//        )
//      }
//    }
//  }
//}

@Composable
fun CircularLoading() {
  Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(50.dp), horizontalArrangement = Arrangement.Center
  ) {
    AnimationLoading()
  }
}


@Composable
fun AnimationLoading(
  modifier: Modifier = Modifier,
  cicrleSize: Dp = 25.dp,
  circleColor: Color = MaterialTheme.colorScheme.primary,
  spaceBetween: Dp = 10.dp,
  travelDistance: Dp = 20.dp,
) {
  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
    val circles = listOf(
      remember { androidx.compose.animation.core.Animatable(initialValue = 0f) },
      remember { androidx.compose.animation.core.Animatable(initialValue = 0f) },
      remember { androidx.compose.animation.core.Animatable(initialValue = 0f) }
    )

    circles.forEachIndexed { index, animatable ->
      LaunchedEffect(key1 = animatable) {
        delay(index * 100L)
        animatable.animateTo(
          targetValue = 1f,
          animationSpec = infiniteRepeatable(
            animation = keyframes {
              durationMillis = 1200
              0.0f at 0 with LinearOutSlowInEasing
              1.0f at 300 with LinearOutSlowInEasing
              0.0f at 600 with LinearOutSlowInEasing
              0.0f at 1200 with LinearOutSlowInEasing
            },
            repeatMode = RepeatMode.Restart
          )
        )
      }
    }

    val circleValues = circles.map { it.value }
    val distance = with(LocalDensity.current) { travelDistance.toPx() }
    val lastCircle = circleValues.size - 1

    Row(modifier = modifier) {
      circleValues.forEachIndexed { index, value ->
        Box(modifier = Modifier
            .size(cicrleSize)
            .graphicsLayer { translationY = -value * distance }
            .background(
                color = circleColor,
                shape = CircleShape
            ))
        if (index != lastCircle) {
          Spacer(modifier = Modifier.width((spaceBetween)))
        }
      }
    }
  }


}



