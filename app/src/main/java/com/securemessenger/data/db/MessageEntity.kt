package com.securemessenger.data.db

import androidx.room.*
import com.securemessenger.data.model.*

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["chatId"],
            childColumns = ["chatId"]
        )
    ]
)
data class MessageEntity(
    @PrimaryKey val id: String,
    val chatId: String,
    val senderId: String,
    val senderName: String,
    val text: String,
    val timestamp: Long,
    val state: String
)

fun MessageEntity.toModel(): MessageModel {
    return MessageModel(
        transportInfo = Message(
            id = this.id,
            senderId = this.senderId,
            timestamp = this.timestamp,
            data = mutableListOf()
        ),
        senderName = this.senderName,
        state = when (this.state) {
            "VISIBLE" -> MessageState.Visible(this.text)
            "HIDDEN" -> MessageState.Hidden
            else -> MessageState.Visible(this.text)
        }
    )
}
