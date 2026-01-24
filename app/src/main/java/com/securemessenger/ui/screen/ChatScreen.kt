package com.securemessenger.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.*
import com.securemessenger.data.model.*
import com.securemessenger.ui.component.*
import com.securemessenger.ui.theme.*
import com.securemessenger.ui.viewmodel.*

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    ChatContent(
        state = state,
        onBack = onBack,
        onIntent = viewModel::handleIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatContent(
    state: ChatState,
    onBack: () -> Unit,
    onIntent: (ChatIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { Text("SECURE NODE", color = Color.White, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                reverseLayout = true,
                contentPadding = PaddingValues(16.dp)
            ) {
                items(state.messages) { message ->
                    AnimatedMessageCard(model = message)
                }
            }

            ChatInput(
                text = state.inputText,
                onTextChange = { onIntent(ChatIntent.TypeMessage(it)) },
                onSend = { onIntent(ChatIntent.SendMessage) }
            )
        }
    }
}

@Composable
fun ChatInput(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Print message...", color = Color.Gray) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = AccentCyan,
                unfocusedBorderColor = Color.Gray
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = onSend,
            modifier = Modifier.background(AccentCyan, CircleShape)
        ) {
            Icon(Icons.Default.Send, contentDescription = null, tint = Color.Black)
        }
    }
}

@Preview(showBackground = true, name = "Chat Screen Dark Mode")
@Composable
fun ChatScreenPreview() {
    SecureMessengerTheme(darkTheme = true) {
        val mockMessages = listOf(
            MessageModel(
                transportInfo = Message("1", "user1", 1737630000, mutableListOf()),
                senderName = "Alice",
                state = MessageState.Visible("Hello! Is this channel secure?")
            ),
            MessageModel(
                transportInfo = Message("2", "me", 1737630500, mutableListOf()),
                senderName = "Me",
                state = MessageState.Visible("Yes, end-to-end encryption is active.")
            ),
            MessageModel(
                transportInfo = Message("3", "user1", 1737630900, mutableListOf()),
                senderName = "Alice",
                state = MessageState.Hidden
            )
        )

        val previewState = ChatState(
            messages = mockMessages.reversed(),
            inputText = "Ready to send..."
        )

        ChatContent(
            state = previewState,
            onBack = {},
            onIntent = {}
        )
    }
}