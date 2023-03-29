package com.sam.kladishoes.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sam.kladishoes.R

val yusei = FontFamily(
    listOf(
        Font(R.font.roboto)
    )
)
// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = yusei,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    h6 = TextStyle(
        fontFamily = yusei,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)