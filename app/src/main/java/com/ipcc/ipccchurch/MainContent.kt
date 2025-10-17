package com.ipcc.ipccchurch

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ipcc.ipccchurch.ui.Screen
import com.ipcc.ipccchurch.ui.screens.events.EventsScreen
import com.ipcc.ipccchurch.ui.screens.home.HomeScreen
import com.ipcc.ipccchurch.ui.screens.player.PlayerScreen
import com.ipcc.ipccchurch.ui.screens.profile.ProfileScreen
import com.ipcc.ipccchurch.ui.screens.sermonlist.SermonListScreen
import com.ipcc.ipccchurch.ui.screens.sermons.SermonsScreen
import com.ipcc.ipccchurch.ui.screens.settings.SettingsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(onLogout: () -> Unit) {
    // This NavController is ONLY for the main app screens (Home, Sermons, etc.)
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val navItems = listOf(Screen.Home, Screen.Sermons, Screen.Events, Screen.Profile)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var topBarActions by remember { mutableStateOf<@Composable RowScope.() -> Unit>({}) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(16.dp))
                NavigationDrawerItem(
                    label = { Text("Settings") },
                    selected = currentDestination?.route == "settings",
                    onClick = {
                        navController.navigate("settings")
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.Settings, null) }
                )
                NavigationDrawerItem(
                    label = { Text("About Us") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Default.Info, null) }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("IPCC Church") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, "Menu")
                        }
                    },
                    actions = { topBarActions(this) }
                )
            },
            bottomBar = {
                NavigationBar {
                    navItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
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
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Home.route) {
                    topBarActions = {
                        IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                            Icon(Icons.Filled.Person, "Profile")
                        }
                    }
                    HomeScreen(
                        onSermonClick = { playlistId, sermonId -> navController.navigate("player/$playlistId/$sermonId") },
                        onPlaylistClick = { playlistId -> navController.navigate("sermon_list/$playlistId") }
                    )
                }
                composable(Screen.Sermons.route) {
                    topBarActions = {
                        IconButton(onClick = { /* Search click */ }) {
                            Icon(Icons.Filled.Search, "Search")
                        }
                        IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                            Icon(Icons.Filled.Person, "Profile")
                        }
                    }
                    SermonsScreen(
                        onSermonClick = { playlistId, sermonId -> navController.navigate("player/$playlistId/$sermonId") },
                        onPlaylistClick = { playlistId -> navController.navigate("sermon_list/$playlistId") }
                    )
                }
                composable(Screen.Events.route) { EventsScreen() }
                composable(Screen.Profile.route) {
                    topBarActions = {}
                    ProfileScreen(onLogout = onLogout) // Pass the logout function down
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
}