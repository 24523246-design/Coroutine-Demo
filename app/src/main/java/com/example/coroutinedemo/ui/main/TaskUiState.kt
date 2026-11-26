package com.example.coroutinedemo.ui.main

import com.example.coroutinedemo.data.Task

data class TasksUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val announcement: String = "",
    val courseCount: Int = 0,          // Latihan 1
    val loadDurationMs: Long? = null,  // Modifikasi: mengukur waktu eksekusi
    val searchQuery: String = "",      // Modifikasi: filter pencarian
    val isSortedAsc: Boolean = false,  // Latihan 6: status pengurutan
    val errorMessage: String? = null,
    val tapCount: Int = 0
)
