package com.example.todoapp.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * DTO (Data Transfer Object) — struktura odpowiedzi z JSONPlaceholder /todos.
 * Używamy kotlinx.serialization do parsowania JSON.
 */
@Serializable
data class TodoDto(
    val id: Int,
    val title: String,
    val completed: Boolean
)