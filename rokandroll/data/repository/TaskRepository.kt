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

    suspend fun getTasksForUser(userId: String): List<Task> {
        return tasksCollection
            .whereEqualTo("userId", userId)
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                document.toObject(Task::class.java)?.copy(id = document.id)
            }
    }

    suspend fun deleteTask(taskId: String) {
        tasksCollection
            .document(taskId)
            .delete()
            .await()
    }

    suspend fun softDeleteTask(task: Task) {
        tasksCollection
            .document(task.id)
            .set(task.copy(deleted = true))
            .await()
    }

    suspend fun getTaskById(taskId: String): Task? {
        return tasksCollection
            .document(taskId)
            .get()
            .await()
            .toObject(Task::class.java)
            ?.copy(id = taskId)
    }

    suspend fun updateTask(task: Task) {
        tasksCollection
            .document(task.id)
            .set(task)
            .await()
    }
}