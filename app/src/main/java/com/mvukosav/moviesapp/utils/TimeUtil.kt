package com.mvukosav.moviesapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * This object provides methods for time management
 */
object TimeUtil {
    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy", Locale.getDefault())
        return format.format(date)
    }
}