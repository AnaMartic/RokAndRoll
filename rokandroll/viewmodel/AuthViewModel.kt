package com.orwima.rokandroll.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.orwima.rokandroll.data.model.User
import com.orwima.rokandroll.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val userRepository = UserRepository()

    private val _authMessage = MutableStateFlow("")
    val authMessage: StateFlow<String> = _authMessage

    fun register(
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = result.user?.uid ?: ""

                val user = User(
                    id = userId,
                    name = name,
                    email = email,
                    hourlyRate = 0.0,
                    city = ""
                )

                userRepository.saveUser(user)

                _authMessage.value = "Registracija uspješna."
                onSuccess()
            } catch (e: Exception) {
                _authMessage.value = "Greška: ${e.message}"
            }
        }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _authMessage.value = "Prijava uspješna."
                onSuccess()
            } catch (e: Exception) {
                _authMessage.value = "Greška: ${e.message}"
            }
        }
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun logout() {
        auth.signOut()
    }
}