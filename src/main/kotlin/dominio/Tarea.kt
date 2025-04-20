package org.example.dominio

import org.example.aplicacion.GestorTareas
import org.example.utilidades.Utils
import org.example.dominio.EstadoTarea
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
    var usuarioAsignado: Usuario? = null
) : Actividad(id, fechaCreacion, descripcion) {

    companion object {
        fun creaInstancia(desc: String): Tarea {
            require(desc.isNotEmpty()) { "Descripción no puede estar vacía" }
            val fechaActual = Utils.obtenerFechaActual()
            return Tarea(
                generarId(fechaActual),
                fechaActual,
                desc,
                EstadoTarea.ABIERTA
            )
        }
    }

    override fun obtenerDetalle(): String {
        val asignado = usuarioAsignado?.let { " - Asignado a: ${it.nombre}" } ?: ""
        return "Tarea #$id: $descripcion [Estado: ${estado.name}]$asignado"
    }
}