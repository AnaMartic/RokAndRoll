package com.orwima.rokandroll.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.orwima.rokandroll.data.model.User
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    suspend fun saveUser(user: User) {
        usersCollection
            .document(user.id)
            .set(user)
            .await()
    }
}