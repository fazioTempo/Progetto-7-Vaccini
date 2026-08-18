package com.example.progetto_7_vaccini.data

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {
    private val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ITALY).apply {
        isLenient = false
        timeZone = TimeZone.getTimeZone("UTC")
    }

    fun formatTimestamp(timestamp: Long): String {
        return formatter.format(Date(timestamp))
    }

    fun calculateAge(birthDate: String): Int? {
        return try {
            val date = formatter.parse(birthDate) ?: return null
            val birth = Calendar.getInstance().apply { time = date }
            val today = Calendar.getInstance()
            
            var age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR)
            
            if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
                age--
            }
            age
        } catch (e: Exception) {
            null
        }
    }

    fun isValidDate(dateStr: String): Boolean {
        return try {
            formatter.parse(dateStr)
            true
        } catch (e: Exception) {
            false
        }
    }
}
