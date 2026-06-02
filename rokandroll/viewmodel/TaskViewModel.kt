package com.orwima.rokandroll.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orwima.rokandroll.data.model.Task
import com.orwima.rokandroll.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    private val repository = TaskRepository()

    private val _statusMessage = MutableStateFlow("")
    val statusMessage: StateFlow<String> = _statusMessage

    fun addTask(task: Task, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.addTask(task)
                _statusMessage.value = "Obaveza je spremljena."
                onSuccess()
            } catch (e: Exception) {
                _statusMessage.value = "Greška: ${e.message}"
            }
        }
    }
}