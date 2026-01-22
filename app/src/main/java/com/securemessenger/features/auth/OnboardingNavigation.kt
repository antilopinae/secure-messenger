package com.securemessenger.features.auth

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object OnboardingNavigationObject {
    const val ONBOARDING_SCREEN = "ONBOARDING_SCREEN"
    const val LOGIN_SCREEN = "LOGIN_SCREEN"
    const val SIGNUP_SCREEN = "SIGNUP_SCREEN"
}

@Composable
fun OnboardingNavigation(/*onboardingViewModel: OnboardingViewModel*/) {

    val navController = rememberNavController()

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember {
        SnackbarHostState()
    }
//    val snackBarState by onboardingViewModel.snackBarState.collectAsState()

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
    ) { paddingValues ->
        val it = paddingValues
        NavHost(
            navController = navController,
            startDestination = OnboardingNavigationObject.ONBOARDING_SCREEN
        )
        {
            composable(
                route = OnboardingNavigationObject.ONBOARDING_SCREEN
            ) {
            }
            composable(
                route = OnboardingNavigationObject.LOGIN_SCREEN
            ) {
                LoginScreen(
                    navController,
//                    onboardingViewModel::action
                )
            }

            composable(
                route = OnboardingNavigationObject.SIGNUP_SCREEN
            ) {
//                SignUpScreen(
//                    navController,
//                )
            }
        }
    }
}