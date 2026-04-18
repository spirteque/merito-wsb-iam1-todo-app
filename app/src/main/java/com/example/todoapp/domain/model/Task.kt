package com.example.todoapp.domain.model

/**
 * Model zadania używany w całej aplikacji (UI + logika).
 * Oddzielony od bazy danych celowo — Room ma swój TaskEntity.
 */
data class Task(
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val isDone: Boolean = false,
    val isRemote: Boolean = false // true = pochodzi z API, nie z lokalnej bazy
)