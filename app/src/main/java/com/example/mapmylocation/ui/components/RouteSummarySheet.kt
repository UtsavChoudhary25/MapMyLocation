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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mapmylocation.data.models.RouteInfo
import com.example.mapmylocation.data.models.WeatherInfo
import com.example.mapmylocation.ui.theme.BorderColor
import com.example.mapmylocation.ui.theme.CardWhite
import com.example.mapmylocation.ui.theme.CharcoalText
import com.example.mapmylocation.ui.theme.ForestGreen
import com.example.mapmylocation.ui.theme.MutedText
import com.example.mapmylocation.ui.theme.SageGreenLight
import com.example.mapmylocation.ui.theme.Terracotta

@Composable
fun RouteSummarySheet(
    routeInfo: RouteInfo,
    destinationWeather: WeatherInfo? = null,
    onViewRouteClick: () -> Unit,
    onClearRouteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = CardWhite,
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Drag handle indicator
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(BorderColor)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "YOUR JOURNEY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreen,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Origin -> Destination row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "Origin",
                    tint = ForestGreen,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "From: ${routeInfo.originName}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = CharcoalText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Navigation,
                    contentDescription = "Destination",
                    tint = Terracotta,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "To: ${routeInfo.destinationName}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CharcoalText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stats grid (Distance, Duration, Weather)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SageGreenLight.copy(alpha = 0.5f))
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = "Distance",
                            tint = ForestGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Distance",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MutedText
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = routeInfo.distanceText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalText
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Duration",
                            tint = ForestGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Duration",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MutedText
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = routeInfo.durationText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalText
                    )
                }

                if (destinationWeather != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Dest Weather",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MutedText
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${destinationWeather.temperatureCelsius.toInt()}°C • ${destinationWeather.category.displayName}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Actions row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onClearRouteClick() },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = MutedText,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Clear Route", color = CharcoalText, fontSize = 13.sp)
                }

                Button(
                    onClick = { onViewRouteClick() },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = "Start",
                        tint = CardWhite,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "View Route", color = CardWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
