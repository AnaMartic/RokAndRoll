package com.orwima.rokandroll.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.orwima.rokandroll.navigation.Screen

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
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