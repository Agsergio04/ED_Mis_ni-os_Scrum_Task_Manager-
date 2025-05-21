package org.example.aplicacion

import DashboardService
import org.example.datos.IActividadRepository
import org.example.datos.IUsuarioRepository
import org.example.aplicacion.HistorialService
import org.example.dominio.*
import org.example.utilidades.Utils

/**
 * Gestiona la lógica de negocio relacionada con actividades, usuarios e historial.
 *
 * Coordina la creación, actualización, asignación y filtrado de tareas y eventos,
 * además de mantener el historial de acciones y la integración con el dashboard.
 *
 * @property actividadRepo Repositorio para operaciones con actividades.
 * @property usuarioRepo Repositorio para operaciones con usuarios.
 * @property historialService Servicio para registrar y consultar el historial.
 * @property dashboardService Servicio para generar información del dashboard.
 */
class ActividadService(
    private val actividadRepo: IActividadRepository,
    private val usuarioRepo: IUsuarioRepository,
    private val historialService: HistorialService,
    val dashboardService: DashboardService
) {

    /**
     * Crea una nueva tarea con descripción y etiquetas proporcionadas,
     * la añade al repositorio y registra la acción en el historial.
     *
     * @param descripcion Descripción breve de la tarea.
     * @param etiquetas Etiquetas asociadas a la tarea, separadas por comas.
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
     * Crea un nuevo evento con los datos indicados y lo añade al repositorio.
     *
     * @param descripcion Descripción del evento.
     * @param fecha Fecha en formato esperado del evento.
     * @param ubicacion Lugar donde se realiza el evento.
     * @param etiquetas Etiquetas asociadas al evento.
     */
    fun crearEvento(descripcion: String, fecha: String, ubicacion: String, etiquetas: String) {
        val evento = Evento.creaInstancia(descripcion, fecha, ubicacion, etiquetas)
        actividadRepo.aniadirActividad(evento)
    }

    /**
     * Obtiene la lista completa de actividades (tareas y eventos) almacenadas.
     *
     * @return Lista de todas las actividades.
     */
    fun listarActividades(): List<Actividad> {
        return actividadRepo.obtenerTodas()
    }

    /**
     * Obtiene la lista completa de usuarios registrados.
     *
     * @return Lista de todos los usuarios.
     */
    fun listarUsuarios(): List<Usuario> {
        return usuarioRepo.obtenerTodos()
    }

    /**
     * Cambia el estado de una tarea específica, actualiza el historial con el cambio.
     *
     * @param id Identificador de la tarea.
     * @param nuevoEstado Nuevo estado que se asignará a la tarea.
     *
     * @throws IllegalArgumentException Si el ID no corresponde a una tarea.
     */
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

    /**
     * Crea un nuevo usuario con el nombre dado y lo almacena en el repositorio.
     *
     * @param nombre Nombre del usuario a crear.
     * @return Instancia del usuario creado.
     */
    fun crearUsuario(nombre: String): Usuario {
        return usuarioRepo.crearUsuario(nombre)
    }

    /**
     * Asigna una tarea a un usuario específico y registra la acción en el historial.
     *
     * @param idTarea ID de la tarea a asignar.
     * @param idUsuario ID del usuario al que se asigna la tarea.
     *
     * @throws IllegalArgumentException Si la tarea o usuario no existen.
     */
    fun asignarTarea(idTarea: Int, idUsuario: Int) {
        val tarea = actividadRepo.obtenerPorId(idTarea) as? Tarea
        val usuario = usuarioRepo.obtenerPorId(idUsuario)

        require(tarea != null) { "Tarea no encontrada" }
        require(usuario != null) { "Usuario no encontrado" }

        tarea.usuarioAsignado = usuario
        historialService.registrarAccion(idTarea, "Asignada al usuario #${usuario.obtenerId()}")
    }

    /**
     * Obtiene todas las tareas asignadas a un usuario dado por su ID.
     *
     * @param idUsuario ID del usuario.
     * @return Lista de tareas asignadas a ese usuario.
     */
    fun obtenerTareasPorUsuario(idUsuario: Int): List<Tarea> {
        return actividadRepo.obtenerTodas()
            .filterIsInstance<Tarea>()
            .filter { it.usuarioAsignado?.obtenerId() == idUsuario }
    }

    /**
     * Obtiene el historial de acciones asociado a una actividad específica.
     *
     * @param idActividad ID de la actividad.
     * @return Lista de entradas del historial para la actividad.
     */
    fun obtenerHistorial(idActividad: Int): List<Historial> {
        return historialService.obtenerHistorial(idActividad)
    }

    /**
     * Asocia una subtarea a una tarea principal y registra esta acción.
     *
     * @param idMadre ID de la tarea madre.
     * @param idHija ID de la tarea subtarea.
     *
     * @throws IllegalArgumentException Si alguna tarea no existe.
     */
    fun asociarSubtarea(idMadre: Int, idHija: Int) {
        val tareaMadre = actividadRepo.obtenerPorId(idMadre) as? Tarea
            ?: throw IllegalArgumentException("Tarea madre no encontrada")
        val tareaHija = actividadRepo.obtenerPorId(idHija) as? Tarea
            ?: throw IllegalArgumentException("Tarea hija no encontrada")
        tareaMadre.agregarSubtarea(tareaHija)
        historialService.registrarAccion(idMadre, "Subtarea #${tareaHija.obtenerId()} asociada")
    }

    /**
     * Filtra las actividades según varios criterios opcionales:
     * tipo, estado, etiquetas, nombre de usuario asignado y fecha.
     *
     * @param tipo Tipo de actividad: "TAREA" o "EVENTO". Opcional.
     * @param estado Estado de la tarea (si aplica). Opcional.
     * @param etiquetas Lista de etiquetas que deben contener las actividades. Opcional.
     * @param nombreUsuario Nombre del usuario asignado a la tarea. Opcional.
     * @param fechaFiltro Fecha para filtrar eventos. Opcional.
     * @return Lista de actividades que cumplen los criterios.
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
            } else true  // Si no se especifica estado, lo dejamos pasar

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
