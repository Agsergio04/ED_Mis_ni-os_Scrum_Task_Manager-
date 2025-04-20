package org.example.datos

import org.example.dominio.Actividad

/**
 * Crea la interfaz IActividadRepository.
 *
 * crea las funciones añadirActividad y obtenerTodas
 */
interface IActividadRepository {
    fun aniadirActividad(actividad: Actividad)
    fun obtenerTodas(): List<Actividad>
    fun obtenerPorId(id: Int): Actividad?
}
