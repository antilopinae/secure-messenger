package com.securemessenger.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.securemessenger.data.model.MessageModel
import com.securemessenger.data.model.MessageState
import com.securemessenger.ui.theme.AccentCyan
import com.securemessenger.ui.theme.SecureMessengerTheme
import com.securemessenger.ui.theme.SurfaceGray

@Composable
fun MessageItem(
    message: MessageModel,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { if (message.state is MessageState.Hidden) onClick(message.id) }
    ) {
        Text(
            text = message.senderName,
            color = AccentCyan,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        AnimatedContent(
            targetState = message.state,
            transitionSpec = { fadeIn(animationSpec = tween(500)).togetherWith(fadeOut()) }
        ) { state ->
            when (state) {
                is MessageState.Hidden -> ShutterView()
                is MessageState.Loading -> LoadingView()
                is MessageState.Visible -> DecryptedView(state.text)
                else -> {}
            }
        }
    }
}

@Composable
fun ShutterView() {
    Surface(
        color = SurfaceGray,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "0101101010011010010101110101",
                color = Color.DarkGray.copy(alpha = 0.5f),
                fontFamily = FontFamily.Monospace,
                maxLines = 1,
                modifier = Modifier

            )
            Text(
                text = "TAP TO DECRYPT",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun LoadingView() {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0.8f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(SurfaceGray, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text("COMBINING FRAGMENTS...", color = AccentCyan.copy(alpha = alpha))
    }
}

@Composable
fun DecryptedView(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MessageItemHiddenPreview() {
    SecureMessengerTheme {
        MessageItem(
            message = MessageModel(
                id = "1",
                senderId = "123",
                senderName = "Alice",
                timestamp = 123,
                state = MessageState.Hidden,
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MessageItemLoadingPreview() {
    SecureMessengerTheme {
        MessageItem(
            message = MessageModel(
                id = "1",
                senderId = "123",
                senderName = "Alice",
                timestamp = 123,
                state = MessageState.Loading,
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MessageItemVisiblePreview() {
    SecureMessengerTheme {
        MessageItem(
            message = MessageModel(
                id = "1",
                senderId = "123",
                senderName = "Alice",
                timestamp = 123,
                state = MessageState.Visible(text = "Sim sim"),
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MessageItemErrorPreview() {
    SecureMessengerTheme {
        MessageItem(
            message = MessageModel(
                id = "1",
                senderId = "123",
                senderName = "Alice",
                timestamp = 123,
                state = MessageState.Error(msg = "Error - what??"),
            ),
            onClick = {}
        )
    }
}

