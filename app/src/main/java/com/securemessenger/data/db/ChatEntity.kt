package com.securemessenger.data.db

import androidx.room.*

@Entity(
    tableName = "chats",
    indices = [Index("timestamp")]
)
data class ChatEntity(
    @PrimaryKey
    @ColumnInfo(name = "chatId")
    val chatId: String,

    val chatName: String,
    val lastMessage: String,
    val timestamp: Long
)
