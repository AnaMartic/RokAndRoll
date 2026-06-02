package com.orwima.rokandroll.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Euro
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EarningsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F2FA))
            .padding(20.dp)
    ) {
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
                    text = "Ukupno ovaj mjesec",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "420,00 €",
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
                value = "56 h",
                icon = Icons.Default.Work,
                modifier = Modifier.weight(1f)
            )

            EarningsSmallCard(
                title = "Satnica",
                value = "7.50 €",
                icon = Icons.Default.Euro,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Zadnje smjene",
            fontSize = 20.sp,
            color = Color(0xFF2B2B2B)
        )

        Spacer(modifier = Modifier.height(12.dp))

        ShiftEarningCard(
            date = "12.06.2026.",
            hours = "5 h",
            amount = "37,50 €"
        )

        Spacer(modifier = Modifier.height(12.dp))

        ShiftEarningCard(
            date = "10.06.2026.",
            hours = "6 h",
            amount = "45,00 €"
        )

        Spacer(modifier = Modifier.height(12.dp))

        ShiftEarningCard(
            date = "08.06.2026.",
            hours = "4 h",
            amount = "30,00 €"
        )
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
fun ShiftEarningCard(
    date: String,
    hours: String,
    amount: String
) {
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
                    text = date,
                    fontSize = 16.sp,
                    color = Color(0xFF2B2B2B)
                )

                Text(
                    text = hours,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = amount,
                fontSize = 18.sp,
                color = Color(0xFF6750A4)
            )
        }
    }
}