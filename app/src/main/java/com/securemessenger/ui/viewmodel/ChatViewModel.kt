package com.securemessenger.ui.viewmodel

import androidx.lifecycle.*
import com.securemessenger.data.db.*
import com.securemessenger.data.model.*
import com.securemessenger.data.repository.ChatRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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
    private val chatRepository: ChatRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val chatId: String = checkNotNull(savedStateHandle["chatId"])
    private val _inputText = MutableStateFlow("")

    val state: StateFlow<ChatState> = combine(
        chatRepository.getMessages(chatId), _inputText
    ) { messages, input ->
        ChatState(messages = messages.map { it.toModel() }, inputText = input)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ChatState(isLoading = true))

    fun handleIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.SendMessage -> {
                val text = _inputText.value
                if (text.isBlank()) return
                viewModelScope.launch {
                    chatRepository.sendMessage(chatId, text)
                    _inputText.value = ""
                }
            }

            is ChatIntent.TypeMessage -> _inputText.value = intent.text
            is ChatIntent.RevealMessage -> {
                // Вызов логики восстановления через Go
            }
        }
    }
}