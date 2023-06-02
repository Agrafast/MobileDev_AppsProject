package com.agrafast.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.agrafast.ui.theme.AgraFastTheme

@Composable
fun SimpleActionBar(
  title: String,
  onBackClicked: () -> Unit,
  content: (@Composable () -> Unit)? = {}
) {
  Row(
    Modifier.height(64.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    IconButton(onClick = onBackClicked) {
      Icon(
        imageVector = Icons.Default.ArrowBack, contentDescription = "Back"
      )
    }
    Box(modifier = Modifier.fillMaxWidth()) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
      )
    }
    content?.invoke()
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  AgraFastTheme {
    SimpleActionBar("Tanaman", {}) {

    }
  }
}