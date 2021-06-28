package com.example.todo.databaseHelpers

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.todo.models.Todo

@Dao
interface TodoDao {
    @Insert
    suspend fun insertTodo(todo: Todo)

    @Query("Select * from Todo")
    fun getAllTodos():LiveData<List<Todo>>

    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Query("Select * from Todo where isDone=:isDone")
    fun getAllTodos(isDone:Boolean):LiveData<List<Todo>>

    @Query("Update Todo set isDone=:isDone where id=:id")
    suspend fun finishTask(id:Long,isDone: Boolean=true)
}