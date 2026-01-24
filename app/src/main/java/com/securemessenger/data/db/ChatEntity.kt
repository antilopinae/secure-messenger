package com.securemessenger.data.db

import androidx.room.*

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val chatId: String,
    val chatName: String,
    val lastMessage: String,
    val timestamp: Long
)
