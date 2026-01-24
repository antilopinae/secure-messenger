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
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import com.securemessenger.ui.event.*
import com.securemessenger.ui.navigation.*
import com.securemessenger.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    action: (MainScreenAction) -> Unit,
    event: MainScreenEvent,
    onOpenDrawer: () -> Unit
) {
    Scaffold(
        containerColor = Color.Black,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "SECURE CHATS",
                        style = MaterialTheme.typography.labelLarge.copy(
                            letterSpacing = 3.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open Menu",
                            tint = AccentCyan
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black.copy(alpha = 0.9f)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = AccentCyan,
                contentColor = Color.Black,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "New Chat")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
        ) {
            SystemStatusHeader(nodesActive = event.userList?.size ?: 0)

            val users = event.userList ?: emptyList()

            if (users.isEmpty()) {
                EmptyChatsPlaceholder()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(users) { user ->
                        ChatItem(
                            userName = user,
                            lastMessage = "Encrypted packet...",
                            time = "12:45",
                            onClick = {
                                action(MainScreenAction.SelectUser(user))
                                navController.navigate(Screen.Chat.createRoute(user))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SystemStatusHeader(nodesActive: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .border(1.dp, Color.White.copy(alpha = 0.1f), MaterialTheme.shapes.small)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(AccentCyan, CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "SYSTEM READY // NODES ONLINE: $nodesActive",
            style = MaterialTheme.typography.labelSmall,
            color = AccentCyan.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun ChatItem(
    userName: String,
    lastMessage: String,
    time: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .border(1.dp, AccentCyan.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.take(1).uppercase(),
                    color = AccentCyan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = userName,
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = time,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = lastMessage,
                    color = Color.Gray,
                    fontSize = 14.sp,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun EmptyChatsPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("No active nodes found", color = Color.Gray)
            Text(
                "Swipe right or use the menu to start",
                color = Color.Gray.copy(alpha = 0.5f),
                fontSize = 12.sp
            )
        }
    }
}