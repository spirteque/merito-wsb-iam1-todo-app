package com.example.todoapp.data.repository

import com.example.todoapp.domain.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Interfejs repozytorium zadań.
 * Dzięki interfejsowi ViewModel nie zależy bezpośrednio od implementacji.
 */
interface TaskRepository {
    fun observeTasks(): Flow<List<Task>>
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun fetchRemoteSuggestions(): Result<List<Task>>
}