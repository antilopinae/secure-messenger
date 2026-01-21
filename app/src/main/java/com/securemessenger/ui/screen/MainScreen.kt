package com.securemessenger.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.securemessenger.R
import com.securemessenger.data.model.UserModel
import com.securemessenger.ui.navigation.MainActivityNavigationNames
import com.securemessenger.ui.component.HeightSpacer
import com.securemessenger.ui.component.ImageCircle
import com.securemessenger.ui.component.WidthSpacer
import com.securemessenger.ui.event.MainScreenAction
import com.securemessenger.ui.event.MainScreenEvent

@Composable
fun MainScreen(
    navController: NavHostController,
    action: (MainScreenAction) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Black)
    ) {
        constraints

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                HeightSpacer()
                Text(
                    text = "Chat-U",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = White
                )

//                Image(
//                    painter = painterResource(id = R.drawable.person_icon),
//                    contentDescription = "",
//                    modifier = Modifier
//                        .size(40.dp)
//                        .background(color = White, shape = CircleShape)
//                        .padding(5.dp)
//                )
            }

            Text(
                text = "Chats",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )

            HeightSpacer()

//            mainScreenEvent.value.userList?.let { list ->
//                LazyColumn {
//                    items(list.size) { index ->
//                        val userItem = list[index]
//                        ChatItem(userItem) {
//                            action(MainScreenAction.SelectUser(userItem))
//                            navController.navigate(MainActivityNavigationNames.CHAT_SCREEN)
//                        }
//                    }
//                }
//            }
        }

        FloatingActionButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(15.dp),
            containerColor = White,
            contentColor = Color.Black
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = ""
            )
        }
    }
}

@Composable
fun ChatItem(userItem: UserModel, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
            ) {
//                ImageCircle()

                WidthSpacer(width = 20.dp)

                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .height(80.dp)
                        .padding(top = 10.dp)
                ) {
                    Text(
                        text = userItem.userName ?: "",
                        fontSize = 20.sp,
                        color = White
                    )

                    HeightSpacer(height = 10.dp)

                    Text(
                        text = "Hey, What's Up?",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .height(80.dp)
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = "4:29 pm",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
//    MainScreen(mainActivityViewModel.userList.collectAsState())
}