package com.example.mapmylocation.data.network

import com.example.mapmylocation.data.models.GoogleDirectionsResponse
import com.example.mapmylocation.data.models.GoogleGeocodingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleDirectionsApi {

    @GET("maps/api/directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String
    ): Response<GoogleDirectionsResponse>

    @GET("maps/api/geocode/json")
    suspend fun geocodeAddress(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): Response<GoogleGeocodingResponse>
}
