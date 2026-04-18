package com.example.todoapp.data.local

import androidx.room.*
import com.example.todoapp.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) — interfejs dostępu do tabeli tasks.
 * Room generuje implementację automatycznie w czasie kompilacji.
 */
@Dao
interface TaskDao {

    /** Zwraca wszystkie zadania jako Flow — aktualizuje się automatycznie przy zmianach. */
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun observeAll(): Flow<List<TaskEntity>>

    /** Dodaje nowe zadanie. id jest generowane automatycznie. */
    @Insert
    suspend fun insert(task: TaskEntity)

    /** Aktualizuje istniejące zadanie (dopasowanie po id). */
    @Update
    suspend fun update(task: TaskEntity)

    /** Usuwa zadanie. */
    @Delete
    suspend fun delete(task: TaskEntity)
}