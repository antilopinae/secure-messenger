package com.securemessenger.ui.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimestamp(timestamp: Long): String {
    val dataFormat = SimpleDateFormat("h:mm a", Locale.ENGLISH)
    val data = Date(timestamp)
    return dataFormat.format(data)
}