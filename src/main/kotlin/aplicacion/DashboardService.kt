import org.example.datos.IActividadRepository
import org.example.dominio.EstadoTarea
import org.example.utilidades.Utils

class DashboardService(
    private val actividadRepo: IActividadRepository,
) {
    fun obtenerMetricasHoy(): Map<String, Any> {
        val hoy = Utils.obtenerFechaActual()
        return mapOf(
            "tareasAbiertas" to actividadRepo.contarTareasPorEstado(EstadoTarea.ABIERTA),
            "tareasEnProgreso" to actividadRepo.contarTareasPorEstado(EstadoTarea.EN_PROGRESO),
            "tareasFinalizadas" to actividadRepo.contarTareasPorEstado(EstadoTarea.ACABADA),
            "eventosHoy" to actividadRepo.obtenerEventosEntreFechas(hoy, hoy),
            "tareasConSubtareas" to actividadRepo.contarTareasConSubtareas(),
        )
    }

    fun obtenerMetricasSemana(): Map<String, Any> {
        val hoy = java.time.LocalDate.parse(Utils.obtenerFechaActual())
        val finSemana = hoy.plusDays(7).toString()
        return mapOf(
            "eventosSemana" to actividadRepo.obtenerEventosEntreFechas(
                hoy.toString(),
                finSemana,
            ),
        )
    }
}
