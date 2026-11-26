package com.example.coroutinedemo.data

enum class Priority(val label: String) {
    HIGH("Tinggi"),
    MEDIUM("Sedang"),
    LOW("Rendah")
}

data class Task(
    val id: Int,
    val title: String,
    val course: String,
    val priority: Priority = Priority.MEDIUM,
    val isCompleted: Boolean = false
)
