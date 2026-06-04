package com.orwima.rokandroll.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Euro
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun EarningsScreen(
    navController: NavController,
    taskViewModel: TaskViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val tasks by taskViewModel.tasks.collectAsState()
    val user by userViewModel.user.collectAsState()

    LaunchedEffect(Unit) {
        taskViewModel.loadTasks()
        userViewModel.loadCurrentUser()
    }

    val hourlyRate = user?.hourlyRate ?: 0.0

    val shifts = tasks.filter { it.type == "Smjena" }

    val totalHours = shifts.sumOf { calculateHours(it.startTime, it.endTime) }
    val totalEarnings = totalHours * hourlyRate

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F2FA))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                Text(
                    text = "Zarada",
                    fontSize = 30.sp,
                    color = Color(0xFF2B2B2B)
                )
            }

            item {
                Text(
                    text = "Pregled zarade iz smjena",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF6750A4)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp)
                    ) {
                        Text(
                            text = "Ukupno",
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "%.2f €".format(totalEarnings),
                            fontSize = 34.sp,
                            color = Color.White
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    EarningsSmallCard(
                        title = "Sati",
                        value = "%.1f h".format(totalHours),
                        icon = Icons.Default.Work,
                        modifier = Modifier.weight(1f)
                    )

                    EarningsSmallCard(
                        title = "Satnica",
                        value = "%.2f €".format(hourlyRate),
                        icon = Icons.Default.Euro,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                EarningsLineChart(
                    earningsByDay = shifts.map { shift ->
                        shift.date to (calculateHours(shift.startTime, shift.endTime) * hourlyRate)
                    }
                )
            }

            item {
                Text(
                    text = "Smjene",
                    fontSize = 20.sp,
                    color = Color(0xFF2B2B2B)
                )
            }

            if (shifts.isEmpty()) {
                item {
                    Text(
                        text = "Još nema spremljenih smjena.",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            } else {
                items(shifts) { shift ->
                    ShiftFromTaskCard(
                        shift = shift,
                        hourlyRate = hourlyRate
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = {
                navController.navigate(Screen.AddTask.route)
            },
            containerColor = Color(0xFF6750A4),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Dodaj smjenu"
            )
        }
    }
}

@Composable
fun EarningsSmallCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(120.dp),
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
                    text = value,
                    fontSize = 22.sp,
                    color = Color(0xFF2B2B2B)
                )
            }
        }
    }
}

@Composable
fun ShiftFromTaskCard(
    shift: Task,
    hourlyRate: Double
) {
    val hours = calculateHours(shift.startTime, shift.endTime)
    val earnings = hours * hourlyRate

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = Color(0xFF6750A4)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = shift.date,
                    fontSize = 16.sp,
                    color = Color(0xFF2B2B2B)
                )

                Text(
                    text = "${shift.startTime} - ${shift.endTime} • %.1f h".format(hours),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = "%.2f €".format(earnings),
                fontSize = 18.sp,
                color = Color(0xFF6750A4)
            )
        }
    }
}

fun calculateHours(startTime: String, endTime: String): Double {
    return try {
        val startParts = startTime.split(":")
        val endParts = endTime.split(":")

        val startHour = startParts[0].toInt()
        val startMinute = startParts[1].toInt()

        val endHour = endParts[0].toInt()
        val endMinute = endParts[1].toInt()

        val startTotalMinutes = startHour * 60 + startMinute
        val endTotalMinutes = endHour * 60 + endMinute

        val difference = endTotalMinutes - startTotalMinutes

        if (difference > 0) {
            difference / 60.0
        } else {
            0.0
        }
    } catch (e: Exception) {
        0.0
    }
}

@Composable
fun EarningsLineChart(
    earningsByDay: List<Pair<String, Double>>
) {
    if (earningsByDay.isEmpty()) return

    val groupedData = earningsByDay
        .groupBy { it.first }
        .mapValues { entry -> entry.value.sumOf { it.second } }
        .toList()
        .sortedBy { it.first }

    val maxEarnings = groupedData.maxOfOrNull { it.second } ?: 0.0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "Graf zarade po danima",
                fontSize = 18.sp,
                color = Color(0xFF2B2B2B)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                if (groupedData.size < 2 || maxEarnings <= 0.0) {
                    return@Canvas
                }

                val widthStep = size.width / (groupedData.size - 1)
                val heightScale = size.height / maxEarnings.toFloat()

                val points = groupedData.mapIndexed { index, item ->
                    val x = index * widthStep
                    val y = size.height - (item.second.toFloat() * heightScale)
                    Offset(x, y)
                }

                val path = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    points.drop(1).forEach { point ->
                        lineTo(point.x, point.y)
                    }
                }

                drawPath(
                    path = path,
                    color = Color(0xFF6750A4),
                    style = Stroke(
                        width = 6f,
                        cap = StrokeCap.Round
                    )
                )

                points.forEach { point ->
                    drawCircle(
                        color = Color(0xFF6750A4),
                        radius = 8f,
                        center = point
                    )
                }
            }
        }
    }
}