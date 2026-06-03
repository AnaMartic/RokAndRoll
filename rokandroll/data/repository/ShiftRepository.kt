package com.orwima.rokandroll.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.orwima.rokandroll.data.model.Shift
import kotlinx.coroutines.tasks.await

class ShiftRepository {

    private val db = FirebaseFirestore.getInstance()
    private val shiftsCollection = db.collection("shifts")

    suspend fun addShift(shift: Shift) {
        shiftsCollection.add(shift).await()
    }

    suspend fun getShiftsForUser(userId: String): List<Shift> {
        return shiftsCollection
            .whereEqualTo("userId", userId)
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                document.toObject(Shift::class.java)?.copy(id = document.id)
            }
    }
}