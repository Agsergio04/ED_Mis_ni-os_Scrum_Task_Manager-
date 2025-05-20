package org.example.dominio

/**
 * Clase base abstracta para representar una actividad genérica en el sistema.
 *
 * Proporciona propiedades comunes como identificador, fecha de creación, descripción y etiquetas.
 * Incluye un método abstracto [obtenerDetalle] que debe ser implementado por las subclases para
 * proporcionar detalles específicos de la actividad.
 *
 * @property id Identificador único de la actividad.
 * @property fechaCreacion Fecha en la que se creó la actividad, en formato "yyyy-MM-dd".
 * @property descripcion Descripción breve de la actividad.
 * @property etiquetas Etiquetas asociadas a la actividad, separadas por comas.
 * @property estadoTarea Estado actual de la tarea, por defecto es [EstadoTarea.ABIERTA].
 */
abstract class Actividad(
    protected val id: Int,
    private val fechaCreacion: String,
    protected val descripcion: String,
    val etiquetas: String,
    var estadoTarea: EstadoTarea = EstadoTarea.ABIERTA
) {

    /**
     * Método abstracto que debe ser implementado por las subclases para proporcionar
     * una representación detallada de la actividad.
     *
     * @return Cadena de texto con los detalles de la actividad.
     */
    abstract fun obtenerDetalle(): String

    /**
     * Devuelve el identificador único de la actividad.
     *
     * @return Identificador de la actividad.
     */
    fun obtenerId(): Int {
        return id
    }

    /**
     * Cambia el estado de la tarea al siguiente en el ciclo:
     * ABIERTA → EN_PROGRESO → ACABADA → ABIERTA.
     *
     * @param estado Nuevo estado al que se desea cambiar.
     */
    open fun cambiarEstado(estado: EstadoTarea) {
        estadoTarea = when (estado) {
            EstadoTarea.ABIERTA -> EstadoTarea.EN_PROGRESO
            EstadoTarea.EN_PROGRESO -> EstadoTarea.ACABADA
            EstadoTarea.ACABADA -> EstadoTarea.ABIERTA
        }
    }

    companion object {
        private val contadorFechas = mutableMapOf<String, Int>()

        /**
         * Genera un identificador único basado en la fecha proporcionada.
         * El formato del ID es "yyyymmddN", donde N es un contador incremental para ese día.
         *
         * @param fecha Fecha en formato "yyyy-MM-dd".
         * @return Identificador único generado como entero.
         */
        fun generarId(fecha: String): Int {
            val contador = contadorFechas.getOrDefault(fecha, 0) + 1
            contadorFechas[fecha] = contador
            return "${fecha.replace("-", "")}$contador".toInt()
        }
    }
}
