package com.orwima.rokandroll

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.orwima.rokandroll.navigation.AppNavigation
import com.orwima.rokandroll.ui.theme.RokAndRollTheme
import com.orwima.rokandroll.notifications.ContractNotificationScheduler

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        ContractNotificationScheduler(this).scheduleNextContractReminder()
        setContent {
            RokAndRollTheme {
                AppNavigation()
            }
        }
    }
}