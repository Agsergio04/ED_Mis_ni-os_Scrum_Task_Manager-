package org.example.aplicacion

import org.example.datos.IHistorialRepository
import org.example.dominio.Historial
import org.example.utilidades.Utils

/**
 * Servicio para registrar y recuperar el historial de acciones realizadas sobre actividades.
 *
 * @property repo repositorio de historial para persistencia y consulta de registros
 */
class HistorialService(private val repo: IHistorialRepository) {

    /**
     * Registra una nueva entrada en el historial de la actividad especificada.
     *
     * @param idActividad identificador de la actividad asociada a la acción
     * @param accion descripción de la acción realizada
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
     * Obtiene todas las entradas de historial para una actividad dada.
     *
     * @param idActividad identificador de la actividad cuyas entradas de historial se recuperan
     * @return lista de objetos Historial ordenada (según implementación del repositorio)
     */
    fun obtenerHistorial(idActividad: Int): List<Historial> {
        return repo.obtenerPorActividad(idActividad)
    }
}
