package com.orwima.rokandroll.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.orwima.rokandroll.data.model.Shift
import com.orwima.rokandroll.data.repository.ShiftRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShiftViewModel : ViewModel() {

    private val repository = ShiftRepository()
    private val auth = FirebaseAuth.getInstance()

    private val _shifts = MutableStateFlow<List<Shift>>(emptyList())
    val shifts: StateFlow<List<Shift>> = _shifts

    private val _statusMessage = MutableStateFlow("")
    val statusMessage: StateFlow<String> = _statusMessage

    fun addShift(
        date: String,
        startTime: String,
        endTime: String,
        hoursText: String,
        hourlyRateText: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid

                if (userId == null) {
                    _statusMessage.value = "Korisnik nije prijavljen."
                    return@launch
                }

                val hours = hoursText.replace(",", ".").toDoubleOrNull()
                val hourlyRate = hourlyRateText.replace(",", ".").toDoubleOrNull()

                if (date.isBlank() || startTime.isBlank() || endTime.isBlank()) {
                    _statusMessage.value = "Datum i vrijeme su obavezni."
                    return@launch
                }

                if (hours == null || hours <= 0.0) {
                    _statusMessage.value = "Unesi ispravan broj sati."
                    return@launch
                }

                if (hourlyRate == null || hourlyRate <= 0.0) {
                    _statusMessage.value = "Unesi ispravnu satnicu."
                    return@launch
                }

                val shift = Shift(
                    userId = userId,
                    date = date,
                    startTime = startTime,
                    endTime = endTime,
                    hours = hours,
                    hourlyRate = hourlyRate,
                    earnings = hours * hourlyRate
                )

                repository.addShift(shift)
                _statusMessage.value = "Smjena je spremljena."
                loadShifts()
                onSuccess()
            } catch (e: Exception) {
                _statusMessage.value = "Greška: ${e.message}"
            }
        }
    }

    fun loadShifts() {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid

                if (userId == null) {
                    _statusMessage.value = "Korisnik nije prijavljen."
                    return@launch
                }

                _shifts.value = repository.getShiftsForUser(userId)
            } catch (e: Exception) {
                _statusMessage.value = "Greška pri dohvaćanju smjena: ${e.message}"
            }
        }
    }
}