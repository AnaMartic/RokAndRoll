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
import com.orwima.rokandroll.data.model.Shift
import com.orwima.rokandroll.navigation.Screen
import com.orwima.rokandroll.viewmodel.ShiftViewModel

@Composable
fun EarningsScreen(
    navController: NavController,
    shiftViewModel: ShiftViewModel = viewModel()
) {
    val shifts by shiftViewModel.shifts.collectAsState()

    LaunchedEffect(Unit) {
        shiftViewModel.loadShifts()
    }

    val totalEarnings = shifts.sumOf { it.earnings }
    val totalHours = shifts.sumOf { it.hours }
    val averageRate = if (totalHours > 0) totalEarnings / totalHours else 0.0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F2FA))
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = "Zarada",
                fontSize = 30.sp,
                color = Color(0xFF2B2B2B)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Pregled studentske zarade",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

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

            Spacer(modifier = Modifier.height(20.dp))

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
                    title = "Prosjek",
                    value = "%.2f €".format(averageRate),
                    icon = Icons.Default.Euro,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Smjene",
                fontSize = 20.sp,
                color = Color(0xFF2B2B2B)
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (shifts.isEmpty()) {
                Text(
                    text = "Još nema spremljenih smjena.",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 90.dp)
                ) {
                    items(shifts) { shift ->
                        ShiftEarningCard(shift = shift)
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                navController.navigate(Screen.AddShift.route)
            },
            containerColor = Color(0xFF6750A4),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp)
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
fun ShiftEarningCard(shift: Shift) {
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
                    text = "${shift.startTime} - ${shift.endTime} • ${shift.hours} h",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = "%.2f €".format(shift.earnings),
                fontSize = 18.sp,
                color = Color(0xFF6750A4)
            )
        }
    }
}