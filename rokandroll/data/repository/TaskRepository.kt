package com.orwima.rokandroll.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.orwima.rokandroll.data.model.Task
import kotlinx.coroutines.tasks.await

class TaskRepository {

    private val db = FirebaseFirestore.getInstance()
    private val tasksCollection = db.collection("tasks")

    suspend fun addTask(task: Task) {
        tasksCollection.add(task).await()
    }

    suspend fun getTasks(): List<Task> {
        return tasksCollection
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                document.toObject(Task::class.java)?.copy(id = document.id)
            }
    }
}