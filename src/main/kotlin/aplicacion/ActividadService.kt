package org.example.aplicacion

import DashboardService
import org.example.datos.IActividadRepository
import org.example.datos.IUsuarioRepository
import org.example.aplicacion.HistorialService
import org.example.dominio.*
import org.example.utilidades.Utils

/**
 * Gestor de la lógica de negocio para actividades (tareas y eventos), usuarios e historial de acciones.
 *
 * @property actividadRepo repositorio para gestionar actividades (Tarea y Evento)
 * @property usuarioRepo repositorio para gestionar usuarios
 * @property historialService servicio para registrar y consultar el historial de acciones
 * @property dashboardService servicio para métricas y panel de control
 */
class ActividadService(
    private val actividadRepo: IActividadRepository,
    private val usuarioRepo: IUsuarioRepository,
    private val historialService: HistorialService,
    val dashboardService: DashboardService
) {

    /**
     * Crea una nueva tarea con descripción y etiquetas asociadas.
     *
     * @param descripcion texto que describe la tarea
     * @param etiquetas cadena de etiquetas separadas por comas
     * @throws IllegalArgumentException si la descripción o etiquetas no cumplen los requisitos de Tarea
     */
    fun crearTarea(descripcion: String, etiquetas: String) {
        val tarea = Tarea.creaInstancia(descripcion, etiquetas)
        actividadRepo.aniadirActividad(tarea)
        println("Tarea creada con ID: ${tarea.obtenerId()}")
        historialService.registrarAccion(
            tarea.obtenerId(),
            "Tarea creada: \"$descripcion\""
        )
    }

    /**
     * Crea un nuevo evento con descripción, fecha, ubicación y etiquetas.
     *
     * @param descripcion texto que describe el evento
     * @param fecha fecha en formato válido ("yyyy-MM-dd")
     * @param ubicacion lugar donde ocurrirá el evento
     * @param etiquetas cadena de etiquetas separadas por comas
     * @throws IllegalArgumentException si la fecha no tiene formato correcto
     */
    fun crearEvento(descripcion: String, fecha: String, ubicacion: String, etiquetas: String) {
        val evento = Evento.creaInstancia(descripcion, fecha, ubicacion, etiquetas)
        actividadRepo.aniadirActividad(evento)
        historialService.registrarAccion(
            evento.obtenerId(),
            "Evento creado: \"$descripcion\" en $fecha @ $ubicacion"
        )
    }

    /**
     * Devuelve todas las actividades almacenadas (tareas y eventos).
     *
     * @return lista de todas las actividades
     */
    fun listarActividades(): List<Actividad> = actividadRepo.obtenerTodas()

    /**
     * Devuelve todos los usuarios registrados.
     *
     * @return lista de usuarios
     */
    fun listarUsuarios(): List<Usuario> = usuarioRepo.obtenerTodos()

    /**
     * Cambia el estado de una tarea identificada por su ID.
     *
     * @param id identificador de la tarea
     * @param nuevoEstado nuevo estado a aplicar (EstadoTarea)
     * @throws IllegalArgumentException si no existe la tarea con el ID proporcionado
     */
    fun cambiarEstadoTarea(id: Int, nuevoEstado: EstadoTarea) {
        val actividad = actividadRepo.obtenerPorId(id)
        if (actividad is Tarea) {
            val estadoAnterior = actividad.estadoTarea.name
            actividad.cambiarEstado(nuevoEstado)
            historialService.registrarAccion(
                id,
                "Estado cambiado: $estadoAnterior → ${nuevoEstado.name}"
            )
        } else {
            throw IllegalArgumentException("No existe una tarea con el ID proporcionado")
        }
    }

    /**
     * Crea un nuevo usuario con el nombre especificado.
     *
     * @param nombre nombre del usuario
     * @return la instancia de Usuario creada
     */
    fun crearUsuario(nombre: String): Usuario = usuarioRepo.crearUsuario(nombre)

    /**
     * Asigna una tarea a un usuario.
     *
     * @param idTarea ID de la tarea a asignar
     * @param idUsuario ID del usuario responsable
     * @throws IllegalArgumentException si la tarea o el usuario no se encuentran
     */
    fun asignarTarea(idTarea: Int, idUsuario: Int) {
        val tarea = actividadRepo.obtenerPorId(idTarea) as? Tarea
        val usuario = usuarioRepo.obtenerPorId(idUsuario)

        require(tarea != null) { "Tarea no encontrada" }
        require(usuario != null) { "Usuario no encontrado" }

        tarea.usuarioAsignado = usuario
        historialService.registrarAccion(
            idTarea,
            "Asignada al usuario #${usuario.obtenerId()}"
        )
    }

    /**
     * Obtiene las tareas asociadas a un usuario específico.
     *
     * @param idUsuario ID del usuario
     * @return lista de tareas asignadas al usuario
     */
    fun obtenerTareasPorUsuario(idUsuario: Int): List<Tarea> {
        return actividadRepo.obtenerTodas()
            .filterIsInstance<Tarea>()
            .filter { it.usuarioAsignado?.obtenerId() == idUsuario }
    }

    /**
     * Recupera el historial de acciones de una actividad.
     *
     * @param idActividad ID de la actividad
     * @return lista de entradas de historial
     */
    fun obtenerHistorial(idActividad: Int): List<Historial> =
        historialService.obtenerHistorial(idActividad)

    /**
     * Asocia una subtarea (hija) a una tarea madre.
     *
     * @param idMadre ID de la tarea principal
     * @param idHija ID de la tarea secundaria a asociar
     * @throws IllegalArgumentException si cualquiera de las tareas no existe
     */
    fun asociarSubtarea(idMadre: Int, idHija: Int) {
        val tareaMadre = actividadRepo.obtenerPorId(idMadre) as? Tarea
            ?: throw IllegalArgumentException("Tarea madre no encontrada")
        val tareaHija = actividadRepo.obtenerPorId(idHija) as? Tarea
            ?: throw IllegalArgumentException("Tarea hija no encontrada")
        tareaMadre.agregarSubtarea(tareaHija)
        historialService.registrarAccion(
            idMadre,
            "Subtarea #${tareaHija.obtenerId()} asociada"
        )
    }

    /**
     * Filtra actividades según varios criterios: tipo, estado, etiquetas, usuario y fecha.
     *
     * @param tipo opcional; "TAREA" para tareas, "EVENTO" para eventos
     * @param estado opcional; estado de la tarea (solo aplica si tipo=TAREA)
     * @param etiquetas opcional; lista de etiquetas que deben contener las actividades
     * @param nombreUsuario opcional; nombre del usuario asignado (solo aplica a tareas)
     * @param fechaFiltro opcional; fecha para filtrar eventos (formato "yyyy-MM-dd")
     * @return lista de actividades que cumplen todos los filtros
     */
    fun filtrarActividades(
        tipo: String? = null,
        estado: EstadoTarea? = null,
        etiquetas: List<String>? = null,
        nombreUsuario: String? = null,
        fechaFiltro: String? = null
    ): List<Actividad> {
        val actividades = listarActividades()
        val usuarios = listarUsuarios()

        val usuario = nombreUsuario?.let { name ->
            usuarios.find { it.nombre.equals(name) }
        }

        return actividades.filter { act ->
            val coincideTipo = when (tipo?.uppercase()) {
                "TAREA" -> act is Tarea
                "EVENTO" -> act is Evento
                else -> true
            }

            val coincideEstado = if (estado != null && act is Tarea) {
                act.estadoTarea == estado
            } else true

            val coincideEtiquetas = etiquetas?.all { tag -> act.etiquetas.contains(tag) } ?: true

            val coincideUsuario = if (usuario != null && act is Tarea) {
                act.usuarioAsignado == usuario
            } else true

            val coincideFecha = if (fechaFiltro != null && act is Evento) {
                Utils.compararFecha(act.fecha, fechaFiltro)
            } else true

            coincideTipo && coincideEstado && coincideEtiquetas && coincideUsuario && coincideFecha
        }
    }
}

