package org.example.aplicacion

import DashboardService
import org.example.datos.IActividadRepository
import org.example.datos.IUsuarioRepository
import org.example.aplicacion.HistorialService
import org.example.dominio.*
import org.example.utilidades.Utils

/**
 * Gestiona la lógica de negocio de actividades, usuarios e historial.
 */

class ActividadService(
    private val actividadRepo: IActividadRepository,
    private val usuarioRepo: IUsuarioRepository,
    private val dashboardService: DashboardService,
    private val historialService: HistorialService
) {

    fun crearTarea(descripcion: String, etiquetas: String): Tarea {
        val tarea = buildTarea(descripcion, etiquetas)
        persistirActividad(tarea)
        registrarHistorial(tarea.obtenerId(), "Tarea creada: '$descripcion'")
        return tarea
    }

    fun crearEvento(descripcion: String, fecha: String, ubicacion: String, etiquetas: String): Evento {
        val evento = buildEvento(descripcion, fecha, ubicacion, etiquetas)
        persistirActividad(evento)
        return evento
    }

    fun crearUsuario(nombre: String): Usuario = usuarioRepo.crearUsuario(nombre)


    fun listarActividades(): List<Actividad> = actividadRepo.obtenerTodas()

    fun obtenerTareasPorUsuario(idUsuario: Int): List<Tarea> =
        listarActividades()
            .filterIsInstance<Tarea>()
            .filter { it.usuarioAsignado?.obtenerId() == idUsuario }

    fun obtenerHistorial(idActividad: Int): List<Historial> = historialService.obtenerHistorial(idActividad)


    fun cambiarEstadoTarea(id: Int, nuevoEstado: EstadoTarea) {
        val tarea = fetchTareaById(id)
        val anterior = tarea.estadoTarea
        tarea.cambiarEstado(nuevoEstado)
        registrarHistorial(id, "Estado: ${anterior.name}→${nuevoEstado.name}")
    }

    fun asignarTarea(idTarea: Int, idUsuario: Int) {
        val tarea = fetchTareaById(idTarea)
        val usuario = fetchUsuarioById(idUsuario)
        tarea.usuarioAsignado = usuario
        registrarHistorial(idTarea, "Asignada a usuario ${usuario.obtenerId()}")
    }

    fun asociarSubtarea(idMadre: Int, idHija: Int) {
        val madre = fetchTareaById(idMadre)
        val hija = fetchTareaById(idHija)
        madre.agregarSubtarea(hija)
        registrarHistorial(idMadre, "Subtarea #${hija.obtenerId()} asociada")
    }


    fun filtrarActividades(
        tipo: String? = null,
        estado: EstadoTarea? = null,
        etiquetas: List<String>? = null,
        nombreUsuario: String? = null,
        fechaFiltro: String? = null
    ): List<Actividad> {
        val actividades = listarActividades()
        val usuario = nombreUsuario?.let { findUsuarioByName(it) }

        return actividades.filter { act ->
            checkTipo(act, tipo)
                    && checkEstado(act, estado)
                    && checkEtiquetas(act, etiquetas)
                    && checkUsuario(act, usuario)
                    && checkFecha(act, fechaFiltro)
        }
    }


    private fun buildTarea(descripcion: String, etiquetas: String): Tarea =
        Tarea.creaInstancia(descripcion, etiquetas)

    private fun buildEvento(descripcion: String, fecha: String, ubicacion: String, etiquetas: String): Evento =
        Evento.creaInstancia(descripcion, fecha, ubicacion, etiquetas)

    private fun persistirActividad(actividad: Actividad) =
        actividadRepo.aniadirActividad(actividad)

    private fun registrarHistorial(id: Int, mensaje: String) =
        historialService.registrarAccion(id, mensaje)

    private fun fetchTareaById(id: Int): Tarea {
        val act = actividadRepo.obtenerPorId(id) ?:
        throw IllegalArgumentException("Tarea no encontrada: $id")
        return act as? Tarea ?: throw IllegalArgumentException("No es una tarea: $id")
    }

    private fun fetchUsuarioById(id: Int): Usuario =
        usuarioRepo.obtenerPorId(id) ?: throw IllegalArgumentException("Usuario no encontrado: $id")

    private fun findUsuarioByName(nombre: String): Usuario? =
        usuarioRepo.obtenerTodos().find { it.nombre == nombre }

    private fun checkTipo(act: Actividad, tipo: String?): Boolean = when (tipo?.uppercase()) {
        "TAREA" -> act is Tarea
        "EVENTO" -> act is Evento
        else -> true
    }

    private fun checkEstado(act: Actividad, estado: EstadoTarea?): Boolean = if (estado != null && act is Tarea) {
        act.estadoTarea == estado
    } else true

    private fun checkEtiquetas(act: Actividad, etiquetas: List<String>?): Boolean =
        etiquetas?.all { tag -> act.etiquetas.contains(tag) } ?: true

    private fun checkUsuario(act: Actividad, usuario: Usuario?): Boolean = if (usuario != null && act is Tarea) {
        act.usuarioAsignado == usuario
    } else true

    private fun checkFecha(act: Actividad, fechaFiltro: String?): Boolean = if (fechaFiltro != null && act is Evento) {
        Utils.compararFecha(act.fecha, fechaFiltro)
    } else true
}