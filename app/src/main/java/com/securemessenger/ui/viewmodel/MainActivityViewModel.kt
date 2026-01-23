package com.securemessenger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.securemessenger.ui.event.MainScreenAction
import com.securemessenger.ui.event.MainScreenEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {
    //    private val _snackBarState = MutableStateFlow(SnackBarState())
    private val _mainScreenEvent = MutableStateFlow(
        MainScreenEvent(
        )
    )
    private val _isLoading = MutableStateFlow(false)

    //    val snackBarState: StateFlow<SnackBarState> = _snackBarState.asStateFlow()
    val chatViewModel: ChatViewModel = ChatViewModel()

    val mainScreenEvent: StateFlow<MainScreenEvent> = _mainScreenEvent.asStateFlow()

    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadUserList()
    }

    fun action(event: MainScreenAction) {
        _isLoading.value = true
        when (event) {
            is MainScreenAction.SelectUser -> initChat(event)
            is MainScreenAction.SendMessage -> chatViewModel.sendMessage(event.message) {
                _isLoading.value = false
                event.callBack(it)
            }
        }
    }

    private fun loadUserList() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
        }
    }

    private fun initChat(event: MainScreenAction.SelectUser) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
            }
        } catch (e: Exception) {
            _isLoading.value = false
        }
    }
}