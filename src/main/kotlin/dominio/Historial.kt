package org.example.dominio

/**
 * Representa un registro de historial asociado a una actividad.
 *
 * Esta clase almacena información sobre acciones realizadas en una actividad específica,
 * incluyendo la fecha de la acción, una descripción de la misma y el identificador de la actividad asociada.
 *
 * @property fecha Fecha en la que se realizó la acción, en formato "yyyy-MM-dd".
 * @property descripcion Descripción detallada de la acción realizada.
 * @property idActividad Identificador único de la actividad relacionada con este registro.
 */
data class Historial(
    val fecha: String,
    val descripcion: String,
    val idActividad: Int
)
