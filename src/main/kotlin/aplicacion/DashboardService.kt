package org.example.aplicacion

import org.example.datos.IActividadRepository
import org.example.dominio.EstadoTarea
import org.example.utilidades.Utils

/**
 * Servicio para calcular métricas y estadísticas de las actividades registradas.
 *
 * @property actividadRepo repositorio de actividades para consultas de estado y fechas
 */
class DashboardService(
    private val actividadRepo: IActividadRepository
) {
    /**
     * Obtiene las métricas de actividades correspondientes al día actual.
     *
     * @return mapa con claves y valores que representan:
     * - "tareasAbiertas": número de tareas en estado ABIERTA
     * - "tareasEnProgreso": número de tareas en estado EN_PROGRESO
     * - "tareasFinalizadas": número de tareas en estado ACABADA
     * - "eventosHoy": lista de eventos cuya fecha coincide con la fecha actual
     * - "tareasConSubtareas": número de tareas que tienen al menos una subtarea asociada
     */
    fun obtenerMetricasHoy(): Map<String, Any> {
        val hoy = Utils.obtenerFechaActual()
        return mapOf(
            "tareasAbiertas" to actividadRepo.contarTareasPorEstado(EstadoTarea.ABIERTA),
            "tareasEnProgreso" to actividadRepo.contarTareasPorEstado(EstadoTarea.EN_PROGRESO),
            "tareasFinalizadas" to actividadRepo.contarTareasPorEstado(EstadoTarea.ACABADA),
            "eventosHoy" to actividadRepo.obtenerEventosEntreFechas(hoy, hoy),
            "tareasConSubtareas" to actividadRepo.contarTareasConSubtareas()
        )
    }

    /**
     * Obtiene las métricas de eventos programados en la próxima semana a partir del día actual.
     *
     * @return mapa con la clave "eventosSemana" y valor con la lista de eventos entre hoy y siete días después
     */
    fun obtenerMetricasSemana(): Map<String, Any> {
        val hoy = java.time.LocalDate.parse(Utils.obtenerFechaActual())
        val finSemana = hoy.plusDays(7).toString()
        return mapOf(
            "eventosSemana" to actividadRepo.obtenerEventosEntreFechas(
                hoy.toString(),
                finSemana
            )
        )
    }
}