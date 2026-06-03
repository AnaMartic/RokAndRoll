package com.orwima.rokandroll.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object Calendar : Screen("calendar")
    data object Earnings : Screen("earnings")
    data object AddTask : Screen("add_task")
    data object AddShift: Screen("add_shift")
}