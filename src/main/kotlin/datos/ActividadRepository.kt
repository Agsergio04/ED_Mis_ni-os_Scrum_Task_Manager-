package org.example.datos

import org.example.dominio.Actividad
import org.example.dominio.EstadoTarea
import org.example.dominio.Evento
import org.example.dominio.Tarea

class ActividadRepository() : IActividadRepository {
    private val actividades = mutableListOf<Actividad>()
    override fun aniadirActividad(actividad: Actividad) {
        actividades.add(actividad)
    }

    override fun obtenerTodas(): List<Actividad> {
        return actividades.toList()
    }

    override fun obtenerPorId(id: Int): Actividad? {
        return actividades.find { it.obtenerId() == id }
    }

    override fun contarTareasPorEstado(estado: EstadoTarea): Int {
        return actividades.filterIsInstance<Tarea>()
            .count { it.estadoTarea == estado }
    }

    override fun obtenerEventosEntreFechas(inicio: String, fin: String): List<Evento> {
        return actividades.filterIsInstance<Evento>()
            .filter {
                it.fecha >= inicio && it.fecha <= fin
            }
    }

    override fun contarTareasConSubtareas(): Int {
        return 0
    }
}
