package com.ipcc.ipccchurch.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Radio // <-- Add this import
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Sermons : Screen("sermons", "Sermons", Icons.Default.PlayArrow)
    object Radio : Screen("radio", "Radio", Icons.Default.Radio) // <-- Add this line
    object Events : Screen("events", "Events", Icons.Default.DateRange)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
}