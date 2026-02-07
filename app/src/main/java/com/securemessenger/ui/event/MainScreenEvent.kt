package com.securemessenger.ui.event

import com.securemessenger.ui.viewmodel.PreviewChatModel

data class MainScreenEvent(
    val chats: List<PreviewChatModel>
)