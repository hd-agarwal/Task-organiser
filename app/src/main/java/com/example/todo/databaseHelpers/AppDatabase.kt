package com.example.todo.databaseHelpers

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todo.models.Todo

@Database(entities = [Todo::class],version = 2)
abstract class AppDatabase:RoomDatabase() {
    abstract fun todoDao(): TodoDao
}