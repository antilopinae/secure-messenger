package com.securemessenger.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.*
import androidx.navigation.compose.*
import com.securemessenger.ui.component.*
import com.securemessenger.ui.screen.*
import com.securemessenger.ui.viewmodel.*

@Composable
fun AppNavigation(viewModel: MainActivityViewModel) {
    val navController = rememberNavController()
    val mainScreenEvent by viewModel.mainScreenEvent.collectAsState()

    CustomDrawerWrapper(
        drawerContent = {
            DrawerMenuContent(
                onItemClick = { screen ->
                    navController.navigate(screen.route) {
                        popUpTo(Screen.ChatList.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        mainContent = { onMenuClick ->
            NavHost(
                navController = navController,
                startDestination = Screen.ChatList.route
            ) {
                composable(Screen.ChatList.route) {
                    MainScreen(
                        navController = navController,
                        action = viewModel::action,
                        event = mainScreenEvent,
                        onOpenDrawer = onMenuClick
                    )
                }

                composable(
                    route = Screen.Chat.route,
                    arguments = listOf(navArgument("chatId") { type = NavType.StringType })
                ) {
                    ChatScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    )
}