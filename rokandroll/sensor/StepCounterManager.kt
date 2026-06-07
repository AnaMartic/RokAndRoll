package com.orwima.rokandroll.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StepCounterManager(context: Context) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val stepSensor =
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

    private val prefs =
        context.getSharedPreferences("steps_prefs", Context.MODE_PRIVATE)

    private val _steps = MutableStateFlow(
        loadSavedSteps()
    )
    val steps: StateFlow<Int> = _steps

    private var initialSteps: Int? = null

    fun startListening() {
        stepSensor?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {

        val newSteps = _steps.value + 1

        _steps.value = newSteps

        prefs.edit()
            .putInt("steps_count", newSteps)
            .putString("steps_date", getTodayDate())
            .apply()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun getTodayDate(): String {
        return java.text.SimpleDateFormat(
            "dd.MM.yyyy.",
            java.util.Locale.getDefault()
        ).format(java.util.Date())
    }

    private fun loadSavedSteps(): Int {

        val savedDate = prefs.getString(
            "steps_date",
            ""
        ) ?: ""

        return if (savedDate == getTodayDate()) {
            prefs.getInt("steps_count", 0)
        } else {

            prefs.edit()
                .putString("steps_date", getTodayDate())
                .putInt("steps_count", 0)
                .apply()

            0
        }
    }
}