package org.example.utilidades

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Objeto utilitario que proporciona funciones auxiliares relacionadas con fechas.
 *
 * Esta clase contiene métodos estáticos para obtener la fecha actual, validar fechas
 * en formato ISO, y comparar fechas con filtros predefinidos como "HOY", "MAÑANA", "SEMANA" o "MES".
 */
object Utils {

    /**
     * Obtiene la fecha actual del sistema en formato "yyyy-MM-dd".
     *
     * @return Cadena que representa la fecha actual, útil para registros y comparaciones.
     */
    fun obtenerFechaActual(): String {
        val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDate.now().format(formato)
    }

    /**
     * Verifica si una cadena representa una fecha válida en formato ISO ("yyyy-MM-dd").
     *
     * @param fecha Cadena a validar como fecha.
     * @return `true` si la fecha es válida; de lo contrario, `false`.
     */
    fun esFechaValida(fecha: String): Boolean {
        return try {
            LocalDate.parse(fecha)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Compara una fecha con un filtro temporal predefinido.
     *
     * @param fecha Fecha del evento en formato "dd/MM/yyyy".
     * @param filtro Palabra clave que define el período de comparación:
     *               puede ser "HOY", "MAÑANA", "SEMANA", "MES".
     * @return `true` si la fecha coincide con el filtro; `false` en caso contrario.
     */
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
