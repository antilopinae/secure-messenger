package com.securemessenger.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.securemessenger.data.model.Message
import com.securemessenger.data.model.MessageModel
import com.securemessenger.data.model.MessageState
import com.securemessenger.ui.component.AnimatedMessageCard
import com.securemessenger.ui.theme.AccentCyan
import com.securemessenger.ui.theme.SecureMessengerTheme
import com.securemessenger.ui.viewmodel.ChatIntent
import com.securemessenger.ui.viewmodel.ChatState
import com.securemessenger.ui.viewmodel.ChatViewModel

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