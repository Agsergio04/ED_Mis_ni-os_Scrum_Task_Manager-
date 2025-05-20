package org.example.datos

import org.example.dominio.Historial

/**
 * Implementación en memoria del repositorio de historial de actividades.
 * Gestiona el almacenamiento y recuperación de registros de historial.
 */
class HistorialRepository : IHistorialRepository {

    /**
     * Lista mutable que almacena todas las entradas de historial.
     */
    private val registros = mutableListOf<Historial>()

    /**
     * Agrega una nueva entrada de historial al repositorio.
     *
     * @param historial objeto Historial que contiene fecha, descripción e id de actividad
     */
    override fun agregar(historial: Historial) {
        registros.add(historial)
    }

    /**
     * Recupera todas las entradas de historial asociadas a una actividad.
     *
     * @param idActividad identificador de la actividad para filtrar registros
     * @return lista de objetos Historial cuyo idActividad coincide con el parámetro
     */
    override fun obtenerPorActividad(idActividad: Int): List<Historial> {
        return registros.filter { it.idActividad == idActividad }
    }
}