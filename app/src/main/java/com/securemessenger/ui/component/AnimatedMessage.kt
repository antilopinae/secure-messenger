package com.securemessenger.ui.component

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.securemessenger.data.model.Message
import com.securemessenger.data.model.MessageModel
import com.securemessenger.data.model.MessageState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.*
import java.time.format.DateTimeFormatter

object MessageCardTokens {
    object Dimens {
        val outerPadding = 16.dp
        val contentPadding = 12.dp
        val footerTopMargin = 12.dp
        val iconLabelGap = 8.dp

        val cardCornerRadius = 20.dp
        val curtainCornerRadius = 12.dp

        val minHeight = 80.dp
        val iconSize = 18.dp
        val borderWidth = 1.dp
        val elevation = 2.dp
    }

    object Anim {
        const val shakeStrength = 12f
        const val rotationDuration = 1200
        const val loadingStepDelay = 800L

        val springSpec = spring<Float>(
            stiffness = Spring.StiffnessHigh
        )

        val contentSizeSpec = spring<androidx.compose.ui.unit.IntSize>(
            dampingRatio = Spring.DampingRatioLowBouncy
        )
    }

    object Type {
        val messageLineHeight = 22.sp
        val labelLetterSpacing = 0.5.sp
        val fractionFontSize = 14.sp
        val timeFontSize = 12.sp
    }

    fun getBorderColor(isSystemInDarkTheme: Boolean) = if (isSystemInDarkTheme) {
        Color.White.copy(alpha = 0.15f)
    } else {
        Color(0xFFE2E8F0)
    }

    fun getCurtainColor(isSystemInDarkTheme: Boolean) = if (isSystemInDarkTheme) {
        Color(0xFF334155)
    } else {
        Color(0xFFF1F5F9)
    }

    fun getSecondaryTextColor(isSystemInDarkTheme: Boolean) = if (isSystemInDarkTheme) {
        Color(0xFF94A3B8)
    } else {
        Color(0xFF64748B)
    }
}

@Composable
fun AnimatedMessageCard(
    model: MessageModel,
    modifier: Modifier = Modifier
) {
    var messageState by remember { mutableStateOf(model.state) }
    var downloadProgress by remember { mutableIntStateOf(1) }

    val scope = rememberCoroutineScope()
    val shakeOffset = remember { Animatable(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(MessageCardTokens.Anim.rotationDuration, easing = LinearEasing)
        ), label = "icon_rotate"
    )

    LaunchedEffect(messageState) {
        if (messageState is MessageState.Loading) {
            downloadProgress = 1
            while (downloadProgress < 3) {
                delay(MessageCardTokens.Anim.loadingStepDelay)
                downloadProgress++
            }
            messageState = MessageState.Visible(
                text = "Modern Jetpack Compose allows you to create incredibly smooth interfaces. " +
                        "This card is built using design tokens for easy maintenance. " +
                        "All paddings, durations, and colors are moved to a separate config object."
            )
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(MessageCardTokens.Dimens.outerPadding)
            .animateContentSize(animationSpec = MessageCardTokens.Anim.contentSizeSpec),
        shape = RoundedCornerShape(MessageCardTokens.Dimens.cardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(
            MessageCardTokens.Dimens.borderWidth,
            MessageCardTokens.getBorderColor(isSystemInDarkTheme())
        ),
        elevation = CardDefaults.cardElevation(MessageCardTokens.Dimens.elevation)
    ) {
        Column(modifier = Modifier.padding(MessageCardTokens.Dimens.contentPadding)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = MessageCardTokens.Dimens.minHeight)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (messageState is MessageState.Hidden) {
                            scope.launch {
                                val spec = MessageCardTokens.Anim.springSpec
                                val strength = MessageCardTokens.Anim.shakeStrength
                                shakeOffset.animateTo(strength, spec)
                                shakeOffset.animateTo(-strength, spec)
                                shakeOffset.animateTo(0f, spec)
                                messageState = MessageState.Loading
                            }
                        }
                    }
            ) {
                if (messageState !is MessageState.Visible) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .offset(x = shakeOffset.value.dp)
                            .clip(RoundedCornerShape(MessageCardTokens.Dimens.curtainCornerRadius))
                            .background(MessageCardTokens.getCurtainColor(isSystemInDarkTheme()))
                            .padding(MessageCardTokens.Dimens.contentPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (messageState is MessageState.Hidden) "Tap to reveal" else "Decrypting...",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = MessageCardTokens.Type.labelLetterSpacing
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    val textData = (messageState as? MessageState.Visible)?.text ?: ""

                    Text(
                        text = textData,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            lineHeight = MessageCardTokens.Type.messageLineHeight
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MessageCardTokens.Dimens.footerTopMargin),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$downloadProgress/5",
                        fontSize = MessageCardTokens.Type.fractionFontSize,
                        fontWeight = FontWeight.Black,
                        color = if (messageState is MessageState.Loading)
                            MaterialTheme.colorScheme.primary else MessageCardTokens.getSecondaryTextColor(
                            isSystemInDarkTheme()
                        )
                    )

                    AnimatedVisibility(
                        visible = messageState is MessageState.Loading,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(start = MessageCardTokens.Dimens.iconLabelGap)
                                .size(MessageCardTokens.Dimens.iconSize)
                                .rotate(rotation),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Text(
                    text = Instant.ofEpochSecond(model.transportInfo.timestamp)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime().format(
                            DateTimeFormatter.ofPattern("HH:mm:ss")
                        ),
                    fontSize = MessageCardTokens.Type.timeFontSize,
                    color = MessageCardTokens.getSecondaryTextColor(isSystemInDarkTheme())
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Theme")
@Composable
fun PreviewMessageLight() {
    MaterialTheme {
        Surface(color = Color(0xFFF8FAFC), modifier = Modifier.fillMaxSize()) {
            Column {
                AnimatedMessageCard(getMockMessage())
            }
        }
    }
}

@Preview(showBackground = true, name = "Dark Theme")
@Composable
fun PreviewMessageDark() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Surface(color = Color(0xFF0F172A), modifier = Modifier.fillMaxSize()) {
            Column {
                AnimatedMessageCard(getMockMessage())
            }
        }
    }
}

private fun getMockMessage() = MessageModel(
    transportInfo = Message(
        id = "1",
        senderId = "A1",
        timestamp = 1234567,
        data = mutableListOf()
    ),
    senderName = "Secure Bot",
    state = MessageState.Hidden
)