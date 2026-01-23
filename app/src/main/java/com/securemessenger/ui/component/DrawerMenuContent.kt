package com.securemessenger.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.securemessenger.ui.navigation.Screen
import com.securemessenger.ui.theme.AccentCyan

@Composable
fun DrawerMenuContent(onItemClick: (Screen) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.7f)
            .padding(start = 24.dp, bottom = 48.dp, top = 64.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            "CORE\nSYSTEM",
            color = AccentCyan,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        val items = listOf(
            Screen.ChatList to "MESSAGES",
            Screen.Profile to "IDENTITY",
            Screen.Settings to "CONFIG"
        )

        items.forEach { (screen, label) ->
            Text(
                text = label,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(screen) }
                    .padding(vertical = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .size(40.dp)
                .border(1.dp, AccentCyan, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Lock,
                contentDescription = null,
                tint = AccentCyan,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}