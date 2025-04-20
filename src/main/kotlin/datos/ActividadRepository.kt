package org.example.datos

import org.example.dominio.Actividad

class ActividadRepository : IActividadRepository {
    private val actividades = mutableListOf<Actividad>()
    override fun aniadirActividad(actividad: Actividad) {
        actividades.add(actividad)
    }

    override fun obtenerTodas(): List<Actividad> {
        return actividades.toList()
    }

    override fun obtenerPorId(id: Int): Actividad? {
        return actividades.find {it.obtenerId() == id }
    }
}
