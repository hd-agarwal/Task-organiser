package com.example.todo

import java.text.SimpleDateFormat
import java.util.*

internal class DateAndTime {
    companion object {
        fun getDateString(date: Long, format: String): String {
            val sdf = SimpleDateFormat(format)
            return sdf.format(date)
        }

        fun getDateString(date: Long): String {
            val sdf = SimpleDateFormat("EEE, d MMM yyyy")
            return sdf.format(date)
        }

        fun getDateString(date: Date, format: String): String {
            val sdf = SimpleDateFormat(format)
            return sdf.format(date)
        }

        fun getDateString(date: Date): String {
            val sdf = SimpleDateFormat("EEE, d MMM yyyy")
            return sdf.format(date)
        }

        fun getTimeString(time: Long, format: String): String {
            val sdf = SimpleDateFormat(format)
            return sdf.format(time)
        }

        fun getTimeString(time: Long): String {
            val sdf = SimpleDateFormat("h:mm a")
            return sdf.format(time)
        }

        fun getTimeString(time: Date, format: String): String {
            val sdf = SimpleDateFormat(format)
            return sdf.format(time)
        }

        fun getTimeString(time: Date): String {
            val sdf = SimpleDateFormat("EEE, d MMM yyyy")
            return sdf.format(time)
        }
    }
}