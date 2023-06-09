package com.agrafast.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.agrafast.R

@Composable
fun InputText(
  value: String,
  label: String,
  @DrawableRes leadingRes: Int,
  onValueChange: (String) -> Unit,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
  val isPasswordInput = keyboardOptions.keyboardType == KeyboardType.Password
  var visible by remember { mutableStateOf(!isPasswordInput) }

  OutlinedTextField(
    value = value,
    onValueChange = {
      onValueChange(it)
    },
    placeholder = { Text(text = label) },
    modifier = Modifier
      .fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    singleLine = true,
    label = { Text(text = label) },
    keyboardOptions = keyboardOptions,
    leadingIcon = {
      Icon(
        painter = painterResource(leadingRes),
        contentDescription = ""
      )
    },
    trailingIcon = {
      if(isPasswordInput){
      val resId = if (visible) R.drawable.ic_visibility_on else R.drawable.ic_visibility_off
        Icon(
          modifier = Modifier.size(24.dp).clickable(
            indication = null,
            interactionSource = MutableInteractionSource()
          ) { visible = !visible },
          painter = painterResource(id = resId),
          contentDescription = null
        )
      }
    },
    visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation()
  )
}