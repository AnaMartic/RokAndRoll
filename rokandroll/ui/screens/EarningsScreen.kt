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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Calendar

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
    var selectedChartFilter by remember { mutableStateOf("Tjedan") }

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
                ChartFilterRow(
                    selectedFilter = selectedChartFilter,
                    onFilterSelected = { selectedChartFilter = it }
                )

                EarningsLineChart(
                    earningsByDay = shifts.map { shift ->
                        shift.date to (calculateHours(shift.startTime, shift.endTime) * hourlyRate)
                    },
                    selectedFilter = selectedChartFilter
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
    earningsByDay: List<Pair<String, Double>>,
    selectedFilter: String
) {
    if (earningsByDay.isEmpty()) return

    val textMeasurer = rememberTextMeasurer()

    val filteredData = earningsByDay.filterBySelectedPeriod(selectedFilter)

    val groupedData = filteredData
        .groupBy { it.first }
        .mapValues { entry -> entry.value.sumOf { it.second } }
        .toList()
        .sortedBy { parseDateForChart(it.first)?.time ?: Long.MAX_VALUE }

    val maxEarnings = groupedData.maxOfOrNull { it.second } ?: 0.0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(310.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "Graf zarade - $selectedFilter",
                fontSize = 18.sp,
                color = Color(0xFF2B2B2B)
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (groupedData.size < 2 || maxEarnings <= 0.0) {
                Text(
                    text = "Dodaj barem dvije smjene za prikaz grafa.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                return@Column
            }

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                val leftPadding = 72f
                val bottomPadding = 55f
                val topPadding = 16f
                val rightPadding = 90f

                val chartWidth = size.width - leftPadding - rightPadding
                val chartHeight = size.height - topPadding - bottomPadding

                val axisColor = Color.Gray
                val lineColor = Color(0xFF6750A4)

                val origin = Offset(leftPadding, topPadding + chartHeight)
                val yAxisTop = Offset(leftPadding, topPadding)
                val xAxisEnd = Offset(leftPadding + chartWidth, topPadding + chartHeight)

                drawLine(
                    color = axisColor,
                    start = origin,
                    end = yAxisTop,
                    strokeWidth = 3f
                )

                drawLine(
                    color = axisColor,
                    start = origin,
                    end = xAxisEnd,
                    strokeWidth = 3f
                )

                val yLabels = listOf(
                    0.0,
                    maxEarnings / 2,
                    maxEarnings
                )

                yLabels.forEach { value ->
                    val y = origin.y - ((value / maxEarnings).toFloat() * chartHeight)

                    drawLine(
                        color = Color(0xFFE0E0E0),
                        start = Offset(leftPadding, y),
                        end = Offset(leftPadding + chartWidth, y),
                        strokeWidth = 1.5f
                    )

                    drawText(
                        textMeasurer = textMeasurer,
                        text = "%.0f€".format(value),
                        topLeft = Offset(-22f, y - 12f),
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    )
                }

                val widthStep = chartWidth / (groupedData.size - 1)

                val points = groupedData.mapIndexed { index, item ->
                    val x = leftPadding + index * widthStep
                    val y = origin.y - ((item.second / maxEarnings).toFloat() * chartHeight)
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
                    color = lineColor,
                    style = Stroke(
                        width = 6f,
                        cap = StrokeCap.Round
                    )
                )

                points.forEach { point ->
                    drawCircle(
                        color = lineColor,
                        radius = 7f,
                        center = point
                    )
                }

                groupedData.forEachIndexed { index, item ->
                    val x = leftPadding + index * widthStep
                    val label = item.first.take(5)

                    drawText(
                        textMeasurer = textMeasurer,
                        text = label,
                        topLeft = Offset(x - 18f, origin.y + 8f),
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ChartFilterRow(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        listOf("Tjedan", "Mjesec").forEach { filter ->
            Button(
                onClick = { onFilterSelected(filter) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedFilter == filter) {
                        Color(0xFF6750A4)
                    } else {
                        Color.White
                    },
                    contentColor = if (selectedFilter == filter) {
                        Color.White
                    } else {
                        Color(0xFF6750A4)
                    }
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(filter)
            }
        }
    }
}

fun List<Pair<String, Double>>.filterBySelectedPeriod(
    selectedFilter: String
): List<Pair<String, Double>> {
    val formatter = SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault())
    val today = Calendar.getInstance()

    val startDate = Calendar.getInstance().apply {
        time = today.time
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val endDate = Calendar.getInstance().apply {
        time = startDate.time

        if (selectedFilter == "Tjedan") {
            add(Calendar.DAY_OF_YEAR, 7)
        } else {
            add(Calendar.MONTH, 1)
        }
    }

    return this.filter { item ->
        val date = try {
            formatter.parse(item.first)
        } catch (e: Exception) {
            null
        }

        date != null &&
                !date.before(startDate.time) &&
                date.before(endDate.time)
    }
}

fun parseDateForChart(dateText: String): Date? {
    return try {
        val formatter = SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault())
        formatter.parse(dateText)
    } catch (e: Exception) {
        null
    }
}