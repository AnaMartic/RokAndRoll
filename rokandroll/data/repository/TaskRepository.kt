package com.orwima.rokandroll.data.repository

import com.orwima.rokandroll.data.model.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TaskRepository {

    private val db = FirebaseFirestore.getInstance()
    private val tasksCollection = db.collection("tasks")

    suspend fun addTask(task: Task) {
        tasksCollection.add(task).await()
    }
}