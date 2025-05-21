package org.example.aplicacion

import org.example.datos.IHistorialRepository
import org.example.dominio.Historial
import org.example.utilidades.Utils

/**
 * Servicio encargado de gestionar el historial de acciones realizadas sobre actividades.
 *
 * Permite registrar nuevas acciones y consultar el historial asociado a una actividad específica.
 *
 * @property repo Repositorio para almacenar y recuperar entradas de historial.
 */
class HistorialService(private val repo: IHistorialRepository) {

    /**
     * Registra una acción realizada sobre una actividad, guardando la descripción y la fecha actual.
     *
     * @param idActividad Identificador de la actividad a la que se asocia la acción.
     * @param accion Descripción textual de la acción realizada.
     */
    fun registrarAccion(idActividad: Int, accion: String) {
        repo.agregar(
            Historial(
                fecha = Utils.obtenerFechaActual(),
                descripcion = accion,
                idActividad = idActividad
            )
        )
    }

    /**
     * Obtiene el historial completo de acciones registradas para una actividad dada.
     *
     * @param idActividad Identificador de la actividad.
     * @return Lista de entradas de historial relacionadas con la actividad.
     */
    fun obtenerHistorial(idActividad: Int): List<Historial> {
        return repo.obtenerPorActividad(idActividad)
    }
}
