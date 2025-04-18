package org.example.utilidades

object Utils {
    fun obtenerFechaActual(): String {
        val formato = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return java.time.LocalDate.now().format(formato)
    }
    fun esFechaValida(fecha: String): Boolean {
        return try {
            java.time.LocalDate.parse(fecha)
            true
        } catch (e: Exception) {
            false
        }
    }
}
