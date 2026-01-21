package com.securemessenger.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.securemessenger.ui.screen.ChatScreen
import com.securemessenger.ui.screen.MainScreen
import com.securemessenger.ui.screen.ProfileScreen
import com.securemessenger.ui.screen.SearchScreen
import com.securemessenger.ui.viewmodel.MainActivityViewModel

object MainActivityNavigationNames {
    const val MAIN_SCREEN = "MAIN_SCREEN"
    const val CHAT_SCREEN = "CHAT_SCREEN"
    const val PROFILE_SCREEN = "PROFILE_SCREEN"
    const val SEARCH_SCREEN = "SEARCH_SCREEN"
}

@Composable
fun MainActivityNavigation(mainActivityViewModel: MainActivityViewModel) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MainActivityNavigationNames.MAIN_SCREEN
    )
    {
        composable(MainActivityNavigationNames.MAIN_SCREEN) {
            MainScreen(
                navController,
                mainActivityViewModel::action
            )
        }

        composable(MainActivityNavigationNames.CHAT_SCREEN) {
            ChatScreen(
                navController,
                mainActivityViewModel::action
            )
        }

        composable(MainActivityNavigationNames.PROFILE_SCREEN) {
            ProfileScreen()
        }

        composable(MainActivityNavigationNames.SEARCH_SCREEN) {
            SearchScreen()
        }

    }

}