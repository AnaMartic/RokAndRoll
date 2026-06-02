package com.orwima.rokandroll.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.orwima.rokandroll.ui.components.BottomNavigationBar
import com.orwima.rokandroll.ui.screens.CalendarScreen
import com.orwima.rokandroll.ui.screens.EarningsScreen
import com.orwima.rokandroll.ui.screens.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen()
            }

            composable(Screen.Calendar.route) {
                CalendarScreen()
            }

            composable(Screen.Earnings.route) {
                EarningsScreen()
            }
        }
    }
}