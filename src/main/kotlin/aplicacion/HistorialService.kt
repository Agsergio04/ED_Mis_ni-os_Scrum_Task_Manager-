package org.example.aplicacion

import org.example.datos.IHistorialRepository
import org.example.dominio.Historial
import org.example.utilidades.Utils

/**
 * Servicio encargado de gestionar el historial de acciones asociadas a actividades.
 *
 * Esta clase actúa como intermediario entre la lógica de aplicación y el repositorio de historial.
 * Su propósito es registrar y recuperar eventos realizados sobre una actividad específica.
 *
 * @property repo Repositorio que maneja la persistencia del historial.
 */
class HistorialService(private val repo: IHistorialRepository) {

    /**
     * Registra una nueva acción en el historial de una actividad específica.
     *
     * @param idActividad Identificador de la actividad sobre la cual se registra la acción.
     * @param accion Descripción de la acción realizada.
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
     * Obtiene el historial de acciones asociadas a una actividad.
     *
     * @param idActividad Identificador de la actividad cuyo historial se desea consultar.
     * @return Una lista de objetos [Historial] correspondientes a la actividad.
     */
    fun obtenerHistorial(idActividad: Int): List<Historial> {
        return repo.obtenerPorActividad(idActividad)
    }
}
