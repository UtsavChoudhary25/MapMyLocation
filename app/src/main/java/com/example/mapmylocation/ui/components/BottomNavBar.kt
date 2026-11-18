package com.example.mapmylocation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mapmylocation.ui.theme.BorderColor
import com.example.mapmylocation.ui.theme.CardWhite
import com.example.mapmylocation.ui.theme.CharcoalText
import com.example.mapmylocation.ui.theme.ForestGreen
import com.example.mapmylocation.ui.theme.MutedText
import com.example.mapmylocation.ui.theme.WarmIvory

enum class NavDestination(val route: String, val title: String, val icon: ImageVector) {
    HOME("home", "Home", Icons.Default.Home),
    EXPLORE("explore", "Explore", Icons.Default.Explore),
    MUSIC("music", "Music", Icons.Default.MusicNote)
}

@Composable
fun MapBottomNavBar(
    currentRoute: String,
    onNavigate: (NavDestination) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        shape = RoundedCornerShape(24.dp),
        color = CardWhite,
        shadowElevation = 4.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavDestination.entries.forEach { destination ->
                val isSelected = currentRoute == destination.route

                val iconColor by animateColorAsState(
                    targetValue = if (isSelected) ForestGreen else MutedText,
                    animationSpec = tween(300),
                    label = "iconColor"
                )

                val backgroundColor by animateColorAsState(
                    targetValue = if (isSelected) WarmIvory else CardWhite,
                    animationSpec = tween(300),
                    label = "bgColor"
                )

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(backgroundColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onNavigate(destination) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.title,
                        tint = iconColor,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = destination.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = if (isSelected) CharcoalText else MutedText,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}
