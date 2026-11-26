package com.example.coroutinedemo.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    viewModel: TasksViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    TasksContent(
        state = state,
        onLoad = { viewModel.loadData() },
        onLoadWithError = { viewModel.loadData(shouldFail = true) },
        onCancel = viewModel::cancelLoading,
        onSort = viewModel::sortTasksByTitle,
        onToggleTask = viewModel::toggleTaskCompletion,
        onSearchChange = viewModel::onSearchQueryChange,
        onTap = viewModel::onTap,
        onBlock = viewModel::blockMainThread,
        modifier = modifier
    )
}

@Composable
fun TasksContent(
    state: TasksUiState,
    onLoad: () -> Unit,
    onLoadWithError: () -> Unit,
    onCancel: () -> Unit,
    onSort: () -> Unit,
    onToggleTask: (Int) -> Unit,
    onSearchChange: (String) -> Unit,
    onTap: () -> Unit,
    onBlock: () -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredTasks = state.tasks.filter {
        it.title.contains(state.searchQuery, ignoreCase = true) ||
                it.course.contains(state.searchQuery, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Coroutine Demo",
            style = MaterialTheme.typography.headlineSmall
        )

        // Baris Tombol Utama: Load, Load (fail), Cancel
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onLoad, enabled = !state.isLoading) {
                Text("Load")
            }
            OutlinedButton(
                onClick = onLoadWithError,
                enabled = !state.isLoading
            ) {
                Text("Load (fail)")
            }
            OutlinedButton(onClick = onCancel, enabled = state.isLoading) {
                Text("Cancel")
            }
        }

        // Baris Tombol Interaktif: Tap me, Block UI, Sort (Latihan 6)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onTap) {
                Text("Tap me: ${state.tapCount}")
            }
            OutlinedButton(onClick = onBlock) {
                Text("Block UI")
            }
            OutlinedButton(
                onClick = onSort,
                enabled = state.tasks.isNotEmpty() && !state.isLoading
            ) {
                Text(if (state.isSortedAsc) "Sort (Z-A)" else "Sort (A-Z)")
            }
        }

        // Indikator Loading
        if (state.isLoading) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CircularProgressIndicator()
                Text("Memuat data secara paralel...")
            }
        }

        // Latihan 2: Pesan Error + Tombol Retry
        state.errorMessage?.let { message ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = onLoad) {
                    Text("Retry")
                }
            }
        }

        // Card Informasi Pengumuman, Jumlah Mata Kuliah (Latihan 1), dan Waktu Muat
        if (state.announcement.isNotEmpty() || state.courseCount > 0) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (state.announcement.isNotEmpty()) {
                        Text(
                            text = "Announcement: ${state.announcement}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    if (state.courseCount > 0) {
                        Text(
                            text = "Active Courses: ${state.courseCount} mata kuliah",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    state.loadDurationMs?.let { duration ->
                        Text(
                            text = "Loaded concurrently in $duration ms",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }

        // Modifikasi: Kolom Pencarian Tugas
        if (state.tasks.isNotEmpty()) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = onSearchChange,
                label = { Text("Cari tugas atau mata kuliah...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Daftar Tugas
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(filteredTasks, key = { it.id }) { task ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = task.isCompleted,
                            onCheckedChange = { onToggleTask(task.id) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = task.title,
                                style = MaterialTheme.typography.titleMedium,
                                textDecoration = if (task.isCompleted) {
                                    TextDecoration.LineThrough
                                } else {
                                    TextDecoration.None
                                }
                            )
                            Text(
                                text = task.course,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        AssistChip(
                            onClick = { },
                            label = { Text(task.priority.label) }
                        )
                    }
                }
            }
        }
    }
}
