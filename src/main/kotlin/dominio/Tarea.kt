package org.example.dominio

import org.example.utilidades.Utils

/**
 * Crea la instancia de una tarea.
 *
 * Esta clase es la que crea las instancias de tarea.
 * @property id .
 * @property fechaCreacion .
 * @property descripcion .
 * @property estado .
 */

class Tarea private constructor(
    id: Int,
    fechaCreacion: String,
    descripcion: String,
    private var estado: EstadoTarea,
    var usuarioAsignado: Usuario? = null,
    etiquetas: String,
    val subtareas: MutableList<Tarea> = mutableListOf()
) : Actividad(id, fechaCreacion, descripcion, etiquetas) {

    companion object {
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

    fun agregarSubtarea(subtarea: Tarea) {
        subtareas.add(subtarea)
    }

    fun obtenerSubtareas(): List<Tarea> = subtareas

    override fun cambiarEstado(estado: EstadoTarea) {
        if (estado == EstadoTarea.ACABADA && subtareas.any { it.estadoTarea != EstadoTarea.ACABADA }) {
            throw IllegalStateException("No se puede cerrar la tarea madre con subtareas abiertas")
        }
        super.cambiarEstado(estado)
    }
    
    override fun obtenerDetalle(): String {
        val asignado = usuarioAsignado?.let { " - Asignado a: ${it.nombre}" } ?: ""
        return "Tarea #$id: $descripcion [Estado: ${estado.name}]$asignado | [ETIQUETAS: $etiquetas] "
    }
}
