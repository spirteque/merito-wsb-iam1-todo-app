package com.example.todoapp.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todoapp.R
import com.example.todoapp.domain.model.Task

/**
 * Główny ekran aplikacji.
 * Pokazuje lokalną listę zadań i propozycje z API.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var taskToEdit by remember { mutableStateOf<Task?>(null) }

    // Dialog edycji — pokazujemy gdy taskToEdit != null
    taskToEdit?.let { task ->
        EditTaskDialog(
            task = task,
            onDismiss = { taskToEdit = null },
            onConfirm = { title, desc ->
                viewModel.editTask(task, title, desc)
                taskToEdit = null
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.addTask() }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.task_add))
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Pole do wpisania nowego zadania
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.newTaskTitle,
                        onValueChange = viewModel::onNewTaskTitleChanged,
                        placeholder = { Text(stringResource(R.string.task_new_placeholder)) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = { viewModel.addTask() },
                        enabled = uiState.newTaskTitle.isNotBlank()
                    ) {
                        Text(stringResource(R.string.task_add))
                    }
                }
            }

            // Lista lokalnych zadań
            if (uiState.tasks.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.task_empty),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(uiState.tasks, key = { it.id }) { task ->
                    TaskItem(
                        task = task,
                        onToggle = { viewModel.toggleTask(task) },
                        onEdit = { taskToEdit = task },
                        onDelete = { viewModel.deleteTask(task) }
                    )
                    HorizontalDivider()
                }
            }

            // Sekcja propozycji z API
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 8.dp, top = 16.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.task_remote_section),
                        style = MaterialTheme.typography.titleSmall
                    )
                    TextButton(onClick = { viewModel.refreshRemote() }) {
                        Text(stringResource(R.string.task_remote_refresh))
                    }
                }
            }

            when {
                uiState.isLoadingRemote -> item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.remoteError != null -> item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.task_remote_error),
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.refreshRemote() }) {
                            Text(stringResource(R.string.task_remote_refresh))
                        }
                    }
                }
                else -> items(uiState.remoteSuggestions, key = { "remote_${it.id}" }) { task ->
                    ListItem(
                        headlineContent = { Text(task.title) },
                        trailingContent = {
                            TextButton(onClick = { viewModel.importFromRemote(task) }) {
                                Text(stringResource(R.string.task_import_from_remote))
                            }
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

/** Pojedynczy element listy zadań. */
@Composable
private fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = task.title,
                textDecoration = if (task.isDone) TextDecoration.LineThrough else null
            )
        },
        supportingContent = if (task.description.isNotBlank()) {
            { Text(task.description, style = MaterialTheme.typography.bodySmall) }
        } else null,
        leadingContent = {
            Checkbox(checked = task.isDone, onCheckedChange = { onToggle() })
        },
        trailingContent = {
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.task_edit))
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.task_delete))
                }
            }
        }
    )
}