package org.example.aplicacion  

import org.example.datos.IActividadRepository  
import org.example.datos.IUsuarioRepository  
import org.example.dominio.EstadoTarea  
import org.example.dominio.Tarea  
import org.example.dominio.Evento  
import org.example.dominio.Usuario  
import org.example.dominio.Historial  

/**
 * Gestiona la lógica de negocio de actividades, usuarios e historial.
 */

class ActividadService(  
    private val actividadRepo: IActividadRepository,  
    private val usuarioRepo: IUsuarioRepository,  
    private val historialService: HistorialService  
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
            val estadoAnterior = actividad.estadoTarea.name  
            actividad.cambiarEstado(nuevoEstado)  
            historialService.registrarAccion(id, "Estado cambiado: $estadoAnterior → ${nuevoEstado.name}") 
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
        historialService.registrarAccion(idTarea, "Asignada al usuario #${usuario.obtenerId()}") 
    }  

    fun obtenerTareasPorUsuario(idUsuario: Int): List<Tarea> {  
        return actividadRepo.obtenerTodas()  
            .filterIsInstance<Tarea>()  
            .filter { it.usuarioAsignado?.obtenerId() == idUsuario }  
    }  
    
    fun obtenerHistorial(idActividad: Int): List<Historial> {  
        return historialService.obtenerHistorial(idActividad)  
    }  
}  
