package com.example.todo.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.DisplayMetrics
import android.view.View
import android.widget.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.todo.DateAndTime
import com.example.todo.R
import com.example.todo.databaseHelpers.AppDatabase
import com.example.todo.models.Todo
import kotlinx.android.synthetic.main.activity_create_task.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


const val DB_NAME = "todo.db"

class CreateTaskActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var myCalendar: Calendar
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener
    private lateinit var newCategory: String
    private lateinit var adapter: ArrayAdapter<String>
    private val listCategories =
        arrayListOf("Business", "Personal", "Insurance", "Shopping", "Appointment")
    private val db by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            DB_NAME
        ).fallbackToDestructiveMigration().build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        textInputEditTextDate.apply { setOnClickListener(this@CreateTaskActivity) }
        textInputEditTextTime.apply { setOnClickListener(this@CreateTaskActivity) }
        btnAddCategory.apply { setOnClickListener(this@CreateTaskActivity) }
        btnSave.apply { setOnClickListener(this@CreateTaskActivity) }
        setupCategorySpinner()
    }

    private fun setupCategorySpinner() {
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listCategories)
        listCategories.sort()
        spCategory.adapter = adapter
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.textInputEditTextDate -> {
                setDateListener()
            }
            R.id.textInputEditTextTime -> {
                setTimeListener()
            }
            R.id.btnAddCategory -> {
                showCategoryDialog()
            }
            R.id.btnSave -> {
                saveTask()
            }
        }
    }

    private fun saveTask() {
        when {
            textInputEditTextTitle.text.isNullOrEmpty() -> {
                Toast.makeText(applicationContext, "Task title is empty", Toast.LENGTH_SHORT).show()
            }
            textInputEditTextDescription.text.isNullOrEmpty() -> {
                Toast.makeText(applicationContext, "Task Description is empty", Toast.LENGTH_SHORT)
                    .show()
            }
            textInputEditTextDate.text.isNullOrEmpty() -> {
                Toast.makeText(applicationContext, "Please provide a Date", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                insertToDB(
                    Todo(
                        textInputEditTextTitle.text.toString(),
                        textInputEditTextDescription.text.toString(),
                        myCalendar.time.time,
                        myCalendar.time.time,
                        spCategory.selectedItem.toString()
                    )
                )
            }
        }
    }

    private fun insertToDB(todo: Todo) {
        GlobalScope.launch(Dispatchers.Main){
            withContext(Dispatchers.IO){
                return@withContext db.todoDao().insertTodo(todo)
            }
            Toast.makeText(this@CreateTaskActivity,"Task added",Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showCategoryDialog() {
        val addCategoryDialogBuilder = AlertDialog.Builder(this)
        addCategoryDialogBuilder.apply {
            setTitle("Custom Task Category:-")
            val etInput = EditText(applicationContext)
            etInput.apply {
                hint = "Custom category"
                maxLines = 1
                isSingleLine = true
                inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                lp.setMargins(
                    convertPixelsToDp(30f, this@CreateTaskActivity),
                    convertPixelsToDp(25f, this@CreateTaskActivity),
                    convertPixelsToDp(30f, this@CreateTaskActivity),
                    convertPixelsToDp(25f, this@CreateTaskActivity)
                )
                layoutParams = lp
            }
            addCategoryDialogBuilder.setView(etInput)
            addCategoryDialogBuilder.setPositiveButton(
                "OK"
            ) { _, _ ->
                newCategory = etInput.text.toString()
                listCategories.add(newCategory)
                val spinnerPosition: Int = adapter.getPosition(newCategory)
                spCategory.setSelection(spinnerPosition)
            }
            addCategoryDialogBuilder.setNegativeButton(
                "Cancel"
            ) { dialog, _ -> dialog.cancel() }
        }
        addCategoryDialogBuilder.show()
    }

    private fun setTimeListener() {
        myCalendar = Calendar.getInstance()
        timeSetListener =
            TimePickerDialog.OnTimeSetListener { _: TimePicker, hour: Int, minute: Int ->
                myCalendar.set(Calendar.HOUR_OF_DAY, hour)
                myCalendar.set(Calendar.MINUTE, minute)
                updateTime()
            }
        val timePickerDialog = TimePickerDialog(
            this,
            timeSetListener,
            myCalendar.get(Calendar.HOUR_OF_DAY),
            myCalendar.get(Calendar.MINUTE),
            false
        )
        timePickerDialog.show()
    }

    private fun updateTime() {
        textInputEditTextTime.setText(DateAndTime.getTimeString(myCalendar.time))
    }

    private fun setDateListener() {
        myCalendar = Calendar.getInstance()
        dateSetListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDate()
            }
        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDate() {
        val myFormat = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(myFormat)
        textInputEditTextDate.setText(DateAndTime.getDateString(myCalendar.time))
        tvTimeTag.apply { setTextColor(Color.parseColor("#1693FF")) }
        textInputLayoutTime.apply { isEnabled = true }
    }

    private fun convertPixelsToDp(px: Float, context: Context): Int {
        val resources: Resources = context.resources
        val metrics: DisplayMetrics = resources.displayMetrics
        return (px / (metrics.densityDpi / 160f)).toInt()
    }
}