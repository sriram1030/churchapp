package com.ipcc.ipccchurch.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ipcc.ipccchurch.R // Make sure this import is correct for your R file

@Composable
fun SplashScreen(
    onNavigateToAuth: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    var showLogo by remember { mutableStateOf(false) } // State to control logo animation

    LaunchedEffect(Unit) {
        showLogo = true // Trigger logo animation immediately
        // Simulate some loading time
        kotlinx.coroutines.delay(2000L) // Show splash for 2 seconds
        // Add your actual authentication check here
        // For now, let's assume it navigates to auth if no token, else to main
        val authTokenExists = false // Replace with actual SessionManager check
        if (authTokenExists) {
            onNavigateToMain()
        } else {
            onNavigateToAuth()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // You can change the background color
        contentAlignment = Alignment.Center
    ) {
        // Animated logo
        androidx.compose.animation.AnimatedVisibility(
            visible = showLogo,
            enter = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) + fadeIn(),
            exit = scaleOut() + fadeOut() // Optional exit animation
        ) {
            Image(
                painter = painterResource(id = R.drawable.church_logo), // <--- CHANGE THIS TO YOUR LOGO RESOURCE ID
                contentDescription = "App Logo",
                modifier = Modifier.size(200.dp) // Adjust size as needed
            )
        }
    }
}