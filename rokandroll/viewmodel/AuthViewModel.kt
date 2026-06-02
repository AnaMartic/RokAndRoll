package com.orwima.rokandroll.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _authMessage = MutableStateFlow("")
    val authMessage: StateFlow<String> = _authMessage

    fun register(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
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