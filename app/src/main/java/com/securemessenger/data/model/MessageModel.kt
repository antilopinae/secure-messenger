package com.securemessenger.data.model

data class MessageModel(
    val id: String,
    val senderId: String,
    val senderName: String,
    val timestamp: Long,
    val state: MessageState
)

sealed class MessageState {
    object Hidden : MessageState()
    object Loading : MessageState()
    data class Visible(val text: String) : MessageState()
    data class Error(val msg: String) : MessageState()
}
