package com.agrafast.ui.screen.authetication.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agrafast.ui.theme.AgraFastTheme

enum class AuthType {
  Login,
  Register
}

@Composable
fun AuthFooter(authType: AuthType, onActionClick: () -> Unit) {
  Column(
    modifier = Modifier
      .fillMaxWidth().padding(horizontal = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      modifier = Modifier.fillMaxWidth(),
      text = "Forget Password ?",
      style = MaterialTheme.typography.labelLarge,
      textAlign = TextAlign.Right,
      color = MaterialTheme.colorScheme.primary,
      fontSize = 14.sp
    )
    Spacer(modifier = Modifier.height(16.dp))
    Row(
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      val text =
        if (authType == AuthType.Login) "Doesn't have an account?" else "Already have an account?"
      val actionText = if (authType == AuthType.Login) "Register" else "Login"
      Text(text = text, style = MaterialTheme.typography.bodyMedium)
      Text(
        modifier = Modifier.clickable(
          indication = null,
          interactionSource = MutableInteractionSource()
        ) {
          onActionClick()
        },
        text = actionText,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.bodyMedium
      )
    }
  }
}

@Preview
@Composable
fun DefaultPreview() {
  AgraFastTheme {
    AuthFooter(AuthType.Login, {})
  }
}