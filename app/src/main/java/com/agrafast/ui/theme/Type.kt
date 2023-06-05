package com.agrafast.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.agrafast.R

// Set of Material typography styles to start with

//val Poppins = FontFamily(
//  Font(R.font.poppins_regular),
//  Font(R.font.poppins_medium, FontWeight.Medium),
//  Font(R.font.poppins_semibold, FontWeight.SemiBold),
//)

val Inter = FontFamily(
  Font(R.font.inter_regular),
  Font(R.font.inter_medium, FontWeight.Medium),
  Font(R.font.inter_semibold, FontWeight.SemiBold),
)
val Nunito = FontFamily(
  Font(R.font.nunito_regular),
  Font(R.font.nunito_semibold, FontWeight.SemiBold),
  Font(R.font.nunito_bold, FontWeight.Bold),
)

val Typography = Typography(
  displaySmall = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.SemiBold,
    fontSize = 36.sp,
    lineHeight = 44.sp,
    letterSpacing = 0.sp
  ),

  titleLarge = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.SemiBold,
    fontSize = 20.sp,
    letterSpacing = 0.sp
  ),
  titleMedium = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.SemiBold,
    fontSize = 18.sp,
//    lineHeight = 24.sp,
    letterSpacing = 0.15.sp
  ),
  titleSmall = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    lineHeight = 22.sp,
    letterSpacing = 0.1.sp
  ),
  bodyLarge = TextStyle(
    fontFamily = Nunito,
    fontWeight = FontWeight.SemiBold,
    fontSize = 18.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp
  ),
  bodyMedium = TextStyle(
    fontFamily = Nunito,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    lineHeight = 22.sp,
    letterSpacing = 0.25.sp
  ),
  bodySmall = TextStyle(
    fontFamily = Nunito,
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.25.sp
  ),
  labelLarge = TextStyle(
    fontFamily = Nunito,
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp,
//    lineHeight = 22.sp,
    letterSpacing = 0.25.sp
  ),
)