package com.securemessenger.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.securemessenger.ui.event.MainScreenAction
import com.securemessenger.ui.event.MainScreenEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainActivityViewModel : ViewModel() {
    private val _mainScreenEvent = MutableStateFlow(
        MainScreenEvent(
        )
    )
    private val _isLoading = MutableStateFlow(false)

    val mainScreenEvent: StateFlow<MainScreenEvent> = _mainScreenEvent.asStateFlow()

    fun action(event: MainScreenAction) {
        _isLoading.value = true
        when (event) {
            is MainScreenAction.SelectUser -> {}
            is MainScreenAction.SendMessage -> {}
        }
    }
}