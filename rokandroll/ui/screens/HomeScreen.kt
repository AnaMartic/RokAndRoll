package com.orwima.rokandroll.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.orwima.rokandroll.data.model.Task
import com.orwima.rokandroll.navigation.Screen
import com.orwima.rokandroll.viewmodel.TaskViewModel
import com.orwima.rokandroll.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    taskViewModel: TaskViewModel = viewModel()
) {
    val user by userViewModel.user.collectAsState()
    val tasks by taskViewModel.tasks.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.loadCurrentUser()
        taskViewModel.loadTasks()
    }

    val displayName = user?.name?.takeIf { it.isNotBlank() } ?: ""

    val now = Date()

    val nextTask = tasks
        .filter { it.type != "Smjena" }
        .mapNotNull { task ->
            parseTaskDateTime(task)?.let { date ->
                task to date
            }
        }
        .filter { it.second.after(now) }
        .minByOrNull { it.second.time }
        ?.first

    val nextShift = tasks
        .filter { it.type == "Smjena" }
        .mapNotNull { task ->
            parseTaskDateTime(task)?.let { date ->
                task to date
            }
        }
        .filter { it.second.after(now) }
        .minByOrNull { it.second.time }
        ?.first

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F2FA))
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (displayName.isNotBlank()) "Bok, $displayName 👋" else "Bok 👋",
                    fontSize = 28.sp,
                    color = Color(0xFF2B2B2B)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Pregled današnjih obaveza",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }

            IconButton(
                onClick = {
                    navController.navigate(Screen.Profile.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Postavke",
                    tint = Color(0xFF6750A4)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        InfoCard(
            title = "Sljedeća obaveza",
            subtitle = nextTask?.let {
                "${it.title} • ${it.date} • ${it.startTime} - ${it.endTime}"
            } ?: "Nema spremljenih obaveza",
            icon = Icons.Default.Event
        )

        Spacer(modifier = Modifier.height(14.dp))

        InfoCard(
            title = "Sljedeća smjena",
            subtitle = nextShift?.let {
                "${it.date} • ${it.startTime} - ${it.endTime}"
            } ?: "Nema spremljenih smjena",
            icon = Icons.Default.Work
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            SmallInfoCard(
                title = "Koraci",
                subtitle = "3000",
                icon = Icons.Default.DirectionsWalk,
                modifier = Modifier.weight(1f)
            )

            SmallInfoCard(
                title = "Vrijeme",
                subtitle = "24°C",
                icon = Icons.Default.Cloud,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    subtitle: String,
    icon: ImageVector
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEADDFF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF6750A4)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Text(
                    text = subtitle,
                    fontSize = 16.sp,
                    color = Color(0xFF2B2B2B)
                )
            }
        }
    }
}

@Composable
fun SmallInfoCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(130.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF6750A4)
            )

            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Text(
                    text = subtitle,
                    fontSize = 22.sp,
                    color = Color(0xFF2B2B2B)
                )
            }
        }
    }
}

fun parseTaskDateTime(task: Task): Date? {
    return try {
        val formatter = SimpleDateFormat("dd.MM.yyyy. HH:mm", Locale.getDefault())
        formatter.parse("${task.date} ${task.startTime}")
    } catch (e: Exception) {
        null
    }
}