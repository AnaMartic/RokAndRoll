package com.orwima.rokandroll.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.orwima.rokandroll.ui.screens.CalendarScreen
import com.orwima.rokandroll.ui.screens.EarningsScreen
import com.orwima.rokandroll.ui.screens.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
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