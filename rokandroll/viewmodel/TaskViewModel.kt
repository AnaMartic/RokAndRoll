package com.orwima.rokandroll.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.orwima.rokandroll.data.model.Task
import com.orwima.rokandroll.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    private val repository = TaskRepository()
    private val auth = FirebaseAuth.getInstance()

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _statusMessage = MutableStateFlow("")
    val statusMessage: StateFlow<String> = _statusMessage

    fun addTask(task: Task, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid

                if (userId == null) {
                    _statusMessage.value = "Korisnik nije prijavljen."
                    return@launch
                }

                val taskWithUserId = task.copy(userId = userId)

                repository.addTask(taskWithUserId)
                _statusMessage.value = "Obaveza je spremljena."

                loadTasks()
                onSuccess()
            } catch (e: Exception) {
                _statusMessage.value = "Greška: ${e.message}"
            }
        }
    }

    fun loadTasks() {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid

                if (userId == null) {
                    _statusMessage.value = "Korisnik nije prijavljen."
                    return@launch
                }

                _tasks.value = repository.getTasksForUser(userId)
            } catch (e: Exception) {
                _statusMessage.value = "Greška pri dohvaćanju: ${e.message}"
            }
        }
    }
}