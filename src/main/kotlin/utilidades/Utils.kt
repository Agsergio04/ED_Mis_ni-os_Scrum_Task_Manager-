package org.example.utilidades

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object Utils {
    // Patrón 1: Extract Method (Extract Function)
    private fun getFormatter(pattern: String): DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)

    fun obtenerFechaActual(): String = LocalDate.now().format(getFormatter("yyyy-MM-dd"))

    fun esFechaValida(fecha: String): Boolean =
        try {
            LocalDate.parse(fecha)
            true
        } catch (e: DateTimeParseException) {
            false
        }

    // Patrón 3: Simplify Conditional (Extract Condition)
    fun compararFecha(
        fecha: String,
        filtro: FiltroFecha,
    ): Boolean {
        val fechaEvento = LocalDate.parse(fecha, getFormatter("dd/MM/yyyy"))
        val hoy = LocalDate.now()

        return when (filtro) {
            FiltroFecha.HOY -> isToday(fechaEvento, hoy)
            FiltroFecha.MAÑANA -> isTomorrow(fechaEvento, hoy)
            FiltroFecha.SEMANA -> isWithinWeek(fechaEvento, hoy)
            FiltroFecha.MES -> isSameMonth(fechaEvento, hoy)
            FiltroFecha.TODOS -> true
        }
    }

    private fun isToday(
        fechaEvento: LocalDate,
        hoy: LocalDate,
    ): Boolean = fechaEvento == hoy

    private fun isTomorrow(
        fechaEvento: LocalDate,
        hoy: LocalDate,
    ): Boolean = fechaEvento == hoy.plusDays(1)

    private fun isWithinWeek(
        fechaEvento: LocalDate,
        hoy: LocalDate,
    ): Boolean = fechaEvento.isAfter(hoy.minusDays(1)) && fechaEvento.isBefore(hoy.plusWeeks(1))

    private fun isSameMonth(
        fechaEvento: LocalDate,
        hoy: LocalDate,
    ): Boolean = fechaEvento.month == hoy.month && fechaEvento.year == hoy.year
}
