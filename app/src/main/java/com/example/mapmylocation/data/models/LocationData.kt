package com.example.mapmylocation.data.models

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val name: String = "Current Location",
    val city: String = "Unknown City",
    val isCurrentLocation: Boolean = true
)
