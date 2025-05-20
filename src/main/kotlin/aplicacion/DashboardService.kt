import org.example.datos.IActividadRepository
import org.example.dominio.EstadoTarea
import org.example.utilidades.Utils

/**
 * Servicio encargado de recopilar métricas para el panel de control (dashboard).
 *
 * Este servicio obtiene datos agregados como el número de tareas por estado, eventos del día
 * y tareas con subtareas, con el objetivo de proporcionar una visión general del estado actual del sistema.
 *
 * @property actividadRepo Repositorio que gestiona el acceso a las actividades y tareas.
 */
class DashboardService(
    private val actividadRepo: IActividadRepository
) {

    /**
     * Obtiene métricas relevantes del día actual.
     *
     * Las métricas incluyen:
     * - Número de tareas abiertas
     * - Tareas en progreso
     * - Tareas finalizadas
     * - Eventos programados para hoy
     * - Tareas que tienen subtareas asociadas
     *
     * @return Un mapa con las métricas del día, donde la clave es el nombre de la métrica y el valor es su resultado.
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
     * Obtiene métricas relacionadas con los eventos programados para los próximos 7 días.
     *
     * Calcula el rango desde el día actual hasta siete días después, y recupera los eventos en ese periodo.
     *
     * @return Un mapa con la métrica `eventosSemana`, que contiene los eventos planificados en la semana.
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
