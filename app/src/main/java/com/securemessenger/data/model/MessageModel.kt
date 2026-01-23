package com.securemessenger.data.model

data class Message(
    val id: String,
    val senderId: String,
    val timestamp: Long,
    val data: MutableList<Byte>
)

data class MessageModel(
    val transportInfo: Message,
    val senderName: String?,
    val state: MessageState
)

sealed class MessageState {
    object Hidden : MessageState()
    object Loading : MessageState()
    data class Visible(val text: String) : MessageState()
    data class Error(val msg: String) : MessageState()
}
