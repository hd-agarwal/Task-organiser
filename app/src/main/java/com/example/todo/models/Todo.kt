package com.example.todo.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time

@Entity
data class Todo(
    var task: String,
    var description: String,
    var date: Long,
    var time: Long,
    var category: String,
    var isDone: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L
)