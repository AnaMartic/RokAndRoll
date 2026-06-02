package com.orwima.rokandroll

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.orwima.rokandroll.navigation.AppNavigation
import com.orwima.rokandroll.ui.theme.RokAndRollTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            RokAndRollTheme {
                AppNavigation()
            }
        }
    }
}