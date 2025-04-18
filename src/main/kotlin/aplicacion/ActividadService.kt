package org.example.aplicacion

import org.example.datos.IActividadRepository
import org.example.dominio.Tarea
import org.example.dominio.Evento
/**
 * Crea la tarea y evento, ademas las obtiene.
 *
 */
class ActividadService(private val repo: IActividadRepository) {
    fun crearTarea(descripcion: String) {
        val tarea = Tarea.creaInstancia(descripcion)
        repo.aniadirActividad(tarea)
    }
    fun crearEvento(descripcion: String, fecha: String, ubicacion: String) {
        val evento = Evento.creaInstancia(descripcion, fecha, ubicacion)
        repo.aniadirActividad(evento)
    }
    fun listarActividades(): List<String> {
        return repo.obtenerTodas().map { it.obtenerDetalle() }
    }
}
