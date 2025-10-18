package com.ipcc.ipccchurch.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ipcc.ipccchurch.R
import com.ipcc.ipccchurch.SessionManager
import kotlinx.coroutines.flow.first

@Composable
fun SplashScreen(
    onNavigateToAuth: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    // This effect runs only once when the screen is first displayed
    LaunchedEffect(Unit) {
        // Check if a token exists in DataStore
        val token = sessionManager.authToken.first()
        if (token.isNullOrEmpty()) {
            // No token, navigate to the authentication flow
            onNavigateToAuth()
        } else {
            // Token exists, navigate to the main app
            onNavigateToMain()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Display your logo while the check is running
        Image(
            painter = painterResource(id = R.drawable.church_logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(150.dp)
        )
    }
}