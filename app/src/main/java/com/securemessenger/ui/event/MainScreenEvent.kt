package com.securemessenger.ui.event

import com.securemessenger.data.model.MessageModel
import com.securemessenger.data.model.UserModel

data class MainScreenEvent(
    val currentUser: UserModel? = null,
    val selectedUser: UserModel? = null,
    val userList: MutableList<UserModel>? = null,
    val currentChatId: String? = null,
    val messagesList: List<MessageModel>? = null,
)