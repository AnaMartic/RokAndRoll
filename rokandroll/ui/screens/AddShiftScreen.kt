package com.orwima.rokandroll.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.orwima.rokandroll.navigation.Screen
import com.orwima.rokandroll.viewmodel.ShiftViewModel

@Composable
fun AddShiftScreen(
    navController: NavController,
    shiftViewModel: ShiftViewModel = viewModel()
) {
    var date by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var hours by remember { mutableStateOf("") }
    var hourlyRate by remember { mutableStateOf("") }

    val statusMessage by shiftViewModel.statusMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F2FA))
            .padding(20.dp)
    ) {
        IconButton(onClick = { navController.navigate(Screen.Earnings.route) }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Natrag")
        }

        Text(
            text = "Dodaj smjenu",
            fontSize = 30.sp,
            color = Color(0xFF2B2B2B)
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Datum, npr. 12.06.2026.") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = startTime,
            onValueChange = { startTime = it },
            label = { Text("Početak, npr. 16:00") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = endTime,
            onValueChange = { endTime = it },
            label = { Text("Kraj, npr. 21:00") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = hours,
            onValueChange = { hours = it },
            label = { Text("Broj sati, npr. 5") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = hourlyRate,
            onValueChange = { hourlyRate = it },
            label = { Text("Satnica, npr. 7.50") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
                shiftViewModel.addShift(
                    date = date,
                    startTime = startTime,
                    endTime = endTime,
                    hoursText = hours,
                    hourlyRateText = hourlyRate
                ) {
                    navController.navigate(Screen.Earnings.route)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6750A4)
            )
        ) {
            Icon(Icons.Default.Save, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Spremi smjenu")
        }

        if (statusMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = statusMessage,
                color = Color(0xFF6750A4)
            )
        }
    }
}