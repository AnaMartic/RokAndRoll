package com.orwima.rokandroll.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.orwima.rokandroll.ui.components.BottomNavigationBar
import com.orwima.rokandroll.ui.screens.AddTaskScreen
import com.orwima.rokandroll.ui.screens.CalendarScreen
import com.orwima.rokandroll.ui.screens.EarningsScreen
import com.orwima.rokandroll.ui.screens.HomeScreen
import com.orwima.rokandroll.ui.screens.LoginScreen
import com.orwima.rokandroll.ui.screens.RegisterScreen
import com.orwima.rokandroll.viewmodel.AuthViewModel
import com.orwima.rokandroll.ui.screens.AddShiftScreen
import com.orwima.rokandroll.ui.screens.ProfileScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.ui.graphics.Color

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    val startDestination = if (authViewModel.isUserLoggedIn()) {
        Screen.Home.route
    } else {
        Screen.Login.route
    }

    Scaffold(
        containerColor = Color(0xFF1B2243),
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(navController = navController)
            }

            composable(Screen.Register.route) {
                RegisterScreen(navController = navController)
            }

            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }

            composable(Screen.Calendar.route) {
                CalendarScreen(navController = navController)
            }

            composable(Screen.Earnings.route) {
                EarningsScreen(navController = navController)
            }

            composable(Screen.AddTask.route) {
                AddTaskScreen(navController = navController)
            }

            composable(Screen.AddShift.route) {
                AddShiftScreen(navController = navController)
            }

            composable(Screen.Profile.route) {
                ProfileScreen(navController = navController)
            }

            composable(
                route = Screen.EditTask.route,
                arguments = listOf(
                    navArgument("taskId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
                AddTaskScreen(
                    navController = navController,
                    taskId = taskId
                )
            }
        }
    }
}