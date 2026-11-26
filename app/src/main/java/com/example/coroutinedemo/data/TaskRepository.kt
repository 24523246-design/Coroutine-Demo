package com.example.coroutinedemo.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.IOException

class TaskRepository {

    suspend fun getTasks(shouldFail: Boolean = false): List<Task> =
        withContext(Dispatchers.IO) {
            delay(2000L)
            if (shouldFail) {
                throw IOException("Server is not reachable")
            }
            listOf(
                Task(1, "Write lab report", "Mobile App Development", Priority.HIGH),
                Task(2, "Read chapter 5", "Data Science", Priority.MEDIUM),
                Task(3, "Prepare quiz", "Research Methodology", Priority.HIGH),
                Task(4, "Analyze dataset", "Artificial Intelligence", Priority.LOW)
            )
        }

    suspend fun getAnnouncement(): String =
        withContext(Dispatchers.IO) {
            delay(1000L)
            "Quiz 2 is next Monday"
        }

    // Latihan 1: Permintaan asinkron ketiga dengan delay 1500ms
    suspend fun getCourseCount(): Int =
        withContext(Dispatchers.IO) {
            delay(1500L)
            4
        }
}
