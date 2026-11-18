package com.example.mapmylocation

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mapmylocation.ui.components.MapBottomNavBar
import com.example.mapmylocation.ui.components.NavDestination
import com.example.mapmylocation.ui.screens.ExploreScreen
import com.example.mapmylocation.ui.screens.HomeScreen
import com.example.mapmylocation.ui.screens.MusicScreen
import com.example.mapmylocation.ui.screens.SettingsDialog
import com.example.mapmylocation.ui.theme.MapMyLocationTheme
import com.example.mapmylocation.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Location permission denied. Using default location.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Request location permissions on startup
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        setContent {
            MapMyLocationTheme {
                MapMyLocationApp()
            }
        }
    }
}

@Composable
fun MapMyLocationApp(viewModel: AppViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: NavDestination.HOME.route
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.dismissError()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            MapBottomNavBar(
                currentRoute = currentRoute,
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        popUpTo(NavDestination.HOME.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = NavDestination.HOME.route
            ) {
                composable(NavDestination.HOME.route) {
                    HomeScreen(
                        uiState = uiState,
                        playerState = playerState,
                        onSearchDestination = { query ->
                            viewModel.searchAndSetDestination(query)
                        },
                        onUseCurrentLocation = {
                            viewModel.useCurrentLocationAsOrigin()
                        },
                        onPlayPauseClick = {
                            viewModel.togglePlayPause()
                        },
                        onMusicCardClick = {
                            navController.navigate(NavDestination.MUSIC.route)
                        },
                        onNavigateToExplore = {
                            navController.navigate(NavDestination.EXPLORE.route)
                        },
                        onOpenSettings = {
                            viewModel.openSettings()
                        }
                    )
                }

                composable(NavDestination.EXPLORE.route) {
                    ExploreScreen(
                        uiState = uiState,
                        onSearchDestination = { query ->
                            viewModel.searchAndSetDestination(query)
                        },
                        onUseCurrentLocation = {
                            viewModel.useCurrentLocationAsOrigin()
                        },
                        onClearRoute = {
                            viewModel.clearRoute()
                        },
                        onStartJourneyClick = {
                            viewModel.togglePlayPause()
                            navController.navigate(NavDestination.MUSIC.route)
                        }
                    )
                }

                composable(NavDestination.MUSIC.route) {
                    MusicScreen(
                        uiState = uiState,
                        playerState = playerState,
                        onPlayPauseClick = { viewModel.togglePlayPause() },
                        onSkipNextClick = { viewModel.playerManager.skipNext() },
                        onSkipPreviousClick = { viewModel.playerManager.skipPrevious() },
                        onStopClick = { viewModel.playerManager.stop() },
                        onSeekTo = { pos -> viewModel.playerManager.seekTo(pos) },
                        onTrackSelect = { track -> viewModel.playTrackDirectly(track) },
                        onToggleAutoMusic = { enabled -> viewModel.toggleAutoMusic(enabled) }
                    )
                }
            }

            // Settings Dialog Overlay
            if (uiState.isSettingsOpen) {
                SettingsDialog(
                    uiState = uiState,
                    onDismiss = { viewModel.closeSettings() },
                    onToggleAutoMusic = { enabled -> viewModel.toggleAutoMusic(enabled) },
                    onToggleTempUnit = { viewModel.toggleTemperatureUnit() }
                )
            }
        }
    }
}
