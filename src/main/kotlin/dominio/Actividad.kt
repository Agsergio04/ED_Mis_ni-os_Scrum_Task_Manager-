package org.example.dominio

import org.example.aplicacion.GestorTareas

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
    protected val descripcion: String,
    protected var estadoTarea : EstadoTarea = EstadoTarea.ABIERTA

) : GestorTareas {
    abstract fun obtenerDetalle(): String

    fun obtenerId() : Int{
        return id
    }

    fun cambiarEstado(estado : EstadoTarea){
        estadoTarea = when(estado){
            EstadoTarea.ABIERTA -> EstadoTarea.EN_PROGRESO
            EstadoTarea.ACABADA -> EstadoTarea.ABIERTA
            EstadoTarea.EN_PROGRESO -> EstadoTarea.ACABADA
        }
    }

    companion object {
        private val contadorFechas = mutableMapOf<String, Int>()
        fun generarId(fecha: String): Int {
            val contador = contadorFechas.getOrDefault(fecha, 0) + 1
            contadorFechas[fecha] = contador
            return "${fecha.replace("-", "")}$contador".toInt()
        }
    }
}