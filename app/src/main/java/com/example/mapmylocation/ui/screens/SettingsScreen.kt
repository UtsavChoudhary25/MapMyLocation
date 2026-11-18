package com.example.mapmylocation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mapmylocation.ui.theme.BorderColor
import com.example.mapmylocation.ui.theme.CardWhite
import com.example.mapmylocation.ui.theme.CharcoalText
import com.example.mapmylocation.ui.theme.ForestGreen
import com.example.mapmylocation.ui.theme.MutedText
import com.example.mapmylocation.ui.theme.SageGreenLight
import com.example.mapmylocation.viewmodel.AppUiState

@Composable
fun SettingsDialog(
    uiState: AppUiState,
    onDismiss: () -> Unit,
    onToggleAutoMusic: (Boolean) -> Unit,
    onToggleTempUnit: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = CardWhite,
            shadowElevation = 8.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "App Settings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalText
                    )

                    IconButton(onClick = { onDismiss() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MutedText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Auto Music Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = "Auto Music",
                            tint = ForestGreen
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Auto Music",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CharcoalText
                            )
                            Text(
                                text = "Match playlist to weather",
                                fontSize = 12.sp,
                                color = MutedText
                            )
                        }
                    }

                    Switch(
                        checked = uiState.autoMusicEnabled,
                        onCheckedChange = { onToggleAutoMusic(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CardWhite,
                            checkedTrackColor = ForestGreen
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Temperature Unit Preference
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Thermostat,
                            contentDescription = "Temperature Unit",
                            tint = ForestGreen
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Temperature Unit",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CharcoalText
                            )
                            Text(
                                text = "Currently: ${uiState.temperatureUnit}",
                                fontSize = 12.sp,
                                color = MutedText
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = { onToggleTempUnit() },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = if (uiState.temperatureUnit == "°C") "Switch to °F" else "Switch to °C",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Location Status
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = SageGreenLight.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = ForestGreen
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Location Service",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CharcoalText
                            )
                            Text(
                                text = "Current: ${uiState.currentLocation.city}",
                                fontSize = 11.sp,
                                color = MutedText
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // About Section
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "About",
                        tint = MutedText
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "MapMyLocation v1.0 • Premium Travel Companion",
                        fontSize = 12.sp,
                        color = MutedText
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Text(text = "Close Settings", color = CardWhite, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
