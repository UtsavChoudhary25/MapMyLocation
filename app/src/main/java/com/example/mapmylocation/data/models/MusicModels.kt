package com.example.mapmylocation.data.models

import androidx.annotation.RawRes

data class RecommendedTrack(
    val id: String,
    val title: String,
    val artist: String = "MapMyLocation Radio",
    val category: String,
    val weatherCategory: WeatherCategory,
    @RawRes val rawResId: Int,
    val durationSeconds: Int,
    val albumCoverSubtitle: String = "Weather-Matched Journey Track",
    val description: String
)

data class MusicPlayerState(
    val isPlaying: Boolean = false,
    val currentTrack: RecommendedTrack? = null,
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L,
    val autoMusicEnabled: Boolean = true,
    val playlist: List<RecommendedTrack> = emptyList(),
    val isLoaded: Boolean = false
)
