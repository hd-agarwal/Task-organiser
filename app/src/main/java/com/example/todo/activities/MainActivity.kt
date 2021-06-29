package com.example.todo.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.todo.R
import com.example.todo.adapters.TodoAdapter
import com.example.todo.databaseHelpers.AppDatabase
import com.example.todo.models.Todo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val listTodos = arrayListOf<Todo>()
    private val todoAdapter = TodoAdapter(listTodos)
    private val db by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            DB_NAME
        ).fallbackToDestructiveMigration().build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initUI()
    }

    private fun initUI() {
        floatingActionButton.apply {
            setOnClickListener {
                startActivity(
                    Intent(
                        applicationContext,
                        CreateTaskActivity::class.java
                    )
                )
            }
        }
        rvTasks.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = todoAdapter
        }
        db.todoDao().getAllTodos().observe(this, Observer {
            listTodos.clear()
            listTodos.addAll(it)
            todoAdapter.notifyDataSetChanged()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.history -> startActivity(Intent(applicationContext, HistoryActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}