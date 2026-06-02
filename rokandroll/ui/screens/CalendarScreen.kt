package com.orwima.rokandroll.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalendarScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F2FA))
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = "Kalendar",
                fontSize = 30.sp,
                color = Color(0xFF2B2B2B)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Pregled obaveza i smjena",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            DayHeader(day = "Danas")

            Spacer(modifier = Modifier.height(12.dp))

            CalendarTaskCard(
                title = "Predavanje",
                description = "Razvoj mobilnih aplikacija",
                time = "10:00 - 12:00",
                icon = Icons.Default.School
            )

            Spacer(modifier = Modifier.height(14.dp))

            CalendarTaskCard(
                title = "Smjena",
                description = "Studentski posao",
                time = "16:00 - 21:00",
                icon = Icons.Default.Work
            )

            Spacer(modifier = Modifier.height(24.dp))

            DayHeader(day = "Sutra")

            Spacer(modifier = Modifier.height(12.dp))

            CalendarTaskCard(
                title = "Kolokvij",
                description = "Baze podataka",
                time = "09:00",
                icon = Icons.Default.Event
            )
        }

        FloatingActionButton(
            onClick = {
                // kasnije otvaramo ekran za dodavanje obaveze
            },
            containerColor = Color(0xFF6750A4),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Dodaj obavezu"
            )
        }
    }
}

@Composable
fun DayHeader(day: String) {
    Text(
        text = day,
        fontSize = 20.sp,
        color = Color(0xFF6750A4)
    )
}

@Composable
fun CalendarTaskCard(
    title: String,
    description: String,
    time: String,
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

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    color = Color(0xFF2B2B2B)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = time,
                fontSize = 14.sp,
                color = Color(0xFF6750A4)
            )
        }
    }
}