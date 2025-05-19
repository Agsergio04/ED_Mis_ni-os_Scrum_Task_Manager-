package org.example.datos

import org.example.dominio.Actividad
import org.example.dominio.EstadoTarea
import org.example.dominio.Evento

/**
 * Crea la interfaz IActividadRepository.
 *
 * crea las funciones añadirActividad y obtenerTodas
 */
interface IActividadRepository {
    fun aniadirActividad(actividad: Actividad)
    fun obtenerTodas(): List<Actividad>
    fun obtenerPorId(id: Int): Actividad?
    fun contarTareasPorEstado(estado: EstadoTarea): Int
    fun obtenerEventosEntreFechas(inicio: String, fin: String): List<Evento>
    fun contarTareasConSubtareas(): Int
}
