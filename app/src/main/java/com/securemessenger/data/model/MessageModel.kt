package com.securemessenger.data.model

data class MessageModel(
    val messageId : String? = null,
    val senderId : String? = null,
    val text: String? = null,
    val timeStamp: Long? = null
)
