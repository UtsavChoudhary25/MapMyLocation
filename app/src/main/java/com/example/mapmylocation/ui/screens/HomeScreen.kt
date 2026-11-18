package com.example.mapmylocation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mapmylocation.data.models.MusicPlayerState
import com.example.mapmylocation.data.models.RecommendedTrack
import com.example.mapmylocation.ui.components.DestinationSearchBar
import com.example.mapmylocation.ui.components.MusicRecommendationCard
import com.example.mapmylocation.ui.components.WeatherCard
import com.example.mapmylocation.ui.theme.BorderColor
import com.example.mapmylocation.ui.theme.CardWhite
import com.example.mapmylocation.ui.theme.CharcoalText
import com.example.mapmylocation.ui.theme.ForestGreen
import com.example.mapmylocation.ui.theme.MutedText
import com.example.mapmylocation.ui.theme.SageGreenLight
import com.example.mapmylocation.ui.theme.WarmIvory
import com.example.mapmylocation.viewmodel.AppUiState
import java.util.Calendar

@Composable
fun HomeScreen(
    uiState: AppUiState,
    playerState: MusicPlayerState,
    onSearchDestination: (String) -> Unit,
    onUseCurrentLocation: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onMusicCardClick: () -> Unit,
    onNavigateToExplore: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val scrollState = rememberScrollState()

    val greeting = rememberGreeting()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // HEADER: Branding, greeting, location, settings icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(ForestGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "MapMyLocation",
                            tint = CardWhite,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "MapMyLocation",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen,
                        letterSpacing = (-0.5).sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "$greeting, traveller",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = CharcoalText
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "📍 ${uiState.currentLocation.city}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MutedText
                    )
                }
            }

            IconButton(
                onClick = { onOpenSettings() },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(CardWhite)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = CharcoalText,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // MAIN DESTINATION SECTION
        Text(
            text = "Where are we going?",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = CharcoalText
        )
        Text(
            text = "Let's make your journey better.",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = MutedText
        )

        Spacer(modifier = Modifier.height(14.dp))

        DestinationSearchBar(
            initialQuery = uiState.destinationLocation?.name ?: "",
            isLoading = uiState.isLoadingRoute,
            onSearch = onSearchDestination,
            onUseCurrentLocation = onUseCurrentLocation
        )

        Spacer(modifier = Modifier.height(20.dp))

        // CURRENT WEATHER SECTION
        if (uiState.isLoadingWeather) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(CardWhite),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = ForestGreen)
            }
        } else {
            uiState.currentWeather?.let { weather ->
                WeatherCard(
                    weatherInfo = weather,
                    temperatureUnit = uiState.temperatureUnit,
                    titlePrefix = "Current Weather"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ACTIVE JOURNEY SUMMARY SECTION (If route calculated)
        uiState.activeRoute?.let { route ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = CardWhite,
                shadowElevation = 2.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PLANNED JOURNEY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen,
                            letterSpacing = 0.8.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SageGreenLight)
                                .clickable { onNavigateToExplore() }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Map,
                                    contentDescription = "View Map",
                                    tint = ForestGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "View Map",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreen
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${route.originName} ➔ ${route.destinationName}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalText
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Distance: ${route.distanceText} • Duration: ${route.durationText}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MutedText
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // MUSIC RECOMMENDATION SECTION
        uiState.recommendedTrack?.let { track ->
            MusicRecommendationCard(
                track = track,
                isPlaying = playerState.isPlaying,
                onPlayPauseClick = onPlayPauseClick,
                onCardClick = onMusicCardClick
            )
        }

        Spacer(modifier = Modifier.height(80.dp)) // Padding for bottom navbar
    }
}

@Composable
private fun rememberGreeting(): String {
    val cal = Calendar.getInstance()
    val hour = cal.get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 4..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        else -> "Good evening"
    }
}
