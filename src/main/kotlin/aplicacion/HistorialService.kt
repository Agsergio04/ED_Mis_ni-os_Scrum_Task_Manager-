package org.example.aplicacion  

import org.example.datos.IHistorialRepository
import org.example.dominio.Historial
import org.example.utilidades.Utils  

class HistorialService(private val repo: IHistorialRepository) {
    fun registrarAccion(idActividad: Int, accion: String) {
        repo.agregar(
            Historial(
                fecha = Utils.obtenerFechaActual(),
                descripcion = accion,
                idActividad = idActividad
            )
        )
    }

    fun obtenerHistorial(idActividad: Int): List<Historial> {
        return repo.obtenerPorActividad(idActividad)
    }
}
