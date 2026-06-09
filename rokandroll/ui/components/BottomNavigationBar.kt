package com.orwima.rokandroll.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.orwima.rokandroll.navigation.Screen

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        modifier = Modifier.clip(
            RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp
            )
        ),
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(Screen.Calendar.route) },
            icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Kalendar") },
            label = { Text("Kalendar") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(Screen.Home.route) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(Screen.Earnings.route) },
            icon = { Icon(Icons.Default.Paid, contentDescription = "Zarada") },
            label = { Text("Zarada") }
        )
    }
}