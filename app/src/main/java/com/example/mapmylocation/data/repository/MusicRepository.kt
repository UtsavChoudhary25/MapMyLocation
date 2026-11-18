package com.example.mapmylocation.data.repository

import com.example.mapmylocation.R
import com.example.mapmylocation.data.models.RecommendedTrack
import com.example.mapmylocation.data.models.WeatherCategory

class MusicRepository {

    private val allTracks = listOf(
        // SUNNY TRACKS
        RecommendedTrack(
            id = "sunny_1",
            title = "Golden Hour Journey",
            artist = "MapMyLocation Audio Lab",
            category = "Energetic Instrumental",
            weatherCategory = WeatherCategory.SUNNY,
            rawResId = R.raw.sunny_track_1,
            durationSeconds = 240,
            albumCoverSubtitle = "Selected for Sunny Weather",
            description = "Selected to match clear skies & sunny weather."
        ),
        RecommendedTrack(
            id = "sunny_2",
            title = "Sunlit Highway",
            artist = "MapMyLocation Audio Lab",
            category = "Upbeat Drive",
            weatherCategory = WeatherCategory.SUNNY,
            rawResId = R.raw.sunny_track_2,
            durationSeconds = 210,
            albumCoverSubtitle = "Energetic Sunshine Mix",
            description = "Bring some energy to your journey."
        ),

        // RAINY TRACKS
        RecommendedTrack(
            id = "rainy_1",
            title = "Raindrops on Glass",
            artist = "MapMyLocation Audio Lab",
            category = "Lo-Fi & Soft Piano",
            weatherCategory = WeatherCategory.RAINY,
            rawResId = R.raw.rainy_track_1,
            durationSeconds = 300,
            albumCoverSubtitle = "Rainy Day Lo-Fi",
            description = "Selected to match rainy weather."
        ),
        RecommendedTrack(
            id = "rainy_2",
            title = "Cozy Shelter",
            artist = "MapMyLocation Audio Lab",
            category = "Relaxing Instrumental",
            weatherCategory = WeatherCategory.RAINY,
            rawResId = R.raw.rainy_track_2,
            durationSeconds = 280,
            albumCoverSubtitle = "Calm Ambient Rain",
            description = "Something calm for your journey."
        ),

        // CLOUDY TRACKS
        RecommendedTrack(
            id = "cloudy_1",
            title = "Cloud Drift",
            artist = "MapMyLocation Audio Lab",
            category = "Acoustic Chill",
            weatherCategory = WeatherCategory.CLOUDY,
            rawResId = R.raw.cloudy_track_1,
            durationSeconds = 250,
            albumCoverSubtitle = "Cloudy Acoustic",
            description = "A cloudy day outside, enjoy something chill."
        ),

        // SNOWY TRACKS
        RecommendedTrack(
            id = "snowy_1",
            title = "Winter Piano Reverie",
            artist = "MapMyLocation Audio Lab",
            category = "Peaceful Piano",
            weatherCategory = WeatherCategory.SNOWY,
            rawResId = R.raw.snowy_track_1,
            durationSeconds = 300,
            albumCoverSubtitle = "Winter Piano",
            description = "Cold weather detected. Slow down and enjoy the ride."
        ),

        // OTHER TRACKS
        RecommendedTrack(
            id = "other_1",
            title = "Misty Valley",
            artist = "MapMyLocation Audio Lab",
            category = "Neutral Instrumental",
            weatherCategory = WeatherCategory.OTHER,
            rawResId = R.raw.other_track_1,
            durationSeconds = 240,
            albumCoverSubtitle = "Ambient Horizon",
            description = "Neutral instrumental for atmospheric conditions."
        )
    )

    fun getRecommendedTrackForWeather(category: WeatherCategory): RecommendedTrack {
        val matching = allTracks.filter { it.weatherCategory == category }
        return matching.firstOrNull() ?: allTracks.first()
    }

    fun getPlaylistForWeather(category: WeatherCategory): List<RecommendedTrack> {
        val matching = allTracks.filter { it.weatherCategory == category }
        // If only 1 track for category, include neutral fallback tracks to build playlist
        return if (matching.size >= 2) {
            matching
        } else {
            matching + allTracks.filter { it.weatherCategory != category }.take(3)
        }
    }

    fun getAllTracks(): List<RecommendedTrack> = allTracks
}
