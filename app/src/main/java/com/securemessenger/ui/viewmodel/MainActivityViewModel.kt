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
                    userList = chats.map { it.chatName }.toMutableList()
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = MainScreenEvent(mutableListOf())
            )

    fun action(event: MainScreenAction) {
        when (event) {
            is MainScreenAction.SelectUser -> {}
            is MainScreenAction.SendMessage -> {}
        }
    }
}