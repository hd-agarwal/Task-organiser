package com.example.todo.databaseHelpers

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todo.activities.DB_NAME
import com.example.todo.models.Todo

@Database(entities = [Todo::class],version = 3)
abstract class AppDatabase:RoomDatabase() {
    abstract fun todoDao(): TodoDao
    companion object{
        @Volatile
        private var INSTANCE:AppDatabase?=null
        fun getDatabase(context: Context):AppDatabase{
            val tempInstance= INSTANCE
            if(tempInstance!=null)
                return tempInstance
            synchronized(this){
                val instance= Room.databaseBuilder(context.applicationContext,AppDatabase::class.java,
                    DB_NAME).build()
                INSTANCE=instance
                return instance
            }
        }
    }
}