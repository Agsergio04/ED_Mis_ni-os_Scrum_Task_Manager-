package org.example.utilidades

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object Utils {
    fun obtenerFechaActual(): String {
        val formato = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return java.time.LocalDate.now().format(formato)
    }
    fun esFechaValida(fecha: String?, formato: String = "yyyy-MM-dd"): Boolean {
        if (fecha.isNullOrBlank()) return false
        return try {
            val formatter = DateTimeFormatter.ofPattern(formato)
            LocalDate.parse(fecha, formatter)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }}

    fun compararFecha(fecha: String, filtro: String): Boolean {
        val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val fechaEvento = LocalDate.parse(fecha, formato)
        val hoy = LocalDate.now()

        return when (filtro.uppercase()) {
            "HOY" -> fechaEvento == hoy
            "MAÑANA" -> fechaEvento == hoy.plusDays(1)
            "SEMANA" -> fechaEvento.isAfter(hoy.minusDays(1)) && fechaEvento.isBefore(hoy.plusWeeks(1))
            "MES" -> fechaEvento.month == hoy.month && fechaEvento.year == hoy.year
            else -> true
        }
    }
}
