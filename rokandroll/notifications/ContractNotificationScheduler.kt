package com.orwima.rokandroll.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

class ContractNotificationScheduler(
    private val context: Context
) {
    fun scheduleNextContractReminder() {
        val reminderDate = calculateNextReminderDate()

        val intent = Intent(context, ContractNotificationReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            3001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            reminderDate.timeInMillis,
            pendingIntent
        )
    }

    private fun calculateNextReminderDate(): Calendar {
        val now = Calendar.getInstance()

        val reminder = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            add(Calendar.MONTH, 1)
            add(Calendar.DAY_OF_MONTH, -3)
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (reminder.before(now)) {
            reminder.add(Calendar.MONTH, 1)
        }

        return reminder
    }
}