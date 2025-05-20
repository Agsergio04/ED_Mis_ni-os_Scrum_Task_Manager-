package org.example.dominio

import org.example.utilidades.Utils

/**
 * Representa un evento programado en el sistema.
 *
 * Esta clase hereda de [Actividad] y añade propiedades específicas de un evento,
 * como la fecha y la ubicación. Utiliza un constructor privado y un método de
 * fábrica en el companion object para garantizar la validación de los datos al
 * crear una instancia.
 *
 * @property fecha Fecha programada del evento en formato "yyyy-MM-dd".
 * @property ubicacion Ubicación donde se llevará a cabo el evento.
 */
class Evento private constructor(
    id: Int,
    fechaCreacion: String,
    descripcion: String,
    val fecha: String,
    val ubicacion: String,
    etiquetas: String
) : Actividad(id, fechaCreacion, descripcion, etiquetas) {

    /**
     * Companion object que proporciona un método de fábrica para crear instancias de [Evento].
     */
    companion object {
        /**
         * Crea una nueva instancia de [Evento] después de validar los parámetros proporcionados.
         *
         * @param desc Descripción del evento.
         * @param fecha Fecha programada del evento en formato "yyyy-MM-dd".
         * @param ubicacion Ubicación donde se llevará a cabo el evento.
         * @param etiquetas Etiquetas asociadas al evento, separadas por comas.
         * @return Una nueva instancia de [Evento] con los datos proporcionados.
         * @throws IllegalArgumentException si alguno de los parámetros no cumple con los criterios de validación.
         */
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
                etiquetas
            )
        }
    }

    /**
     * Proporciona una representación detallada del evento.
     *
     * @return Cadena de texto que describe el evento, incluyendo su ID, descripción, fecha, ubicación y etiquetas.
     */
    override fun obtenerDetalle(): String {
        return "Evento #$id: $descripcion [Fecha: $fecha, Ubicación: $ubicacion] | [ETIQUETAS: $etiquetas]"
    }
}
