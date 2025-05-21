package org.example.utilidades

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UtilsTest {
    @Test
    fun testObtenerFechaActual_FormatoCorrecto() {
        val fecha = Utils.obtenerFechaActual()
        // Comprueba que cumple el patrón YYYY-MM-DD
        assertTrue(Regex("\\d{4}-\\d{2}-\\d{2}").matches(fecha))
    }

    @Test
    fun testCompararFecha_EnumValido() {
        // Si la fecha es hoy, devuelve true para FiltroFecha.HOY
        val hoyStr = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        assertTrue(Utils.compararFecha(hoyStr, FiltroFecha.HOY))
    }

    @Test
    fun testCompararFecha_Semana() {
        val hoy = LocalDate.now()
        // Fecha a 5 días: aún dentro de la semana
        val fechaSemana = hoy.plusDays(5).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        assertTrue(Utils.compararFecha(fechaSemana, FiltroFecha.SEMANA))
    }

    @Test
    fun testCompararFecha_Mes() {
        val hoy = LocalDate.now()
        // Primer día del mes actual
        val fechaMes = hoy.withDayOfMonth(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        assertTrue(Utils.compararFecha(fechaMes, FiltroFecha.MES))
    }
}
