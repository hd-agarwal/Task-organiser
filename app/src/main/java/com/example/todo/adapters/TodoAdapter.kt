package com.example.todo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.DateAndTime
import com.example.todo.R
import com.example.todo.models.Todo
import kotlinx.android.synthetic.main.layout_todo.view.*
import java.util.*

class TodoAdapter(private val todoList: List<Todo>) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(todo: Todo) {
            with(itemView) {
                tvTitle.apply { text = todo.title }
                tvDescription.apply { text = todo.description }
                val colors = resources.getIntArray(R.array.random_color)
                val randomColor = colors[Random().nextInt(colors.size)]
                viewColorTag.apply { setBackgroundColor(randomColor) }
                tvCategory.apply { text = todo.category }
                tvDate.apply { text = DateAndTime.getDateString(todo.date) }
                tvTime.apply { text = DateAndTime.getTimeString(todo.time) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_todo, parent, false)
        )

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todoList[position])
    }

    override fun getItemCount() = todoList.size
}