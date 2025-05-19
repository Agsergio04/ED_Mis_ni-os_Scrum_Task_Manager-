package org.example.datos

import org.example.dominio.Historial

class HistorialRepository : IHistorialRepository {
    private val registros = mutableListOf<Historial>()

    override fun agregar(historial: Historial) {
        registros.add(historial)
    }

    override fun obtenerPorActividad(idActividad: Int): List<Historial> {
        return registros.filter { it.idActividad == idActividad }
    }
}
