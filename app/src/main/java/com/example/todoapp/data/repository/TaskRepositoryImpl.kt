package com.example.todoapp.data.repository

import com.example.todoapp.data.local.TaskDao
import com.example.todoapp.data.local.entity.TaskEntity
import com.example.todoapp.data.remote.TodoApi
import com.example.todoapp.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementacja TaskRepository.
 * Łączy dane z lokalnej bazy Room i zdalnego API.
 */
@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val todoApi: TodoApi
) : TaskRepository {

    /** Obserwuje zadania z bazy — Flow automatycznie emituje nowe dane przy zmianach. */
    override fun observeTasks(): Flow<List<Task>> {
        return taskDao.observeAll().map { entities ->
            entities.map { it.toTask() }
        }
    }

    override suspend fun addTask(task: Task) {
        taskDao.insert(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        taskDao.update(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.delete(task.toEntity())
    }

    /** Pobiera propozycje zadań z JSONPlaceholder. Zwraca pierwsze 20 wyników. */
    override suspend fun fetchRemoteSuggestions(): Result<List<Task>> {
        return try {
            val todos = todoApi.getTodos().take(20)
            val tasks = todos.map { dto ->
                Task(
                    id = dto.id,
                    title = dto.title,
                    isDone = dto.completed,
                    isRemote = true
                )
            }
            Result.success(tasks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- Funkcje pomocnicze do mapowania ---

    private fun TaskEntity.toTask() = Task(
        id = id,
        title = title,
        description = description,
        isDone = isDone
    )

    private fun Task.toEntity() = TaskEntity(
        id = id,
        title = title,
        description = description,
        isDone = isDone
    )
}