package com.orwima.rokandroll.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Calendar : Screen("calendar")
    data object Earnings : Screen("earnings")
    data object AddTask : Screen("add_task")
}