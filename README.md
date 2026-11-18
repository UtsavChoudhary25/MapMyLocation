# 🗺️ MapMyLocation

> **Premium Weather-Based Route & Music Android Application**

![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-7F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4.svg?style=for-the-badge&logo=android&logoColor=white)
![Google Maps](https://img.shields.io/badge/Google%20Maps-SDK%20%26%20Compose-4285F4.svg?style=for-the-badge&logo=google-maps&logoColor=white)
![OpenWeather](https://img.shields.io/badge/OpenWeather-API-EB6E4B.svg?style=for-the-badge&logo=openweathermap&logoColor=white)
![ExoPlayer](https://img.shields.io/badge/AndroidX-Media3%20ExoPlayer-3DDC84.svg?style=for-the-badge&logo=android&logoColor=white)
![Architecture](https://img.shields.io/badge/Architecture-Clean%20MVVM-009688.svg?style=for-the-badge)

**MapMyLocation** is an aesthetically pleasing, modern Android application built with Kotlin and Jetpack Compose. Designed as a commercial-grade travel companion, it seamlessly integrates real-time location tracking, interactive Google Maps navigation, OpenWeatherMap forecasting, and a rule-based **Weather-to-Music Recommendation Engine** powered by AndroidX Media3 ExoPlayer.

---

## 🎨 Design System

MapMyLocation follows a warm, minimalist travel design language inspired by **Apple Maps**, **Google Maps**, and **Spotify**:

- 🍦 **Background:** Warm Ivory (`#F7F5EF`)
- 🌲 **Primary Actions:** Forest Green (`#244C3A`)
- 🌿 **Secondary Elements:** Sage Green (`#A9BBA7` / `#E2E9E1`)
- 🧱 **Accents:** Terracotta (`#C98265`)
- ✒️ **Typography:** Charcoal Text (`#252923`) & Muted Gray (`#85877F`)
- ⬜ **Cards & Borders:** Clean Card White (`#FFFFFF`) with subtle border contours (`#E8E6DE`)

---

## 🚀 Key Features

### 1. 🏠 Smart Dashboard (Home Screen)
- **Dynamic Header:** Greeting based on time of day, current city detector, branding, and instant settings modal.
- **Destination Search:** Instant location search field with query clearing and single-tap *"Use my current location"*.
- **Live Weather Summary:** Temperature in °C or °F, weather condition badges, city, humidity percentage, and wind speed.
- **Journey Tracker:** Active distance and travel duration summary when a route is planned.
- **Soundtrack Preview:** Weather-matched music track recommendation with album artwork and functional play/pause control.

### 2. 🗺️ Interactive Maps & Navigation (Explore Screen)
- **Google Maps Compose Integration:** Smooth map view with custom markers for origin and destination.
- **Forest Green Polyline:** Custom-styled route polyline connecting travel origin and destination.
- **Auto-Camera Framing:** Automatic camera animation to smoothly fit origin and destination bounds.
- **Floating Route Sheet:** Bottom sheet displaying starting location, destination, distance, duration, destination weather, *"Start Journey"*, and *"Clear Route"*.

### 3. 🎵 Weather-Based Music Engine & Player (Music Screen)
- **Rule-Based Recommendation Engine:**
  | Weather Condition | Playlist Vibe | Track Titles |
  | :--- | :--- | :--- |
  | **SUNNY** ☀️ | Energetic Upbeat Instrumental | *Golden Hour Journey*, *Sunlit Highway* |
  | **RAINY** 🌧️ | Lo-Fi Beats & Soft Piano | *Raindrops on Glass*, *Cozy Shelter* |
  | **CLOUDY** ⛅ | Acoustic Chill Instrumental | *Cloud Drift* |
  | **SNOWY** ❄️ | Peaceful Piano & Winter Melodies | *Winter Piano Reverie* |
  | **OTHER** 🌫️ | Atmospheric Ambient Instrumental | *Misty Valley* |
- **Full Media3 ExoPlayer Player:** Functional Previous, Play, Pause, Resume, Next/Skip, Stop, and seek progress slider.
- **Playlist Management:** Interactive track selection menu and Auto-Music switch.

### 4. ⚙️ App Settings
- Temperature unit preference (°C / °F).
- Auto-Music playlist switch.
- Location service diagnostic indicator.

---

## 🛠️ Technology Stack & Architecture

```
com.example.mapmylocation
├── data/
│   ├── models/           # LocationData, WeatherModels, DirectionsModels, MusicModels
│   ├── network/          # OpenWeatherApi, GoogleDirectionsApi, RetrofitClient
│   └── repository/       # WeatherRepository, DirectionsRepository, MusicRepository
├── location/             # LocationManager (FusedLocationProviderClient wrapper)
├── player/               # AudioPlayerManager (AndroidX Media3 ExoPlayer engine)
├── ui/
│   ├── components/       # MapBottomNavBar, DestinationSearchBar, WeatherCard, MusicRecommendationCard, RouteSummarySheet
│   ├── screens/          # HomeScreen, ExploreScreen, MusicScreen, SettingsScreen
│   └── theme/            # Color, Type, Theme
├── utils/                # Constants, PolylineUtils
└── viewmodel/            # AppViewModel & AppUiState
```

---

## 🔑 API Setup & Configuration

1. **Google Maps SDK & Directions API:**
   Set your API key in `local.properties`:
   ```properties
   MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY
   ```
   Or in `app/src/main/AndroidManifest.xml`:
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="YOUR_GOOGLE_MAPS_API_KEY" />
   ```

2. **OpenWeatherMap API:**
   Set your API key in `com.example.mapmylocation.utils.Constants.kt`:
   ```kotlin
   var OPENWEATHER_API_KEY = "YOUR_OPENWEATHER_API_KEY"
   ```

*Note: The app includes intelligent fallback simulations for both Weather and Directions APIs so all features run smoothly out-of-the-box even without live API keys.*

---

## 🏃 Building and Running

1. Open **Android Studio** (2024.1+ / Ladybug or newer).
2. Select **File -> Open** and navigate to `Mapmylocation2`.
3. Wait for Gradle Sync to complete.
4. Run the project on an Android Emulator or connected device (Android 7.0 / API 24+).

---

## 📄 License
This project is open-source and available under the [MIT License](LICENSE).
