package com.orwima.rokandroll.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orwima.rokandroll.data.repository.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val apiKey = "d6d41bb4984aae6101302a41d2b05ced"

    private val _weatherText = MutableStateFlow("Nema podataka")
    val weatherText: StateFlow<String> = _weatherText

    fun loadWeather(city: String) {
        if (city.isBlank()) {
            _weatherText.value = "Unesi grad"
            return
        }

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getWeatherByCity(
                    city = city,
                    apiKey = apiKey
                )

                val temperature = response.main.temp
                val description = response.weather.firstOrNull()?.description ?: ""

                _weatherText.value = "%.0f°C\n%s".format(temperature, description)
            } catch (e: Exception) {
                _weatherText.value = "Greška"
            }
        }
    }
}