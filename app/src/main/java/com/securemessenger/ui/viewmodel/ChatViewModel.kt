package com.securemessenger.ui.viewmodel

import androidx.lifecycle.*
import com.securemessenger.data.db.*
import com.securemessenger.data.model.*
import kotlinx.coroutines.flow.*

data class ChatState(
    val messages: List<MessageModel> = emptyList(),
    val isLoading: Boolean = false,
    val inputText: String = ""
)

sealed interface ChatIntent {
    data class TypeMessage(val text: String) : ChatIntent
    object SendMessage : ChatIntent
    data class RevealMessage(val id: String) : ChatIntent
}

class ChatViewModel(
    private val chatDao: ChatDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val chatId: String = checkNotNull(savedStateHandle["chatId"])

    private val _inputText = MutableStateFlow("")

    val state: StateFlow<ChatState> =
        combine(
            chatDao.getMessagesForChat(chatId),
            _inputText
        ) { entities, input ->
            ChatState(
                messages = entities.map { it.toModel() },
                inputText = input
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ChatState(isLoading = true)
        )

    fun handleIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.TypeMessage -> {
                _inputText.value = intent.text
            }

            is ChatIntent.SendMessage -> {}
            is ChatIntent.RevealMessage -> {}
        }
    }
}