package org.example.datos  

import org.example.dominio.Historial  

interface IHistorialRepository {
    fun agregar(historial: Historial)
    fun obtenerPorActividad(idActividad: Int): List<Historial>
}
