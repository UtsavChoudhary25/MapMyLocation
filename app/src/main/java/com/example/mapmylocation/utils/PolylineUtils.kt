package com.example.mapmylocation.utils

import com.google.android.gms.maps.model.LatLng
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object PolylineUtils {

    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5)
            poly.add(p)
        }
        return poly
    }

    /**
     * Calculates distance in kilometers between two coordinates (Haversine formula)
     */
    fun calculateDistanceKm(start: LatLng, end: LatLng): Double {
        val r = 6371.0 // Earth radius in km
        val dLat = Math.toRadians(end.latitude - start.latitude)
        val dLng = Math.toRadians(end.longitude - start.longitude)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(start.latitude)) * cos(Math.toRadians(end.latitude)) *
                sin(dLng / 2) * sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }

    /**
     * Estimates travel duration in minutes based on distance and average driving speed (45 km/h)
     */
    fun estimateDurationMinutes(distanceKm: Double): Int {
        val speedKmh = 45.0
        val hours = distanceKm / speedKmh
        return (hours * 60).toInt().coerceAtLeast(1)
    }

    /**
     * Generates a curved realistic polyline path between start and end for offline/fallback routes
     */
    fun generateSmoothRoute(start: LatLng, end: LatLng): List<LatLng> {
        val points = mutableListOf<LatLng>()
        points.add(start)

        val steps = 20
        val dLat = (end.latitude - start.latitude) / steps
        val dLng = (end.longitude - start.longitude) / steps

        for (i in 1 until steps) {
            val progress = i.toDouble() / steps
            val currentLat = start.latitude + dLat * i
            val currentLng = start.longitude + dLng * i

            // Add slight sine wave curvature to mimic real road curvature
            val offset = sin(progress * Math.PI) * 0.005
            val perpLat = -dLng * offset * 2.0
            val perpLng = dLat * offset * 2.0

            points.add(LatLng(currentLat + perpLat, currentLng + perpLng))
        }

        points.add(end)
        return points
    }
}
