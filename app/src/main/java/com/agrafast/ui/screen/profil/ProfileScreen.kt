package com.agrafast.ui.screen.profil


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.agrafast.AppState
import com.agrafast.R
import com.agrafast.data.firebase.model.User
import com.agrafast.rememberAppState
import com.agrafast.ui.component.SimpleTopBar
import com.agrafast.ui.theme.AgraFastTheme

@Composable
fun ProfileScreen(
    appState: AppState,
) {
    Surface {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            SimpleTopBar(stringResource(id = R.string.my_plant))
            ProfilHeader(modifier = Modifier.padding(top = 24.dp, bottom = 32.dp),user = appState.user)
            Divider()
            ProfilDetail(appState.user)
            Spacer(modifier = Modifier.padding(top = 24.dp))
            ProfileUpdateFunctionallity()
            ProfileUpdateFunctionallity(Icons.Outlined.Build,
                "Update Password")
            ProfileUpdateFunctionallity(Icons.Outlined.ExitToApp,
                "Logout")
        }
    }
}

@Composable
fun ProfilHeader(modifier: Modifier = Modifier, user: User) {
//    user: User
    Column(
        modifier = modifier.fillMaxWidth(),
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
                    model = painterResource(id = R.drawable.maize_banner),
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
fun ProfilDetail(user: User){
    Column(Modifier.padding(top = 16.dp)) {
      //Email Detail
        Text(
            text = "Email",
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = user.email,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
        )

        Spacer(modifier = Modifier.height(8.dp))

        //Telp Detail

        Text(
            text = "No.Telp",
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "user.telpNumber",
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
        )

        Spacer(modifier = Modifier.height(8.dp))

        
         //Alamat Detail
        Text(
            text = "Alamat",
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "user.alamat",
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
        )

    }
}

@Composable
fun ProfileUpdateFunctionallity(Icon: ImageVector = Icons.Outlined.Person,
text: String = "Update Profile") {
    Spacer(modifier = Modifier.height(8.dp))
        Row() {
            Icon(imageVector = Icon,
            contentDescription = null)
            Spacer(modifier = Modifier.padding(end = 4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
            )
        }

}


@Preview
@Composable
fun DefaultPreview() {
    AgraFastTheme() {
        ProfileScreen(appState = rememberAppState())
    }
}
