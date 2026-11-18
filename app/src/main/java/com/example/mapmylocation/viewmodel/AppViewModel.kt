package com.example.mapmylocation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapmylocation.data.models.LocationData
import com.example.mapmylocation.data.models.MusicPlayerState
import com.example.mapmylocation.data.models.RecommendedTrack
import com.example.mapmylocation.data.models.RouteInfo
import com.example.mapmylocation.data.models.WeatherCategory
import com.example.mapmylocation.data.models.WeatherInfo
import com.example.mapmylocation.data.repository.DirectionsRepository
import com.example.mapmylocation.data.repository.MusicRepository
import com.example.mapmylocation.data.repository.WeatherRepository
import com.example.mapmylocation.location.LocationManager
import com.example.mapmylocation.player.AudioPlayerManager
import com.example.mapmylocation.utils.Constants
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AppUiState(
    val currentLocation: LocationData = LocationData(37.7749, -122.4194, "San Francisco", "San Francisco"),
    val destinationLocation: LocationData? = null,
    val currentWeather: WeatherInfo? = null,
    val destinationWeather: WeatherInfo? = null,
    val activeRoute: RouteInfo? = null,
    val recommendedTrack: RecommendedTrack? = null,
    val playlist: List<RecommendedTrack> = emptyList(),
    val temperatureUnit: String = "°C", // "°C" or "°F"
    val autoMusicEnabled: Boolean = true,
    val isAutoPlayConfirmed: Boolean = false,
    val isLoadingRoute: Boolean = false,
    val isLoadingWeather: Boolean = false,
    val isSettingsOpen: Boolean = false,
    val errorMessage: String? = null
)

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val locationManager = LocationManager(application)
    private val weatherRepository = WeatherRepository()
    private val directionsRepository = DirectionsRepository()
    private val musicRepository = MusicRepository()
    val playerManager = AudioPlayerManager(application)

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    val playerState: StateFlow<MusicPlayerState> = playerManager.playerState

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingWeather = true) }
            val currentLoc = locationManager.fetchCurrentLocation()
            _uiState.update { it.copy(currentLocation = currentLoc) }

            // Fetch current weather
            val weatherResult = weatherRepository.getWeatherForLocation(currentLoc.latitude, currentLoc.longitude)
            val weather = weatherResult.getOrDefault(
                WeatherInfo(
                    temperatureCelsius = 21.0,
                    condition = "Clear Sky",
                    description = "Clear skies & bright sunlight",
                    category = WeatherCategory.SUNNY,
                    iconCode = "01d",
                    cityName = currentLoc.city,
                    humidityPercent = 55,
                    windSpeedKmh = 12.0
                )
            )

            // Recommend initial music based on weather
            val initialTrack = musicRepository.getRecommendedTrackForWeather(weather.category)
            val initialPlaylist = musicRepository.getPlaylistForWeather(weather.category)
            playerManager.setPlaylist(initialPlaylist)
            playerManager.playTrack(initialTrack, forcePlay = false)

            _uiState.update {
                it.copy(
                    currentWeather = weather,
                    recommendedTrack = initialTrack,
                    playlist = initialPlaylist,
                    isLoadingWeather = false
                )
            }
        }
    }

    fun searchAndSetDestination(destinationQuery: String) {
        if (destinationQuery.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingRoute = true, errorMessage = null) }

            val searchResult = directionsRepository.searchLocation(destinationQuery)
            searchResult.onSuccess { (destLatLng, destName) ->
                val destLocation = LocationData(
                    latitude = destLatLng.latitude,
                    longitude = destLatLng.longitude,
                    name = destName,
                    city = destName,
                    isCurrentLocation = false
                )
                _uiState.update { it.copy(destinationLocation = destLocation) }

                // Fetch route
                val current = _uiState.value.currentLocation
                val originLatLng = LatLng(current.latitude, current.longitude)
                val routeResult = directionsRepository.getRoute(
                    origin = originLatLng,
                    destination = destLatLng,
                    originName = current.city,
                    destinationName = destName
                )

                routeResult.onSuccess { route ->
                    _uiState.update { it.copy(activeRoute = route, isLoadingRoute = false) }
                }.onFailure {
                    _uiState.update { it.copy(isLoadingRoute = false, errorMessage = "Failed to calculate route") }
                }

                // Fetch weather for destination
                val destWeatherResult = weatherRepository.getWeatherForLocation(destLatLng.latitude, destLatLng.longitude)
                destWeatherResult.onSuccess { destWeather ->
                    updateWeatherAndMusicRecommendation(destWeather)
                }

            }.onFailure {
                _uiState.update { it.copy(isLoadingRoute = false, errorMessage = "Location not found: $destinationQuery") }
            }
        }
    }

    fun useCurrentLocationAsOrigin() {
        viewModelScope.launch {
            val loc = locationManager.fetchCurrentLocation()
            _uiState.update { it.copy(currentLocation = loc) }
        }
    }

    fun clearRoute() {
        _uiState.update {
            it.copy(
                destinationLocation = null,
                activeRoute = null,
                destinationWeather = null
            )
        }
        // Reset music back to current weather
        _uiState.value.currentWeather?.let { updateWeatherAndMusicRecommendation(it) }
    }

    private fun updateWeatherAndMusicRecommendation(weather: WeatherInfo) {
        val recommended = musicRepository.getRecommendedTrackForWeather(weather.category)
        val playlist = musicRepository.getPlaylistForWeather(weather.category)

        playerManager.setPlaylist(playlist)

        _uiState.update {
            it.copy(
                destinationWeather = weather,
                recommendedTrack = recommended,
                playlist = playlist
            )
        }

        // Auto-play if enabled and confirmed by user interaction
        if (_uiState.value.autoMusicEnabled && _uiState.value.isAutoPlayConfirmed) {
            playerManager.playTrack(recommended, forcePlay = true)
        } else {
            playerManager.playTrack(recommended, forcePlay = false)
        }
    }

    fun togglePlayPause() {
        val pState = playerState.value
        val track = uiState.value.recommendedTrack

        _uiState.update { it.copy(isAutoPlayConfirmed = true) }

        if (pState.isPlaying) {
            playerManager.pause()
        } else {
            if (pState.currentTrack != null) {
                playerManager.resume()
            } else if (track != null) {
                playerManager.playTrack(track, forcePlay = true)
            }
        }
    }

    fun playTrackDirectly(track: RecommendedTrack) {
        _uiState.update {
            it.copy(
                recommendedTrack = track,
                isAutoPlayConfirmed = true
            )
        }
        playerManager.playTrack(track, forcePlay = true)
    }

    fun toggleAutoMusic(enabled: Boolean) {
        _uiState.update { it.copy(autoMusicEnabled = enabled) }
        playerManager.setAutoMusic(enabled)
    }

    fun toggleTemperatureUnit() {
        val nextUnit = if (_uiState.value.temperatureUnit == "°C") "°F" else "°C"
        _uiState.update { it.copy(temperatureUnit = nextUnit) }
    }

    fun openSettings() {
        _uiState.update { it.copy(isSettingsOpen = true) }
    }

    fun closeSettings() {
        _uiState.update { it.copy(isSettingsOpen = false) }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }
}
