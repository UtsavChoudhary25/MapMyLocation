package com.example.mapmylocation.utils

object Constants {
    const val OPENWEATHER_BASE_URL = "https://api.openweathermap.org/"
    const val GOOGLE_MAPS_BASE_URL = "https://maps.googleapis.com/"

    // Keys can be configured in local.properties or overridden in settings
    var OPENWEATHER_API_KEY = "YOUR_OPENWEATHER_API_KEY" 
    var GOOGLE_MAPS_API_KEY = "YOUR_GOOGLE_MAPS_API_KEY"

    // Default Fallback Locations
    const val DEFAULT_LAT = 37.7749 // San Francisco / Standard Location
    const val DEFAULT_LNG = -122.4194
    const val DEFAULT_CITY = "San Francisco"
}
