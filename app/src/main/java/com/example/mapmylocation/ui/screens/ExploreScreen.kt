package com.example.mapmylocation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mapmylocation.ui.components.DestinationSearchBar
import com.example.mapmylocation.ui.components.RouteSummarySheet
import com.example.mapmylocation.ui.theme.CardWhite
import com.example.mapmylocation.ui.theme.ForestGreen
import com.example.mapmylocation.viewmodel.AppUiState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun ExploreScreen(
    uiState: AppUiState,
    onSearchDestination: (String) -> Unit,
    onUseCurrentLocation: () -> Unit,
    onClearRoute: () -> Unit,
    onStartJourneyClick: () -> Unit
) {
    val originLatLng = remember(uiState.currentLocation) {
        LatLng(uiState.currentLocation.latitude, uiState.currentLocation.longitude)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(originLatLng, 12f)
    }

    // Camera animation when route is calculated
    LaunchedEffect(uiState.activeRoute) {
        uiState.activeRoute?.let { route ->
            if (route.polylinePoints.isNotEmpty()) {
                val builder = LatLngBounds.builder()
                route.polylinePoints.forEach { builder.include(it) }
                val bounds = builder.build()
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngBounds(bounds, 120),
                    1000
                )
            }
        }
    }

    val mapUiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = false,
            compassEnabled = true
        )
    }

    val mapProperties = remember {
        MapProperties(
            isMyLocationEnabled = false
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // FULL GOOGLE MAP VIEW
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = mapUiSettings,
            properties = mapProperties
        ) {
            // Origin Marker (Current Location)
            val originMarkerState = rememberMarkerState(position = originLatLng)
            originMarkerState.position = originLatLng
            Marker(
                state = originMarkerState,
                title = "Origin: ${uiState.currentLocation.city}",
                snippet = "Starting Point",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            )

            // Destination Marker & Polyline
            uiState.destinationLocation?.let { dest ->
                val destLatLng = LatLng(dest.latitude, dest.longitude)
                val destMarkerState = rememberMarkerState(position = destLatLng)
                destMarkerState.position = destLatLng
                Marker(
                    state = destMarkerState,
                    title = dest.name,
                    snippet = "Destination",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                )
            }

            uiState.activeRoute?.let { route ->
                if (route.polylinePoints.isNotEmpty()) {
                    Polyline(
                        points = route.polylinePoints,
                        color = ForestGreen,
                        width = 10f
                    )
                }
            }
        }

        // FLOATING TOP SEARCH BAR
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .align(Alignment.TopCenter)
        ) {
            DestinationSearchBar(
                initialQuery = uiState.destinationLocation?.name ?: "",
                isLoading = uiState.isLoadingRoute,
                onSearch = onSearchDestination,
                onUseCurrentLocation = {
                    onUseCurrentLocation()
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(originLatLng, 14f)
                }
            )
        }

        // FLOATING CURRENT LOCATION FAB
        FloatingActionButton(
            onClick = {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(originLatLng, 14f)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 20.dp,
                    bottom = if (uiState.activeRoute != null) 280.dp else 100.dp
                ),
            shape = CircleShape,
            containerColor = CardWhite,
            contentColor = ForestGreen,
            elevation = FloatingActionButtonDefaults.elevation(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "My Location",
                modifier = Modifier.size(24.dp)
            )
        }

        // FLOATING ROUTE SUMMARY BOTTOM SHEET
        AnimatedVisibility(
            visible = uiState.activeRoute != null,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 74.dp)
        ) {
            uiState.activeRoute?.let { route ->
                RouteSummarySheet(
                    routeInfo = route,
                    destinationWeather = uiState.destinationWeather,
                    onViewRouteClick = onStartJourneyClick,
                    onClearRouteClick = onClearRoute
                )
            }
        }
    }
}
