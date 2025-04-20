package org.example.aplicacion

import org.example.datos.IActividadRepository
import org.example.datos.IUsuarioRepository
import org.example.aplicacion.GestorTareas
import org.example.dominio.EstadoTarea
import org.example.dominio.Tarea
import org.example.dominio.Evento
import org.example.dominio.Usuario

/**
 * Crea la tarea y evento, ademas las obtiene.
 *
 */


class ActividadService(
    private val actividadRepo: IActividadRepository,
    private val usuarioRepo: IUsuarioRepository
) {
    fun crearTarea(descripcion: String) {
        val tarea = Tarea.creaInstancia(descripcion)
        actividadRepo.aniadirActividad(tarea)
    }

    fun crearEvento(descripcion: String, fecha: String, ubicacion: String) {
        val evento = Evento.creaInstancia(descripcion, fecha, ubicacion)
        actividadRepo.aniadirActividad(evento)
    }

    fun listarActividades(): List<String> {
        return actividadRepo.obtenerTodas().map { it.obtenerDetalle() }
    }

    fun cambiarEstadoTarea(id: Int, nuevoEstado: EstadoTarea) {
        val actividad = actividadRepo.obtenerPorId(id)
        if (actividad is Tarea) {
            actividad.cambiarEstado(nuevoEstado)
        } else {
            throw IllegalArgumentException("No existe una tarea con el ID proporcionado")
        }
    }

    fun crearUsuario(nombre: String): Usuario {
        return usuarioRepo.crearUsuario(nombre)
    }

    fun asignarTarea(idTarea: Int, idUsuario: Int) {
        val tarea = actividadRepo.obtenerPorId(idTarea) as? Tarea
        val usuario = usuarioRepo.obtenerPorId(idUsuario)

        require(tarea != null) { "Tarea no encontrada" }
        require(usuario != null) { "Usuario no encontrado" }

        tarea.usuarioAsignado = usuario
    }

    fun obtenerTareasPorUsuario(idUsuario: Int): List<Tarea> {
        return actividadRepo.obtenerTodas()
            .filterIsInstance<Tarea>()
            .filter { it.usuarioAsignado?.obtenerId() == idUsuario }
    }
}
