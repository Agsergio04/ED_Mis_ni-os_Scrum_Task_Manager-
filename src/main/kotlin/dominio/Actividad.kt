package org.example.dominio

/**
 * Genera la Id mediante un companion object y aporta la funcion abstracta obtenerDetalle.
 *
 * Esta clase es la base para diferentes subclases.
 * @property id .
 * @property fechaCreacion .
 * @property descripcion .
 */
abstract class Actividad(
    protected val id: Int,
    private val fechaCreacion: String,
    protected val descripcion: String

) {
    abstract fun obtenerDetalle(): String
    companion object {
        private val contadorFechas = mutableMapOf<String, Int>()
        fun generarId(fecha: String): Int {
            val contador = contadorFechas.getOrDefault(fecha, 0) + 1
            contadorFechas[fecha] = contador
            return "${fecha.replace("-", "")}$contador".toInt()
        }
    }
}