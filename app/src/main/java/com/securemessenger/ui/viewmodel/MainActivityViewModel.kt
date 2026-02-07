package com.securemessenger.ui.viewmodel

import androidx.lifecycle.*
import com.securemessenger.data.db.*
import com.securemessenger.ui.event.*
import kotlinx.coroutines.flow.*

class MainActivityViewModel(
    private val dao: ChatDao
) : ViewModel() {
    val mainScreenEvent: StateFlow<MainScreenEvent> =
        dao.getAllChats()
            .map { chats ->
                MainScreenEvent(
                    chats = chats.map {
                        PreviewChatModel(id = it.chatId, title = it.chatName)
                    }
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = MainScreenEvent(emptyList())
            )
}