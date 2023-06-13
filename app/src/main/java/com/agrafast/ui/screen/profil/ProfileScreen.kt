package com.agrafast.ui.screen.profil


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.agrafast.AppState
import com.agrafast.R
import com.agrafast.data.firebase.model.User
import com.agrafast.domain.AuthState
import com.agrafast.rememberAppState
import com.agrafast.ui.component.SimpleTopBar
import com.agrafast.ui.navigation.Screen
import com.agrafast.ui.screen.AuthViewModel
import com.agrafast.ui.theme.AgraFastTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(
  appState: AppState,
  authViewModel: AuthViewModel
) {
  // State
  val userState = authViewModel.userState.collectAsState()
  val user = remember { (userState.value as AuthState.Authenticated<User>).data!! }
  Surface {
    LazyColumn(
      Modifier
        .fillMaxHeight()
        .fillMaxWidth(),
    ) {
     stickyHeader {  SimpleTopBar(stringResource(id = R.string.profile)) }
      item {
        ProfilHeader(modifier = Modifier.padding(top = 24.dp, bottom = 32.dp), user = user)
      }
      item {
        Divider()
      }
      item {
        ProfilDetail(user, appState, authViewModel)
      }
//      Spacer(modifier = Modifier.padding(top = 24.dp))
//      ProfileUpdateFunctionallity()
//      ProfileUpdateFunctionallity(
//        Icons.Outlined.Build,
//        "Update Password"
//      )
//      ProfileUpdateFunctionallity(
//        Icons.Outlined.ExitToApp,
//        "Logout"
//      )
    }
  }
}

@Composable
fun ProfilHeader(modifier: Modifier = Modifier, user: User) {
  Column(
    modifier = modifier
      .fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Surface(
      shape = CircleShape,
      modifier = Modifier.size(130.dp),
      tonalElevation = 4.dp,
    ) {
      // Check if photoUrlExist
      if (true) {
        Icon(
          Icons.Outlined.Person,
          contentDescription = "Profile",
          modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
        )
      } else {
        AsyncImage(
          model = painterResource(id = R.drawable.auth_header),
          contentDescription = "Profile",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
        )
      }
    }
    Spacer(Modifier.height(16.dp))
    Text(
      text = user.name,
      style = MaterialTheme.typography.titleLarge,
      maxLines = 1,
    )
  }
}

@Composable
fun ProfilDetail(user: User, appState: AppState, authViewModel: AuthViewModel) {
  Column(
    Modifier.padding(top = 16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    ProfilDetailItem(
      painter = painterResource(id = R.drawable.ic_email),
      title = "Email",
      subtitle = user.email
    )
    ProfilDetailItem(
      painter = painterResource(id = R.drawable.ic_phone),
      title = "Telepon",
      subtitle = user.phone
    )
//    ProfilDetailItem(
//      painter = painterResource(id = R.drawable.ic_map),
//      title = "Alamat",
//      subtitle = user.phone
//    )
    ProfilDetailItem(
      painter = painterResource(id = R.drawable.ic_setting),
      title = "Ubah email",
      onClick = {
        appState.navController.navigate(Screen.UpdateProfile.to("email"))
      }
    )
    ProfilDetailItem(
      painter = painterResource(id = R.drawable.ic_setting),
      title = "Ubah kata sandi",
      onClick = {
        appState.navController.navigate(Screen.UpdateProfile.to("password"))
      }
    )
    ProfilDetailItem(
      painter = painterResource(id = R.drawable.ic_setting),
      title = "Perbarui profil",
      onClick = {
        appState.navController.navigate(Screen.UpdateProfile.to("profil"))
      }
    )
    ProfilDetailItem(
      painter = painterResource(id = R.drawable.ic_logout),
      title = "Keluar",
      onClick = {
        authViewModel.signOut()
        appState.navController.navigate(Screen.Login.route) {
          popUpTo(Screen.Home.route) {
            inclusive = true
          }
        }
      }
    )

  }
}
//
//@Composable
//fun ProfileUpdateFunctionallity(
//  Icon: ImageVector = Icons.Outlined.Person,
//  text: String = "Update Profile"
//) {
//  Spacer(modifier = Modifier.height(8.dp))
//  Row() {
//    Icon(
//      imageVector = Icon,
//      contentDescription = null
//    )
//    Spacer(modifier = Modifier.padding(end = 4.dp))
//    Text(
//      text = text,
//      style = MaterialTheme.typography.titleMedium,
//      color = MaterialTheme.colorScheme.primary,
//      maxLines = 1,
//    )
//  }
//}

@Composable
fun ProfilDetailItem(
  modifier: Modifier = Modifier,
  painter: Painter, title: String,
  subtitle: String? = null,
  onClick: (() -> Unit)? = null
) {
  var mod = modifier
  if (onClick != null) {
    mod = mod
      .clip(RoundedCornerShape(8.dp))
      .clickable {
        onClick.invoke()
      }
      .padding(vertical = 8.dp)
  }
  Row(
    modifier = mod
      .padding(horizontal = 16.dp)
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Icon(painter = painter, contentDescription = null)
    Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        maxLines = 1,
      )
      if (subtitle != null) {
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodyMedium,
          maxLines = 1,
        )
      }
    }
  }
}

@Preview
@Composable
fun DefaultPreview() {
  AgraFastTheme() {
    ProfileScreen(appState = rememberAppState(), hiltViewModel())
  }
}
