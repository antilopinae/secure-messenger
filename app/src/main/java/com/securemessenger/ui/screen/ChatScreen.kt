package com.securemessenger.ui.screen

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.securemessenger.data.model.MessageModel
import com.securemessenger.ui.event.MainScreenAction
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ChatScreen(
    navController: NavHostController,
    action: (MainScreenAction) -> Unit
) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current

    val messageText = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .background(color = Color.Black)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "",
                        tint = White,
                        modifier = Modifier.clickable {
                            backDispatcher?.onBackPressedDispatcher?.onBackPressed()
                        }
                    )
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Gray
            )
        }

        LazyColumn(
            reverseLayout = true,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Gray
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText.value,
                    onValueChange = {
                        messageText.value = it
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = White,
                        focusedBorderColor = White,
                        focusedTextColor = White
                    ),
                    modifier = Modifier
                        .height(80.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(25.dp),
                    label = {
                        Text(text = "Type message", color = Color.Gray)
                    }
                )

                Button(
                    onClick = {
                        action(MainScreenAction.SendMessage(messageText.value) {
                            if (it) {
                                messageText.value = ""
                            }
                        })
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = White,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .size(50.dp),
                    contentPadding = PaddingValues(10.dp),
                    shape = RoundedCornerShape(15.dp)
                ) {
                }
            }
        }
    }
}

@Composable
fun SendChatItem(item: MessageModel) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 15.dp)
    ) {
        constraints

    }
}

@Composable
fun ReceiveChatItem(item: MessageModel) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 15.dp)
    ) {
        constraints

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatScreenPreview() {
//    ChatScreen()
}