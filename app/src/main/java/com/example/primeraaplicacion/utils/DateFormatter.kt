package com.example.primeraaplicacion.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {
    private const val DEFAULT_PATTERN = "dd/MM/yyyy"
    private const val FULL_PATTERN = "dd/MM/yyyy HH:mm"

    fun format(date: Date, pattern: String = DEFAULT_PATTERN): String =
        SimpleDateFormat(pattern, Locale.getDefault()).format(date)

    fun formatFull(date: Date): String = format(date, FULL_PATTERN)

    fun today(): String = format(Date())
}
