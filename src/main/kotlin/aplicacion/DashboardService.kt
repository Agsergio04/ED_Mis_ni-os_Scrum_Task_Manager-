import org.example.datos.IActividadRepository
import org.example.dominio.EstadoTarea
import org.example.utilidades.Utils

/**
 * Servicio que proporciona métricas y estadísticas sobre actividades y eventos.
 *
 * Utiliza el repositorio de actividades para obtener datos y calcular métricas
 * relevantes para el dashboard o panel de control.
 *
 * @property actividadRepo Repositorio para acceder a las actividades almacenadas.
 */
class DashboardService(
    private val actividadRepo: IActividadRepository
) {

    /**
     * Obtiene métricas relevantes para el día actual, incluyendo conteo de tareas por estado,
     * eventos programados para hoy y tareas que contienen subtareas.
     *
     * @return Mapa con claves descriptivas y valores correspondientes a las métricas.
     *         Las claves incluyen: "tareasAbiertas", "tareasEnProgreso", "tareasFinalizadas",
     *         "eventosHoy" y "tareasConSubtareas".
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
     * Obtiene métricas de eventos planificados en el rango de una semana a partir del día actual.
     *
     * @return Mapa con la clave "eventosSemana" que contiene la lista de eventos programados
     *         en los próximos 7 días.
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
