package com.example.mapmylocation.data.network

import com.example.mapmylocation.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    val openWeatherApi: OpenWeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.OPENWEATHER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenWeatherApi::class.java)
    }

    val googleDirectionsApi: GoogleDirectionsApi by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.GOOGLE_MAPS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleDirectionsApi::class.java)
    }
}
