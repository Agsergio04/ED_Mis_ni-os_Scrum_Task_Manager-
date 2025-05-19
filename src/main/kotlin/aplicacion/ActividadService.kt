package org.example.aplicacion

import DashboardService
import org.example.datos.IActividadRepository
import org.example.datos.IUsuarioRepository
import org.example.dominio.*
import org.example.utilidades.Utils

/**
 * Gestiona la lógica de negocio de actividades, usuarios e historial.
 */

class ActividadService(
    private val actividadRepo: IActividadRepository,
    private val usuarioRepo: IUsuarioRepository,
    private val historialService: HistorialService,
    val dashboardService: DashboardService,
) {

    fun crearTarea(descripcion: String, etiquetas: String) {
        val tarea = Tarea.creaInstancia(descripcion, etiquetas)
        actividadRepo.aniadirActividad(tarea)
        println("Tarea creada con ID: ${tarea.obtenerId()}")
        historialService.registrarAccion(
            tarea.obtenerId(),
            "Tarea creada: \"$descripcion\"",
        )
    }

    fun crearEvento(descripcion: String, fecha: String, ubicacion: String, etiquetas: String) {
        val evento = Evento.creaInstancia(descripcion, fecha, ubicacion, etiquetas)
        actividadRepo.aniadirActividad(evento)
    }

    fun listarActividades(): List<Actividad> {
        return actividadRepo.obtenerTodas()
    }

    fun listarUsuarios(): List<Usuario> {
        return usuarioRepo.obtenerTodos()
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

    fun asociarSubtarea(idMadre: Int, idHija: Int) {
        val tareaMadre = actividadRepo.obtenerPorId(idMadre) as? Tarea ?: throw IllegalArgumentException("Tarea madre no encontrada")
        val tareaHija = actividadRepo.obtenerPorId(idHija) as? Tarea ?: throw IllegalArgumentException("Tarea hija no encontrada")
        tareaMadre.agregarSubtarea(tareaHija)
        historialService.registrarAccion(idMadre, "Subtarea #${tareaHija.obtenerId()} asociada")
    }

    fun filtrarActividades(
        tipo: String? = null,
        estado: EstadoTarea? = null,
        etiquetas: List<String>? = null,
        nombreUsuario: String? = null,
        fechaFiltro: String? = null,
    ): List<Actividad> {
        val actividades = listarActividades()
        val usuarios = listarUsuarios()

        val usuario = nombreUsuario?.let {
            usuarios.find { it.nombre.equals(it) }
        }

        return actividades.filter { act ->
            val coincideTipo = when (tipo?.uppercase()) {
                "TAREA" -> act is Tarea
                "EVENTO" -> act is Evento
                else -> true
            }

            val coincideEstado = if (estado != null && act is Tarea) {
                act.estadoTarea == estado
            } else {
                true // Si no se especifica estado, lo dejamos pasar
            }

            val coincideEtiquetas = etiquetas?.all { tag -> act.etiquetas.contains(tag) } ?: true

            val coincideUsuario = if (usuario != null && act is Tarea) {
                act.usuarioAsignado == usuario
            } else {
                true
            }

            val coincideFecha = if (fechaFiltro != null && act is Evento) {
                Utils.compararFecha(act.fecha, fechaFiltro)
            } else {
                true
            }

            coincideTipo && coincideEstado && coincideEtiquetas && coincideUsuario && coincideFecha
        }
    }
}
