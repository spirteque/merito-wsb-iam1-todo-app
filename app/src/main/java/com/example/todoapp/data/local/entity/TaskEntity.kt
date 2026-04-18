package com.example.todoapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Encja Room — reprezentuje wiersz w tabeli "tasks".
 * Nie używamy jej bezpośrednio w UI, tylko mapujemy na Task.
 */
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String = "",
    val isDone: Boolean = false
)