package com.orwima.rokandroll.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.orwima.rokandroll.data.model.User
import com.orwima.rokandroll.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val repository = UserRepository()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _statusMessage = MutableStateFlow("")
    val statusMessage: StateFlow<String> = _statusMessage

    fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                val firebaseUser = auth.currentUser ?: return@launch
                val userFromDatabase = repository.getUser(firebaseUser.uid)

                _user.value = userFromDatabase ?: User(
                    id = firebaseUser.uid,
                    name = "",
                    email = firebaseUser.email ?: "",
                    hourlyRate = 0.0,
                    city = ""
                )
            } catch (e: Exception) {
                _statusMessage.value = "Greška pri dohvaćanju korisnika: ${e.message}"
            }
        }
    }

    fun updateUser(
        name: String,
        hourlyRateText: String,
        city: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val firebaseUser = auth.currentUser

                if (firebaseUser == null) {
                    _statusMessage.value = "Korisnik nije prijavljen."
                    return@launch
                }

                val hourlyRate = hourlyRateText
                    .replace(",", ".")
                    .toDoubleOrNull() ?: 0.0

                val updatedUser = User(
                    id = firebaseUser.uid,
                    name = name,
                    email = firebaseUser.email ?: "",
                    hourlyRate = hourlyRate,
                    city = city
                )

                repository.updateUser(updatedUser)
                _user.value = updatedUser
                _statusMessage.value = "Profil je spremljen."
                onSuccess()
            } catch (e: Exception) {
                _statusMessage.value = "Greška pri spremanju profila: ${e.message}"
            }
        }
    }

    fun logout() {
        auth.signOut()
    }
}