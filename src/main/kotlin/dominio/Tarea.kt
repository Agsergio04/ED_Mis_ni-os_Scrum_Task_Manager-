package org.example.dominio

import org.example.utilidades.Utils
import org.example.dominio.EstadoTarea

/**
 * Representa una tarea en el sistema, que puede contener subtareas y estar asignada a un usuario.
 *
 * Esta clase hereda de [Actividad] y añade propiedades específicas de una tarea,
 * como el estado actual, el usuario asignado y una lista de subtareas.
 *
 * @property estado Estado actual de la tarea.
 * @property usuarioAsignado Usuario al que está asignada la tarea.
 * @property subtareas Lista de subtareas asociadas a esta tarea.
 */
class Tarea private constructor(
    id: Int,
    fechaCreacion: String,
    descripcion: String,
    private var estado: EstadoTarea,
    var usuarioAsignado: Usuario? = null,
    etiquetas: String,
    internal val subtareas: MutableList<Tarea> = mutableListOf()
) : Actividad(id, fechaCreacion, descripcion, etiquetas) {

    companion object {
        /**
         * Crea una nueva instancia de [Tarea] con la descripción y etiquetas proporcionadas.
         *
         * @param desc Descripción de la tarea.
         * @param etiquetas Etiquetas asociadas a la tarea.
         * @return Nueva instancia de [Tarea].
         * @throws IllegalArgumentException si la descripción está vacía.
         */
        fun creaInstancia(desc: String, etiquetas: String): Tarea {
            require(desc.isNotEmpty()) { "Descripción no puede estar vacía" }
            val fechaActual = Utils.obtenerFechaActual()
            return Tarea(
                generarId(fechaActual),
                fechaActual,
                desc,
                EstadoTarea.ABIERTA,
                etiquetas = etiquetas
            )
        }
    }

    /**
     * Agrega una subtarea a la lista de subtareas de esta tarea.
     *
     * @param subtarea Subtarea a agregar.
     */
    fun agregarSubtarea(subtarea: Tarea) {
        subtareas.add(subtarea)
    }

    /**
     * Obtiene la lista de subtareas asociadas a esta tarea.
     *
     * @return Lista de subtareas.
     */
    fun obtenerSubtareas(): List<Tarea> = subtareas

    /**
     * Cambia el estado de la tarea al proporcionado, asegurando que no se pueda marcar como
     * finalizada si alguna subtarea aún está abierta.
     *
     * @param estado Nuevo estado al que se desea cambiar.
     * @throws IllegalStateException si se intenta marcar como finalizada con subtareas abiertas.
     */
    override fun cambiarEstado(estado: EstadoTarea) {
        if (estado == EstadoTarea.ACABADA && subtareas.any { it.estadoTarea != EstadoTarea.ACABADA }) {
            throw IllegalStateException("No se puede cerrar la tarea madre con subtareas abiertas")
        }
        super.cambiarEstado(estado)
    }

    /**
     * Obtiene una representación detallada de la tarea.
     *
     * @return Cadena de texto con los detalles de la tarea.
     */
    override fun obtenerDetalle(): String {
        val asignado = usuarioAsignado?.let { " - Asignado a: ${it.nombre}" } ?: ""
        return "Tarea #$id: $descripcion [Estado: ${estado.name}]$asignado | [ETIQUETAS: $etiquetas] "
    }
}
