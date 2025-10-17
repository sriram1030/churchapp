package com.ipcc.ipccchurch

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ipcc.ipccchurch.ui.Screen
import com.ipcc.ipccchurch.ui.screens.auth.AuthScreen
import com.ipcc.ipccchurch.ui.screens.events.EventsScreen
import com.ipcc.ipccchurch.ui.screens.home.HomeScreen
import com.ipcc.ipccchurch.ui.screens.player.PlayerScreen
import com.ipcc.ipccchurch.ui.screens.profile.ProfileScreen
import com.ipcc.ipccchurch.ui.screens.sermonlist.SermonListScreen
import com.ipcc.ipccchurch.ui.screens.sermons.SermonsScreen
import com.ipcc.ipccchurch.ui.screens.settings.SettingsScreen
import com.ipcc.ipccchurch.ui.screens.splash.SplashScreen
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    val navItems = listOf(Screen.Home, Screen.Sermons, Screen.Events, Screen.Profile)
    val showScaffold = navItems.any { it.route == currentDestination?.route }

    Scaffold(
        topBar = {
            if (showScaffold) {
                TopAppBar(
                    title = { Text("IPCC Church") },
                    navigationIcon = {
                        IconButton(onClick = { /* TODO: Drawer logic */ }) {
                            Icon(Icons.Default.Menu, "Menu")
                        }
                    },
                    actions = {
                        if (currentDestination?.route == Screen.Sermons.route) {
                            IconButton(onClick = { /* Search */ }) { Icon(Icons.Default.Search, "Search") }
                        }
                        IconButton(onClick = { navController.navigate(Screen.Profile.route) }) { Icon(Icons.Default.Person, "Profile") }
                    }
                )
            }
        },
        bottomBar = {
            if (showScaffold) {
                NavigationBar {
                    navItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, null) },
                            label = { Text(screen.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id)
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") {
                SplashScreen(
                    onNavigateToAuth = { navController.navigate("auth") { popUpTo("splash") { inclusive = true } } },
                    onNavigateToMain = { navController.navigate(Screen.Home.route) { popUpTo("splash") { inclusive = true } } }
                )
            }

            composable("auth") {
                AuthScreen(
                    onLoginSuccess = { navController.navigate(Screen.Home.route) { popUpTo("auth") { inclusive = true } } },
                    onRegisterSuccess = { navController.navigate(Screen.Home.route) { popUpTo("auth") { inclusive = true } } }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    onSermonClick = { playlistId, sermonId -> navController.navigate("player/$playlistId/$sermonId") },
                    onPlaylistClick = { playlistId -> navController.navigate("sermon_list/$playlistId") }
                )
            }
            composable(Screen.Sermons.route) {
                SermonsScreen(
                    onSermonClick = { playlistId, sermonId -> navController.navigate("player/$playlistId/$sermonId") },
                    onPlaylistClick = { playlistId -> navController.navigate("sermon_list/$playlistId") }
                )
            }
            composable(Screen.Events.route) { EventsScreen() }
            composable(Screen.Profile.route) {
                ProfileScreen(onLogout = {
                    runBlocking { sessionManager.clearAuthToken() }
                    navController.navigate("auth") { popUpTo(Screen.Home.route) { inclusive = true } }
                })
            }
            composable("settings") { SettingsScreen() }
            composable("sermon_list/{playlistId}") { backStackEntry ->
                val playlistId = backStackEntry.arguments?.getString("playlistId")
                SermonListScreen(
                    playlistId = playlistId,
                    onSermonClick = { pId, sermonId -> navController.navigate("player/$pId/$sermonId") }
                )
            }
            composable("player/{playlistId}/{sermonId}") { backStackEntry ->
                val playlistId = backStackEntry.arguments?.getString("playlistId")
                val sermonId = backStackEntry.arguments?.getString("sermonId")
                PlayerScreen(playlistId, sermonId, navController)
            }
        }
    }
}