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
 * Lokalna lista zadań na pierwszym planie; propozycje z API w dolnym arkuszu (modal).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var taskToEdit by remember { mutableStateOf<Task?>(null) }
    var showRemoteSheet by remember { mutableStateOf(false) }
    val remoteSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

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

    if (showRemoteSheet) {
        ModalBottomSheet(
            onDismissRequest = { showRemoteSheet = false },
            sheetState = remoteSheetState,
        ) {
            RemoteSuggestionsSheetContent(
                uiState = uiState,
                onRefresh = { viewModel.refreshRemote() },
                onImport = { viewModel.importFromRemote(it) },
            )
        }
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

            item {
                FilledTonalButton(
                    onClick = { showRemoteSheet = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                ) {
                    if (uiState.isLoadingRemote) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                    Text(stringResource(R.string.task_remote_open_sheet))
                }
            }
        }
    }
}

@Composable
private fun RemoteSuggestionsSheetContent(
    uiState: HomeUiState,
    onRefresh: () -> Unit,
    onImport: (Task) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 24.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.task_remote_section),
                    style = MaterialTheme.typography.titleLarge,
                )
                TextButton(onClick = onRefresh, enabled = !uiState.isLoadingRemote) {
                    Text(stringResource(R.string.task_remote_refresh))
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        }

        when {
            uiState.isLoadingRemote -> item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.remoteError != null -> item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.task_remote_error),
                        color = MaterialTheme.colorScheme.error,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRefresh) {
                        Text(stringResource(R.string.task_remote_refresh))
                    }
                }
            }

            uiState.remoteSuggestions.isEmpty() -> item {
                Text(
                    text = stringResource(R.string.task_remote_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                )
            }

            else -> items(uiState.remoteSuggestions, key = { "remote_${it.id}" }) { task ->
                ListItem(
                    headlineContent = { Text(task.title) },
                    trailingContent = {
                        TextButton(onClick = { onImport(task) }) {
                            Text(stringResource(R.string.task_import_from_remote))
                        }
                    },
                )
                HorizontalDivider()
            }
        }
    }
}

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
