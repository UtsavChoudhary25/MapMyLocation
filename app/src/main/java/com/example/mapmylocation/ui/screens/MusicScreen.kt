package com.example.mapmylocation.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mapmylocation.data.models.MusicPlayerState
import com.example.mapmylocation.data.models.RecommendedTrack
import com.example.mapmylocation.ui.theme.BorderColor
import com.example.mapmylocation.ui.theme.CardWhite
import com.example.mapmylocation.ui.theme.CharcoalText
import com.example.mapmylocation.ui.theme.ForestGreen
import com.example.mapmylocation.ui.theme.MutedText
import com.example.mapmylocation.ui.theme.SageGreenLight
import com.example.mapmylocation.ui.theme.Terracotta
import com.example.mapmylocation.ui.theme.WarmIvory
import com.example.mapmylocation.viewmodel.AppUiState

@Composable
fun MusicScreen(
    uiState: AppUiState,
    playerState: MusicPlayerState,
    onPlayPauseClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    onSkipPreviousClick: () -> Unit,
    onStopClick: () -> Unit,
    onSeekTo: (Long) -> Unit,
    onTrackSelect: (RecommendedTrack) -> Unit,
    onToggleAutoMusic: (Boolean) -> Unit
) {
    val track = playerState.currentTrack ?: uiState.recommendedTrack
    val currentWeather = uiState.destinationWeather ?: uiState.currentWeather

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // TOP HEADER
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "MapMyLocation Radio",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalText
                    )
                    Text(
                        text = "Weather-Matched Journey Soundtrack",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MutedText
                    )
                }

                currentWeather?.let { w ->
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(ForestGreen.copy(alpha = 0.1f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "🌤️ ${w.category.displayName}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // LARGE ALBUM ARTWORK CARD
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = CardWhite,
                shadowElevation = 4.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Album Cover Box
                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(ForestGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = "Album Cover",
                                tint = CardWhite,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = track?.weatherCategory?.displayName?.uppercase() ?: "SOUNDTRACK",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = WarmIvory,
                                letterSpacing = 1.2.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Track Title & Artist
                    Text(
                        text = track?.title ?: "Select a Track",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalText
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${track?.artist ?: "MapMyLocation Radio"} • ${track?.category ?: "Ambient"}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = MutedText
                    )

                    track?.description?.let { desc ->
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SageGreenLight)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "✨ $desc",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = ForestGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // PROGRESS SLIDER & TIMESTAMPS
                    val currentPos = playerState.currentPositionMs
                    val totalDuration = if (playerState.durationMs > 0) playerState.durationMs else (track?.durationSeconds?.times(1000L) ?: 100000L)

                    Slider(
                        value = currentPos.coerceAtMost(totalDuration).toFloat(),
                        onValueChange = { onSeekTo(it.toLong()) },
                        valueRange = 0f..totalDuration.toFloat(),
                        colors = SliderDefaults.colors(
                            thumbColor = ForestGreen,
                            activeTrackColor = ForestGreen,
                            inactiveTrackColor = BorderColor
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = formatTimeMs(currentPos),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MutedText
                        )
                        Text(
                            text = formatTimeMs(totalDuration),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MutedText
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // PLAYER CONTROLS (Prev, Play/Pause, Next, Stop)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onStopClick() },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(WarmIvory)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "Stop",
                                tint = CharcoalText,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = { onSkipPreviousClick() },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(WarmIvory)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipPrevious,
                                contentDescription = "Previous",
                                tint = CharcoalText,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        // Play/Pause Main Button
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(ForestGreen)
                                .clickable { onPlayPauseClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (playerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (playerState.isPlaying) "Pause" else "Play",
                                tint = CardWhite,
                                modifier = Modifier.size(34.dp)
                            )
                        }

                        IconButton(
                            onClick = { onSkipNextClick() },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(WarmIvory)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipNext,
                                contentDescription = "Next",
                                tint = CharcoalText,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // AUTO MUSIC TOGGLE ROW
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = CardWhite,
                shadowElevation = 1.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Auto Music Recommendation",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CharcoalText
                        )
                        Text(
                            text = "Automatically match playlist when destination changes",
                            fontSize = 12.sp,
                            color = MutedText
                        )
                    }

                    Switch(
                        checked = uiState.autoMusicEnabled,
                        onCheckedChange = { onToggleAutoMusic(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CardWhite,
                            checkedTrackColor = ForestGreen,
                            uncheckedThumbColor = MutedText,
                            uncheckedTrackColor = BorderColor
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Playlist for this journey",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = CharcoalText
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        // PLAYLIST TRACK LIST
        items(uiState.playlist) { itemTrack ->
            val isSelected = track?.id == itemTrack.id
            val isCurrentPlaying = isSelected && playerState.isPlaying

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onTrackSelect(itemTrack) },
                shape = RoundedCornerShape(14.dp),
                color = if (isSelected) SageGreenLight.copy(alpha = 0.6f) else CardWhite,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSelected) ForestGreen else BorderColor
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) ForestGreen else WarmIvory),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isCurrentPlaying) Icons.Default.VolumeUp else Icons.Default.MusicNote,
                            contentDescription = "Track",
                            tint = if (isSelected) CardWhite else ForestGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = itemTrack.title,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                            color = CharcoalText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${itemTrack.category} • ${itemTrack.weatherCategory.displayName}",
                            fontSize = 12.sp,
                            color = MutedText
                        )
                    }

                    if (isCurrentPlaying) {
                        Text(
                            text = "Playing",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

private fun formatTimeMs(ms: Long): String {
    val totalSeconds = (ms / 1000).toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}
