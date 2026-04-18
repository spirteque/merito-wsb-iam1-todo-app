package com.example.todoapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoapp.data.local.entity.TaskEntity

/**
 * Główna klasa bazy danych Room.
 * exportSchema = false — nie generujemy pliku schematu (nie potrzebujemy migracji w projekcie).
 */
@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}