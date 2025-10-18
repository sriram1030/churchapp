package com.ipcc.ipccchurch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ipcc.ipccchurch.ui.MiniPlayer
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
    val sharedPlayerViewModel: SharedPlayerViewModel = viewModel()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    LaunchedEffect(Unit) {
        sharedPlayerViewModel.initializeController(context)
    }

    val navItems = listOf(Screen.Home, Screen.Sermons, Screen.Events, Screen.Profile)
    val mainScreenRoutes = navItems.map { it.route }

    // --- Visibility Logic ---
    val isMainScreen = mainScreenRoutes.any { route -> currentDestination?.hierarchy?.any { it.route == route } == true }
    val isPlayerScreen = currentDestination?.route?.startsWith("player") == true

    val showTopBar = currentDestination?.route !in listOf("login", "register", "splash")
    val showBottomNav = isMainScreen
    val showMiniPlayer = sharedPlayerViewModel.currentSermon.value != null && !isPlayerScreen

    Scaffold(
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = {
                        val title = when {
                            currentDestination?.route?.startsWith("sermon_list") == true -> "Sermon Series"
                            isPlayerScreen -> "Now Playing"
                            currentDestination?.route == "settings" -> "Settings"
                            else -> "IPCC Church"
                        }
                        Text(title)
                    },
                    navigationIcon = {
                        if (!isMainScreen) {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                            }
                        } else {
                            IconButton(onClick = { /* TODO: Drawer */ }) {
                                Icon(Icons.Default.Menu, "Menu")
                            }
                        }
                    },
                    actions = {
                        if (currentDestination?.route == Screen.Sermons.route) {
                            IconButton(onClick = { /* Search */ }) { Icon(Icons.Default.Search, "Search") }
                        }
                        if (isMainScreen) {
                            IconButton(onClick = { navController.navigate(Screen.Profile.route) }) { Icon(Icons.Default.Person, "Profile") }
                        }
                    }
                )
            }
        },
        bottomBar = {
            // The bottom bar is a Column that conditionally shows the MiniPlayer and NavigationBar
            Column {
                if (showMiniPlayer) {
                    MiniPlayer(
                        sharedPlayerViewModel = sharedPlayerViewModel,
                        onNavigateToPlayer = {
                            val sermon = sharedPlayerViewModel.currentSermon.value
                            if (sermon != null) {
                                navController.navigate("player/latest/${sermon.id}")
                            }
                        }
                    )
                }
                if (showBottomNav) {
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
            // In MainApp.kt, inside the NavHost
            composable(Screen.Home.route) {
                HomeScreen(
                    onSermonClick = { playlistId, sermonId -> navController.navigate("player/$playlistId/$sermonId") },
                    onPlaylistClick = { playlistId -> navController.navigate("sermon_list/$playlistId") },
                    sharedPlayerViewModel = sharedPlayerViewModel // Pass the shared instance here
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
                PlayerScreen(playlistId, sermonId, navController, sharedPlayerViewModel)
            }
        }
    }
}