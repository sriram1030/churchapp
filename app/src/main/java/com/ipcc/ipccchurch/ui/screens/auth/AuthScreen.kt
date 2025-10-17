package com.ipcc.ipccchurch.ui.screens.auth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit,
    onRegisterSuccess: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val isLoading by authViewModel.isLoading
    val errorMessage by authViewModel.errorMessage

    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = Modifier.fillMaxSize()
    ) {
        composable("login") {
            LoginScreen(
                isLoading = isLoading,
                errorMessage = errorMessage,
                onLoginClick = { email, pass ->
                    authViewModel.loginUser(email, pass, context, onSuccess = onLoginSuccess)
                },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                isLoading = isLoading,
                errorMessage = errorMessage,
                onRegisterClick = { name, email, pass ->
                    authViewModel.registerUser(name, email, pass, context, onSuccess = onRegisterSuccess)
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
    }
}