package com.securemessenger.ui.event

sealed interface MainScreenAction {
    data class SelectUser(val userModel: String) : MainScreenAction
    data class SendMessage(val message: String, val callBack: (status: Boolean) -> Unit) :
        MainScreenAction
}