package com.securemessenger.ui.viewmodel

import androidx.lifecycle.*
import com.securemessenger.data.db.*
import com.securemessenger.data.model.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.flow.*
import javax.inject.*

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

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatDao: ChatDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val chatId: String = checkNotNull(savedStateHandle["chatId"])

    private val _inputText = MutableStateFlow("")

    val state: StateFlow<ChatState> = chatDao.getMessagesForChat(chatId)
        .map { entities ->
            ChatState(
                messages = entities.map { it.toModel() },
                inputText = _inputText.value
            )
        }
        .stateIn(
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