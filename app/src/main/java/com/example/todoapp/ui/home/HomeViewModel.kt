package com.example.todoapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.repository.TaskRepository
import com.example.todoapp.domain.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val tasks: List<Task> = emptyList(),
    val remoteSuggestions: List<Task> = emptyList(),
    val isLoadingRemote: Boolean = false,
    val remoteError: String? = null,
    val newTaskTitle: String = ""
)

/**
 * ViewModel dla HomeScreen.
 * Zarządza lokalną listą zadań (Room) i zdalnymi propozycjami (API).
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // Obserwujemy bazę danych — każda zmiana automatycznie aktualizuje UI
        viewModelScope.launch {
            taskRepository.observeTasks().collect { tasks ->
                _uiState.update { it.copy(tasks = tasks) }
            }
        }
        refreshRemote()
    }

    fun onNewTaskTitleChanged(title: String) {
        _uiState.update { it.copy(newTaskTitle = title) }
    }

    fun addTask() {
        val title = _uiState.value.newTaskTitle.trim()
        if (title.isBlank()) return
        viewModelScope.launch {
            taskRepository.addTask(Task(title = title))
            _uiState.update { it.copy(newTaskTitle = "") }
        }
    }

    fun toggleTask(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(isDone = !task.isDone))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }

    fun editTask(task: Task, newTitle: String, newDescription: String) {
        if (newTitle.isBlank()) return
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(title = newTitle, description = newDescription))
        }
    }

    fun refreshRemote() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingRemote = true, remoteError = null) }
            taskRepository.fetchRemoteSuggestions()
                .onSuccess { suggestions ->
                    _uiState.update { it.copy(remoteSuggestions = suggestions, isLoadingRemote = false) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(remoteError = e.message, isLoadingRemote = false) }
                }
        }
    }

    fun importFromRemote(task: Task) {
        viewModelScope.launch {
            // Importujemy zadanie z API do lokalnej bazy (id = 0 = nowy rekord)
            taskRepository.addTask(task.copy(id = 0, isRemote = false))
        }
    }
}