package com.securemessenger.data.db

import androidx.room.*

@Entity(
    tableName = "chat_participants",
    primaryKeys = ["chatId", "nodeId"],
    foreignKeys = [
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["chatId"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ParticipantEntity::class,
            parentColumns = ["nodeId"],
            childColumns = ["nodeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("nodeId")]
)
data class ChatParticipantCrossRef(
    val chatId: String,
    val nodeId: String
)
