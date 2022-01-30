package com.daniyelp.hydrationapp

import java.text.SimpleDateFormat
import java.util.*

fun dateToString(milliseconds: Long, format: String): String {
    val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
    return dateFormatter.format(milliseconds)
}