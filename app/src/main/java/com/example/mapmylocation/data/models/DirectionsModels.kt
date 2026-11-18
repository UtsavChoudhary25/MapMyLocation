package com.example.mapmylocation.data.models

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

data class RouteInfo(
    val originName: String,
    val originLatLng: LatLng,
    val destinationName: String,
    val destinationLatLng: LatLng,
    val distanceText: String,
    val distanceKm: Double,
    val durationText: String,
    val durationMinutes: Int,
    val polylinePoints: List<LatLng>
)

// Retrofit API response models for Google Directions API
data class GoogleDirectionsResponse(
    @SerializedName("routes") val routes: List<GoogleDirectionsRoute>?,
    @SerializedName("status") val status: String?
)

data class GoogleDirectionsRoute(
    @SerializedName("legs") val legs: List<GoogleDirectionsLeg>?,
    @SerializedName("overview_polyline") val overviewPolyline: GoogleOverviewPolyline?
)

data class GoogleDirectionsLeg(
    @SerializedName("distance") val distance: GoogleTextValue?,
    @SerializedName("duration") val duration: GoogleTextValue?,
    @SerializedName("start_address") val startAddress: String?,
    @SerializedName("end_address") val endAddress: String?,
    @SerializedName("start_location") val startLocation: GoogleLatLng?,
    @SerializedName("end_location") val endLocation: GoogleLatLng?
)

data class GoogleTextValue(
    @SerializedName("text") val text: String,
    @SerializedName("value") val value: Int
)

data class GoogleOverviewPolyline(
    @SerializedName("points") val points: String
)

data class GoogleLatLng(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
)

// Geocoding API response
data class GoogleGeocodingResponse(
    @SerializedName("results") val results: List<GoogleGeocodingResult>?,
    @SerializedName("status") val status: String?
)

data class GoogleGeocodingResult(
    @SerializedName("formatted_address") val formattedAddress: String,
    @SerializedName("geometry") val geometry: GoogleGeocodingGeometry
)

data class GoogleGeocodingGeometry(
    @SerializedName("location") val location: GoogleLatLng
)
