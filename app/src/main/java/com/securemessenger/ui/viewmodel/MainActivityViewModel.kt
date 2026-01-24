package com.securemessenger.ui.viewmodel

import androidx.lifecycle.*
import com.securemessenger.data.db.*
import com.securemessenger.ui.event.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.flow.*
import javax.inject.*

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dao: ChatDao
) : ViewModel() {
    val mainScreenEvent: StateFlow<MainScreenEvent> = dao.getAllChats()
        .map { chats ->
            MainScreenEvent(
                userList = chats.map { it.chatName }.toMutableList()
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainScreenEvent(userList = mutableListOf())
        )

    fun action(event: MainScreenAction) {
        when (event) {
            is MainScreenAction.SelectUser -> {}
            is MainScreenAction.SendMessage -> {}
        }
    }
}