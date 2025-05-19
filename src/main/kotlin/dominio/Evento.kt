package org.example.dominio

import org.example.utilidades.Utils
/**
 * Crea la instancia de un evento.
 *
 * Esta clase es la que crea las instancias de evento.
 * @property id .
 * @property fechaCreacion .
 * @property descripcion .
 * @property fecha .
 * @property ubicacion .
 */

class Evento private constructor(
    id: Int,
    fechaCreacion: String,
    descripcion: String,
    val fecha: String,
    val ubicacion: String,
    etiquetas: String,
) : Actividad(id, fechaCreacion, descripcion, etiquetas) {

    companion object {
        fun creaInstancia(desc: String, fecha: String, ubicacion: String, etiquetas: String): Evento {
            require(desc.isNotEmpty()) { "Descripción no puede estar vacía" }
            require(Utils.esFechaValida(fecha)) { "Fecha inválida" }
            require(ubicacion.isNotEmpty()) { "Ubicación no puede estar vacía" }
            val fechaActual = Utils.obtenerFechaActual()
            return Evento(
                generarId(fechaActual),
                fechaActual,
                desc,
                fecha,
                ubicacion,
                etiquetas,
            )
        }
    }

    override fun obtenerDetalle(): String {
        return "Evento #$id: $descripcion [Fecha: $fecha, Ubicación: $ubicacion] | [ETIQUETAS: $etiquetas] "
    }
}
