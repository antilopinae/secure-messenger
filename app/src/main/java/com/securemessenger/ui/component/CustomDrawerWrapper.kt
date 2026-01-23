package com.securemessenger.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun CustomDrawerWrapper(
    drawerContent: @Composable () -> Unit,
    mainContent: @Composable (onMenuClick: () -> Unit) -> Unit
) {
    var isMenuOpen by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val progress by animateFloatAsState(
        targetValue = if (isMenuOpen) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "drawerAnimation"
    )

    val scale = 1f - (progress * 0.2f)
    val translateX = (screenWidth * 0.7f) * progress
    val cornerRadius = (24 * progress).dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = progress
                    scaleX = 0.9f + (progress * 0.1f)
                    scaleY = 0.9f + (progress * 0.1f)
                }
        ) {
            drawerContent()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationX = translateX.toPx()
                    scaleX = scale
                    scaleY = scale
                }
                .clip(RoundedCornerShape(cornerRadius))
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        change.consume()
                        if (dragAmount > 20 && !isMenuOpen) isMenuOpen = true
                        if (dragAmount < -20 && isMenuOpen) isMenuOpen = false
                    }
                }
        ) {
            mainContent { isMenuOpen = !isMenuOpen }

            if (isMenuOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                        .clickable { isMenuOpen = false }
                )
            }
        }
    }
}