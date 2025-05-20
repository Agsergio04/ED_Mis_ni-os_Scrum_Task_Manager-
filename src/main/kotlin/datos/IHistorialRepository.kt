package org.example.datos

import org.example.dominio.Historial

/**
 * Interfaz para el repositorio de historial de actividades.
 * Define las operaciones para gestionar registros de historial asociados a actividades.
 */
interface IHistorialRepository {

    /**
     * Agrega una nueva entrada de historial al repositorio.
     *
     * @param historial objeto Historial que contiene fecha, descripción e id de la actividad.
     */
    fun agregar(historial: Historial)

    /**
     * Recupera todas las entradas de historial para una actividad específica.
     *
     * @param idActividad identificador de la actividad cuyos registros se desean obtener.
     * @return lista de objetos Historial asociados a la actividad.
     */
    fun obtenerPorActividad(idActividad: Int): List<Historial>

}