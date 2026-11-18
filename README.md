# MapMyLocation — Premium Weather-Based Route & Music Android Application

**MapMyLocation** is an aesthetically pleasing, modern Android application built using Kotlin and Jetpack Compose. It seamlessly combines real-time location services, interactive Google Maps routing, OpenWeatherMap forecasting, and an intelligent weather-to-music recommendation engine powered by AndroidX Media3 ExoPlayer.

---

## 🌟 Visual & Architectural Highlights

- **Design System:** Inspired by Apple Maps, Google Maps, and Spotify. Built with a warm ivory background (`#F7F5EF`), deep forest green primary accents (`#244C3A`), sage green secondary tones (`#A9BBA7`), terracotta accents (`#C98265`), clean charcoal typography (`#252923`), and crisp card boundaries (`#E8E6DE`).
- **Architecture:** Clean MVVM (Model-View-ViewModel) architecture using Kotlin Coroutines, StateFlow, and Navigation Compose.
- **Offline & Fallback Resilience:** Smart fallback mechanisms for Google Directions and OpenWeather APIs ensure that the interactive map, polylines, distance metrics, weather details, and music recommendations function seamlessly offline or without live API keys.

---

## 🚀 Core Features

### 1. Dashboard (Home Screen)
- **Header:** Live greeting ("Good morning / afternoon / evening"), current city indicator, branding, and quick settings menu.
- **Destination Search:** Interactive search bar with instant query clearance and a "Use my current location" quick action.
- **Weather Overview:** Real-time temperature (°C or °F), condition, weather category badge, city, humidity percentage, and wind speed.
- **Active Journey Summary:** Real-time distance and travel duration summary when a route is active.
- **Smart Music Recommendation:** Dynamic weather-matched soundtrack recommendation with album art and functional play/pause control.

### 2. Interactive Navigation (Explore Screen)
- **Google Maps Integration:** Google Maps Compose with custom markers for origin and destination.
- **Forest Green Route Polyline:** Smooth, high-precision polyline rendering for travel routes.
- **Automatic Camera Animation:** Smooth map camera bounds framing origin and destination coordinates.
- **Floating Route Sheet:** Bottom sheet detailing travel distance, estimated duration, destination weather, and actions to "View Route" or "Clear Route".

### 3. Weather-Based Music Engine & Player (Music Screen)
- **Rule-Based Recommendation Engine:**
  - **SUNNY:** Energetic upbeat instrumental tracks (*Golden Hour Journey*, *Sunlit Highway*).
  - **RAINY:** Lo-Fi beats, soft piano & relaxing ambient tracks (*Raindrops on Glass*, *Cozy Shelter*).
  - **CLOUDY:** Acoustic chill instrumental melodies (*Cloud Drift*).
  - **SNOWY:** Peaceful solo piano & winter harmonies (*Winter Piano Reverie*).
  - **OTHER:** Neutral atmospheric instrumentals (*Misty Valley*).
- **Full-Featured Audio Player:** Powered by **AndroidX Media3 ExoPlayer**.
- **Playback Controls:** Previous, Play, Pause, Resume, Next/Skip, Stop, and seek slider.
- **Playlist Management:** Interactive playlist displaying all weather-curated tracks.
- **Auto Music Toggle:** Toggle automatic playlist synchronization when weather or destination changes.

### 4. Application Settings
- Temperature unit selection (°C / °F).
- Auto Music recommendation toggle.
- Location service status.
- Application metadata and version information.

---

## 🛠️ Technology Stack

| Layer | Technology |
| :--- | :--- |
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose + Material 3 |
| **Maps & Location** | Google Maps SDK for Android, Google Maps Compose, Fused Location Provider Client |
| **Networking** | Retrofit 2, OkHttp 4, Gson |
| **Audio Engine** | AndroidX Media3 ExoPlayer (`media3-exoplayer`, `media3-ui`, `media3-session`) |
| **Asynchronous Logic** | Kotlin Coroutines & StateFlow |
| **Navigation** | Navigation Compose |
| **Architecture** | Clean MVVM |

---

## 📁 Project Structure

```
com.example.mapmylocation
├── data/
│   ├── models/           # LocationData, WeatherModels, DirectionsModels, MusicModels
│   ├── network/          # OpenWeatherApi, GoogleDirectionsApi, RetrofitClient
│   └── repository/       # WeatherRepository, DirectionsRepository, MusicRepository
├── location/             # LocationManager (FusedLocationProviderClient wrapper)
├── player/               # AudioPlayerManager (AndroidX Media3 ExoPlayer integration)
├── ui/
│   ├── components/       # BottomNavBar, DestinationSearchBar, WeatherCard, MusicRecommendationCard, RouteSummarySheet
│   ├── screens/          # HomeScreen, ExploreScreen, MusicScreen, SettingsScreen
│   └── theme/            # Color, Type, Theme
├── utils/                # Constants, PolylineUtils
└── viewmodel/            # AppViewModel & AppUiState
```

---

## 🔑 API Configuration Setup

### 1. Google Maps API Key
1. Obtain an API key from the [Google Cloud Console](https://console.cloud.google.com/) with **Maps SDK for Android** and **Directions API** enabled.
2. Add your key to `local.properties`:
   ```properties
   MAPS_API_KEY=YOUR_ACTUAL_GOOGLE_MAPS_KEY
   ```
3. Or update `app/src/main/AndroidManifest.xml`:
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="YOUR_ACTUAL_GOOGLE_MAPS_KEY" />
   ```

### 2. OpenWeatherMap API Key
1. Sign up at [OpenWeatherMap](https://openweathermap.org/api) and generate an API key.
2. Set `OPENWEATHER_API_KEY` in `com.example.mapmylocation.utils.Constants.kt`.

*Note: The app includes intelligent fallback simulations for both Weather and Directions APIs so all features run smoothly out of the box even without live API keys.*

---

## 🎵 Audio Resources Setup

Local audio files are located in `app/src/main/res/raw/`:
- `sunny_track_1.wav`
- `sunny_track_2.wav`
- `rainy_track_1.wav`
- `rainy_track_2.wav`
- `cloudy_track_1.wav`
- `snowy_track_1.wav`
- `other_track_1.wav`

To add custom tracks, place `.mp3` or `.wav` files into `res/raw/` and update `com.example.mapmylocation.data.repository.MusicRepository.kt`.

---

## 🏃 Building and Running the Application

1. Open **Android Studio**.
2. Select **File -> Open** and navigate to `Mapmylocation2`.
3. Wait for Gradle Sync to complete.
4. Run the application on an Android Emulator or connected physical device (Android 7.0 / API 24+).
