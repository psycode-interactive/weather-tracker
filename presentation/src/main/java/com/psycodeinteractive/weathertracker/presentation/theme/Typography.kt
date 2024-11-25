package com.psycodeinteractive.weathertracker.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.sp
import com.psycodeinteractive.weathertracker.presentation.R

private val normal = Font(R.font.poppins_regular, Normal)
private val medium = Font(R.font.poppins_medium, Medium)
private val semibold = Font(R.font.poppins_semibold, SemiBold)
private val bold = Font(R.font.poppins_bold, Bold)

private val appFontFamily = FontFamily(normal, medium, semibold, bold)
private val commonTextStyle = TextStyle(fontFamily = appFontFamily)

val themeTypography = Typography(
    displayMedium = commonTextStyle.copy(
        fontSize = 70.sp,
        lineHeight = 105.sp,
        fontWeight = Medium
    ),
    displaySmall = commonTextStyle.copy(
        fontSize = 60.sp,
        lineHeight = 90.sp,
        fontWeight = Medium
    ),
    headlineLarge = commonTextStyle.copy(
        fontSize = 30.sp,
        lineHeight = 45.sp,
        fontWeight = SemiBold
    ),
    headlineMedium = commonTextStyle.copy(
        fontSize = 20.sp,
        lineHeight = 30.sp,
        fontWeight = SemiBold
    ),
    headlineSmall = commonTextStyle.copy(
        fontSize = 15.sp,
        lineHeight = 22.5.sp,
        fontWeight = SemiBold
    ),
    titleLarge = commonTextStyle.copy(
        fontSize = 15.sp,
        lineHeight = 22.5.sp,
        fontWeight = Medium
    ),
    titleMedium = commonTextStyle.copy(
        fontSize = 12.sp,
        lineHeight = 18.sp,
        fontWeight = Medium
    ),
    labelLarge = commonTextStyle.copy(
        fontSize = 15.sp,
        lineHeight = 22.5.sp,
        fontWeight = Normal
    ),
    labelMedium = commonTextStyle.copy(
        fontSize = 8.sp,
        lineHeight = 12.sp,
        fontWeight = Medium
    ),
)
