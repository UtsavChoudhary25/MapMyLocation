package com.example.mapmylocation.data.models

import com.google.gson.annotations.SerializedName

enum class WeatherCategory(val displayName: String, val description: String) {
    SUNNY("Sunny", "Clear skies and bright sunlight"),
    RAINY("Rainy", "Rain, drizzle, or thunderstorms"),
    CLOUDY("Cloudy", "Overcast skies and gentle clouds"),
    SNOWY("Snowy", "Crisp snow and freezing conditions"),
    OTHER("Misty", "Fog, mist, or atmospheric haze")
}

data class WeatherInfo(
    val temperatureCelsius: Double,
    val condition: String,
    val description: String,
    val category: WeatherCategory,
    val iconCode: String,
    val cityName: String,
    val humidityPercent: Int,
    val windSpeedKmh: Double
)

// Retrofit API Models for OpenWeatherMap
data class OpenWeatherResponse(
    @SerializedName("coord") val coord: CoordResponse?,
    @SerializedName("weather") val weather: List<WeatherDescriptionResponse>?,
    @SerializedName("main") val main: MainWeatherResponse?,
    @SerializedName("wind") val wind: WindResponse?,
    @SerializedName("name") val name: String?
)

data class CoordResponse(
    @SerializedName("lon") val lon: Double,
    @SerializedName("lat") val lat: Double
)

data class WeatherDescriptionResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class MainWeatherResponse(
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("humidity") val humidity: Int
)

data class WindResponse(
    @SerializedName("speed") val speed: Double
)
