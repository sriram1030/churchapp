package com.ipcc.ipccchurch.ui.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = viewModel(),
    onNavigateToAuth: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    val context = LocalContext.current
    val authState by splashViewModel.authState

    // This runs once when the screen appears
    LaunchedEffect(Unit) {
        splashViewModel.checkAuthStatus(context)
    }

    // This runs whenever the authState changes
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> onNavigateToMain()
            is AuthState.Unauthenticated -> onNavigateToAuth()
            else -> { /* Stay on splash while loading */ }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}