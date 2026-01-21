package com.securemessenger.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun ThemeSolidButton(
    text: String,
    modifier: Modifier = Modifier,
    contentColor: Color = Color.Black,
    containerColor: Color = White,
    borderColor: Color = White,
    borderRadius: Dp = 30.dp,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(borderRadius),
        colors = ButtonDefaults.buttonColors(
            contentColor = contentColor,
            containerColor = containerColor,
            disabledContentColor = Color.Gray,
            disabledContainerColor = Color.Gray
        ),
        border = BorderStroke(width = 1.dp, color = borderColor)
    ) {
        Text(text = text)
    }
}