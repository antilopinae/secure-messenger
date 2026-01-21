package com.securemessenger.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.securemessenger.R

@Composable
fun ImageCircle(size: Dp = 80.dp, image: Int) {
    Box(
        modifier = Modifier
            .size(size)
            .border(width = 1.dp, color = Color.Gray, shape = CircleShape)
            .clip(shape = CircleShape)
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}