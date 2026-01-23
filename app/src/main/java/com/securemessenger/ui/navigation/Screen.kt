package com.securemessenger.ui.navigation

sealed class Screen(val route: String) {
    object ChatList : Screen("chat_list")
    object Chat : Screen("chat/{chatId}") {
        fun createRoute(chatId: String) = "chat/$chatId"
    }
    object Settings : Screen("settings")
    object CreateChat : Screen("create_chat")
    object Profile : Screen("profile")
}