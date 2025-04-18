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
    private var estado: String
) : Actividad(id, fechaCreacion, descripcion) {

    companion object {
        fun creaInstancia(desc: String): Tarea {
            require(desc.isNotEmpty()) { "Descripción no puede estar vacía" }
            val fechaActual = Utils.obtenerFechaActual()
            return Tarea(
                generarId(fechaActual),
                fechaActual,
                desc,
                "ABIERTA"
            )
        }
    }
    override fun obtenerDetalle(): String {
        return "Tarea ${descripcion} [Estado: $estado]"
    }
}
