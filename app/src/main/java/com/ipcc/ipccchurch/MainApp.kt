package com.ipcc.ipccchurch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ipcc.ipccchurch.ui.screens.auth.AuthScreen

@Composable
fun MainApp() {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    // Read the login state from DataStore
    val isLoggedIn by sessionManager.authToken.collectAsState(initial = null)

    val navController = rememberNavController()

    // Determine the start destination based on the login state
    val startDestination = if (isLoggedIn != null) "main" else "auth"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("auth") {
            AuthScreen(
                onLoginSuccess = {
                    navController.navigate("main") { popUpTo("auth") { inclusive = true } }
                },
                onRegisterSuccess = {
                    navController.navigate("main") { popUpTo("auth") { inclusive = true } }
                }
            )
        }

        composable("main") {
            MainContent( // This is the new name for our main app Composable
                onLogout = {
                    // We need to pass the coroutine scope to clear the token
                    // For simplicity, we navigate first. A ViewModel is better for this.
                    navController.navigate("auth") { popUpTo("main") { inclusive = true } }
                }
            )
        }
    }
}