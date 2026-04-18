package com.example.todoapp.data.remote

import com.example.todoapp.data.remote.dto.TodoDto
import retrofit2.http.GET

/**
 * Interfejs Retrofit — definiuje endpointy REST API.
 * Retrofit generuje implementację automatycznie.
 * Używamy JSONPlaceholder jako przykładowego publicznego API.
 */
interface TodoApi {
    @GET("todos")
    suspend fun getTodos(): List<TodoDto>
}