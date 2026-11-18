package com.example.mapmylocation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mapmylocation.data.models.WeatherCategory
import com.example.mapmylocation.data.models.WeatherInfo
import com.example.mapmylocation.ui.theme.BorderColor
import com.example.mapmylocation.ui.theme.CardWhite
import com.example.mapmylocation.ui.theme.CharcoalText
import com.example.mapmylocation.ui.theme.CloudyGray
import com.example.mapmylocation.ui.theme.ForestGreen
import com.example.mapmylocation.ui.theme.MutedText
import com.example.mapmylocation.ui.theme.RainyBlue
import com.example.mapmylocation.ui.theme.SageGreenLight
import com.example.mapmylocation.ui.theme.SnowyWhite
import com.example.mapmylocation.ui.theme.SunnyYellow

@Composable
fun WeatherCard(
    weatherInfo: WeatherInfo,
    temperatureUnit: String = "°C",
    titlePrefix: String = "Current Weather",
    modifier: Modifier = Modifier
) {
    val tempDisplay = if (temperatureUnit == "°F") {
        val fahrenheit = (weatherInfo.temperatureCelsius * 9 / 5) + 32
        "${fahrenheit.toInt()}°F"
    } else {
        "${weatherInfo.temperatureCelsius.toInt()}°C"
    }

    val (icon, accentColor) = when (weatherInfo.category) {
        WeatherCategory.SUNNY -> Icons.Default.WbSunny to SunnyYellow
        WeatherCategory.RAINY -> Icons.Default.Grain to RainyBlue
        WeatherCategory.CLOUDY -> Icons.Default.Cloud to CloudyGray
        WeatherCategory.SNOWY -> Icons.Default.AcUnit to SnowyWhite
        WeatherCategory.OTHER -> Icons.Default.Air to ForestGreen
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = CardWhite,
        shadowElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = ForestGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$titlePrefix: ${weatherInfo.cityName}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MutedText
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = weatherInfo.category.displayName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = tempDisplay,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalText
                    )
                    Text(
                        text = weatherInfo.description,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = CharcoalText
                    )
                }

                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = weatherInfo.condition,
                        tint = accentColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SageGreenLight.copy(alpha = 0.5f))
                    .padding(vertical = 10.dp, horizontal = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = "Humidity",
                        tint = ForestGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Humidity ${weatherInfo.humidityPercent}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = CharcoalText
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Air,
                        contentDescription = "Wind speed",
                        tint = ForestGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Wind ${weatherInfo.windSpeedKmh.toInt()} km/h",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = CharcoalText
                    )
                }
            }
        }
    }
}
