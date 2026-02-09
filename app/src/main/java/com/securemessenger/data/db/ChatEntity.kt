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

    @ColumnInfo(name = "chatName")
    val chatName: String,

    @ColumnInfo(name = "lastMessage")
    val lastMessage: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long
)
