package com.securemessenger.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.securemessenger.data.model.MessageModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val chatId: String = checkNotNull(savedStateHandle["chatId"])

    private val _state = MutableStateFlow(ChatState())
    val state = _state.asStateFlow()

    fun handleIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.TypeMessage -> _state.update { it.copy(inputText = intent.text) }
            is ChatIntent.SendMessage -> performSendMessage()
            is ChatIntent.RevealMessage -> {
            }
        }
    }

    private fun performSendMessage() {
        val text = state.value.inputText
        viewModelScope.launch {
        }
    }
}