package com.securemessenger.ui.event

import com.securemessenger.data.model.MessageModel

data class MainScreenEvent(
    val currentUser: String? = null,
    val selectedUser: String? = null,
    val userList: MutableList<String>? = null,
    val currentChatId: String? = null,
    val messagesList: List<MessageModel>? = null,
)