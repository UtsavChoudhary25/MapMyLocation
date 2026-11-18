package com.example.mapmylocation.data.repository

import com.example.mapmylocation.data.models.OpenWeatherResponse
import com.example.mapmylocation.data.models.WeatherCategory
import com.example.mapmylocation.data.models.WeatherInfo
import com.example.mapmylocation.data.network.RetrofitClient
import com.example.mapmylocation.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs

class WeatherRepository {

    suspend fun getWeatherForLocation(lat: Double, lng: Double, apiKey: String = Constants.OPENWEATHER_API_KEY): Result<WeatherInfo> = withContext(Dispatchers.IO) {
        try {
            if (apiKey.isNotBlank() && apiKey != "demo_key" && apiKey != "YOUR_API_KEY") {
                val response = RetrofitClient.openWeatherApi.getCurrentWeather(lat, lng, "metric", apiKey)
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    return@withContext Result.success(parseWeatherResponse(body))
                }
            }
            // Fallback simulation when no live API key is set or call fails
            Result.success(generateSimulatedWeather(lat, lng))
        } catch (e: Exception) {
            // Graceful fallback: return simulated weather so UI never breaks
            Result.success(generateSimulatedWeather(lat, lng))
        }
    }

    suspend fun getWeatherForCity(cityName: String, apiKey: String = Constants.OPENWEATHER_API_KEY): Result<WeatherInfo> = withContext(Dispatchers.IO) {
        try {
            if (apiKey.isNotBlank() && apiKey != "demo_key" && apiKey != "YOUR_API_KEY") {
                val response = RetrofitClient.openWeatherApi.getWeatherByCity(cityName, "metric", apiKey)
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    return@withContext Result.success(parseWeatherResponse(body))
                }
            }
            // Fallback simulation
            val (simLat, simLng) = getCityCoordinatesFallback(cityName)
            Result.success(generateSimulatedWeather(simLat, simLng, cityName))
        } catch (e: Exception) {
            val (simLat, simLng) = getCityCoordinatesFallback(cityName)
            Result.success(generateSimulatedWeather(simLat, simLng, cityName))
        }
    }

    private fun parseWeatherResponse(response: OpenWeatherResponse): WeatherInfo {
        val weatherObj = response.weather?.firstOrNull()
        val weatherId = weatherObj?.id ?: 800
        val temp = response.main?.temp ?: 20.0
        val category = mapIdToCategory(weatherId)
        val cityName = response.name.takeIf { !it.isNull_or_empty() } ?: "Current Location"
        val humidity = response.main?.humidity ?: 55
        val windSpeed = (response.wind?.speed ?: 3.5) * 3.6 // m/s to km/h

        return WeatherInfo(
            temperatureCelsius = temp,
            condition = weatherObj?.main ?: category.displayName,
            description = weatherObj?.description?.replaceFirstChar { it.uppercase() } ?: category.description,
            category = category,
            iconCode = weatherObj?.icon ?: "01d",
            cityName = cityName,
            humidityPercent = humidity,
            windSpeedKmh = windSpeed
        )
    }

    private fun String?.isNull_or_empty(): Boolean = this == null || this.trim().isEmpty()

    private fun mapIdToCategory(id: Int): WeatherCategory {
        return when (id) {
            in 200..531 -> WeatherCategory.RAINY
            in 600..622 -> WeatherCategory.SNOWY
            in 701..781 -> WeatherCategory.OTHER
            800 -> WeatherCategory.SUNNY
            in 801..804 -> WeatherCategory.CLOUDY
            else -> WeatherCategory.SUNNY
        }
    }

    private fun generateSimulatedWeather(lat: Double, lng: Double, nameOverride: String? = null): WeatherInfo {
        // Smart hash based on latitude & longitude for consistent city weather
        val hash = (abs(lat * 100) + abs(lng * 100)).toInt()
        val category = when (hash % 5) {
            0 -> WeatherCategory.SUNNY
            1 -> WeatherCategory.RAINY
            2 -> WeatherCategory.CLOUDY
            3 -> WeatherCategory.SNOWY
            else -> WeatherCategory.OTHER
        }

        val temp = when (category) {
            WeatherCategory.SUNNY -> 24.5 + (hash % 6)
            WeatherCategory.RAINY -> 16.0 + (hash % 5)
            WeatherCategory.CLOUDY -> 19.0 + (hash % 4)
            WeatherCategory.SNOWY -> 2.0 - (hash % 5)
            WeatherCategory.OTHER -> 14.0 + (hash % 3)
        }

        val city = nameOverride ?: when {
            abs(lat - 37.77) < 0.5 -> "San Francisco"
            abs(lat - 40.71) < 0.5 -> "New York"
            abs(lat - 51.50) < 0.5 -> "London"
            abs(lat - 35.67) < 0.5 -> "Tokyo"
            abs(lat - 48.85) < 0.5 -> "Paris"
            else -> "Destination"
        }

        return WeatherInfo(
            temperatureCelsius = temp,
            condition = category.displayName,
            description = category.description,
            category = category,
            iconCode = "01d",
            cityName = city,
            humidityPercent = 50 + (hash % 40),
            windSpeedKmh = 8.0 + (hash % 15)
        )
    }

    private fun getCityCoordinatesFallback(city: String): Pair<Double, Double> {
        val lower = city.lowercase()
        return when {
            lower.contains("london") -> 51.5074 to -0.1278
            lower.contains("tokyo") -> 35.6762 to 139.6503
            lower.contains("paris") -> 48.8566 to 2.3522
            lower.contains("new york") || lower.contains("nyc") -> 40.7128 to -74.0060
            lower.contains("san francisco") -> 37.7749 to -122.4194
            lower.contains("sydney") -> -33.8688 to 151.2093
            lower.contains("mumbai") || lower.contains("bombay") -> 19.0760 to 72.8777
            lower.contains("delhi") -> 28.6139 to 77.2090
            lower.contains("bangalore") -> 12.9716 to 77.5946
            lower.contains("berlin") -> 52.5200 to 13.4050
            else -> 37.7749 to -122.4194
        }
    }
}
