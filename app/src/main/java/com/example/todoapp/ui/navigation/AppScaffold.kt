package com.example.todoapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.R
import com.example.todoapp.data.repository.AuthRepository
import com.example.todoapp.ui.theme.TodoAppTheme
import kotlinx.coroutines.launch

/** Dane jednego elementu dolnej nawigacji. */
private data class BottomNavItem(
    val destination: Destination,
    val icon: ImageVector,
    val labelRes: Int
)

private val bottomNavItems = listOf(
    BottomNavItem(Destination.Home, Icons.Default.Home, R.string.nav_home),
    BottomNavItem(Destination.Settings, Icons.Default.Settings, R.string.nav_settings),
    BottomNavItem(Destination.Contact, Icons.Default.Info, R.string.nav_contact),
)

/**
 * Główny composable aplikacji — odpowiada za:
 * - motyw (ciemny/jasny),
 * - Navigation Drawer,
 * - Bottom Navigation Bar,
 * - NavHost ze wszystkimi ekranami.
 */
@Composable
fun AppRoot(authRepository: AuthRepository) {
    val startDestination = if (authRepository.isLoggedIn) Destination.Home.route else Destination.Login.route
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Stan dark mode — przechowywany tutaj, przekazywany do Theme i SettingsScreen
    var darkMode by remember { mutableStateOf(false) }

    // Na ekranach logowania chowamy drawer i bottom bar
    val isAuthScreen = currentRoute == Destination.Login.route ||
            currentRoute == Destination.Register.route

    TodoAppTheme(darkTheme = darkMode) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = !isAuthScreen,
            drawerContent = {
                ModalDrawerSheet {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Map, contentDescription = null) },
                        label = { Text(stringResource(R.string.nav_map)) },
                        selected = currentRoute == Destination.Map.route,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Destination.Map.route) {
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        ) {
            Scaffold(
                bottomBar = {
                    if (!isAuthScreen) {
                        NavigationBar {
                            val currentDestination = navBackStackEntry?.destination
                            bottomNavItems.forEach { item ->
                                NavigationBarItem(
                                    icon = { Icon(item.icon, contentDescription = null) },
                                    label = { Text(stringResource(item.labelRes)) },
                                    selected = currentDestination?.hierarchy?.any {
                                        it.route == item.destination.route
                                    } == true,
                                    onClick = {
                                        navController.navigate(item.destination.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                AppNavHost(
                    navController = navController,
                    startDestination = startDestination,
                    darkMode = darkMode,
                    onDarkModeToggle = { darkMode = it },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}