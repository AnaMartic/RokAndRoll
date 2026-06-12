package com.orwima.rokandroll.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.orwima.rokandroll.data.model.Task
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NotificationScheduler(
    private val context: Context
) {
    fun scheduleTaskReminder(task: Task) {
        val taskDateTime = parseTaskDateTime(task) ?: return

        val reminderTime = Calendar.getInstance().apply {
            time = taskDateTime.time
            add(Calendar.HOUR_OF_DAY, -1)
        }

        val now = Calendar.getInstance()

        if (reminderTime.before(now)) return

        val intent = Intent(context, TaskNotificationReceiver::class.java).apply {
            putExtra("title", "Podsjetnik: ${task.title}")
            putExtra(
                "message",
                "${task.type} počinje za 1 sat, u ${task.startTime}."
            )
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            reminderTime.timeInMillis,
            pendingIntent
        )
    }

    private fun parseTaskDateTime(task: Task): Calendar? {
        return try {
            val formatter = SimpleDateFormat("dd.MM.yyyy. HH:mm", Locale.getDefault())
            val date = formatter.parse("${task.date} ${task.startTime}") ?: return null

            Calendar.getInstance().apply {
                time = date
            }
        } catch (e: Exception) {
            null
        }
    }
}