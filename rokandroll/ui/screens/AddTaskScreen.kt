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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.orwima.rokandroll.data.model.Task
import com.orwima.rokandroll.navigation.Screen
import com.orwima.rokandroll.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    navController: NavController,
    taskViewModel: TaskViewModel = viewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var selectedDate by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    var selectedType by remember { mutableStateOf("Predavanje") }
    var expanded by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val taskTypes = listOf("Predavanje", "Ispit", "Kolokvij", "Smjena", "Ostalo")
    val statusMessage by taskViewModel.statusMessage.collectAsState()

    val datePickerState = rememberDatePickerState()
    val startTimePickerState = rememberTimePickerState(is24Hour = true)
    val endTimePickerState = rememberTimePickerState(is24Hour = true)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F2FA))
            .padding(20.dp)
    ) {
        IconButton(onClick = { navController.navigate(Screen.Calendar.route) }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Natrag")
        }

        Text(
            text = "Dodaj obavezu",
            fontSize = 30.sp,
            color = Color(0xFF2B2B2B)
        )

        Spacer(modifier = Modifier.height(20.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tip obaveze") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(16.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                taskTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            selectedType = type
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Naziv") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Opis") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = if (selectedDate.isBlank()) "Odaberi datum" else "Datum: $selectedDate"
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedButton(
            onClick = { showStartTimePicker = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = if (startTime.isBlank()) "Odaberi početak" else "Početak: $startTime"
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedButton(
            onClick = { showEndTimePicker = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = if (endTime.isBlank()) "Odaberi kraj" else "Kraj: $endTime"
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
                val task = Task(
                    title = title,
                    description = description,
                    date = selectedDate,
                    startTime = startTime,
                    endTime = endTime,
                    type = selectedType
                )

                taskViewModel.addTask(task) {
                    navController.navigate(Screen.Calendar.route)
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
            Text("Spremi obavezu")
        }

        if (statusMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = statusMessage, color = Color(0xFF6750A4))
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            selectedDate = formatDate(millis)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Odaberi")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Odustani")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showStartTimePicker) {
        AlertDialog(
            onDismissRequest = { showStartTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        startTime = formatTime(
                            startTimePickerState.hour,
                            startTimePickerState.minute
                        )
                        showStartTimePicker = false
                    }
                ) {
                    Text("Odaberi")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartTimePicker = false }) {
                    Text("Odustani")
                }
            },
            text = {
                TimePicker(state = startTimePickerState)
            }
        )
    }

    if (showEndTimePicker) {
        AlertDialog(
            onDismissRequest = { showEndTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        endTime = formatTime(
                            endTimePickerState.hour,
                            endTimePickerState.minute
                        )
                        showEndTimePicker = false
                    }
                ) {
                    Text("Odaberi")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndTimePicker = false }) {
                    Text("Odustani")
                }
            },
            text = {
                TimePicker(state = endTimePickerState)
            }
        )
    }
}

fun formatDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun formatTime(hour: Int, minute: Int): String {
    return "%02d:%02d".format(hour, minute)
}