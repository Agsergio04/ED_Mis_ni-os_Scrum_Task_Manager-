package org.example.datos

import org.example.dominio.Actividad
import org.example.dominio.EstadoTarea
import org.example.dominio.Evento
import org.example.dominio.Tarea

/**
 * Implementación en memoria del repositorio de actividades.
 * Gestiona el almacenamiento y consulta de tareas y eventos.
 */
class ActividadRepository : IActividadRepository {

    /**
     * Lista mutable que almacena todas las actividades.
     */
    private val actividades = mutableListOf<Actividad>()

    /**
     * Agrega una nueva actividad (tarea o evento) al repositorio.
     *
     * @param actividad instancia de Actividad a almacenar
     */
    override fun aniadirActividad(actividad: Actividad) {
        actividades.add(actividad)
    }

    /**
     * Recupera una copia de todas las actividades registradas.
     *
     * @return lista inmutable de actividades
     */
    override fun obtenerTodas(): List<Actividad> {
        return actividades.toList()
    }

    /**
     * Busca una actividad por su identificador.
     *
     * @param id identificador único de la actividad
     * @return la actividad encontrada o null si no existe
     */
    override fun obtenerPorId(id: Int): Actividad? {
        return actividades.find { it.obtenerId() == id }
    }

    /**
     * Cuenta cuántas tareas se encuentran en un estado específico.
     *
     * @param estado estado de tarea (ABIERTA, EN_PROGRESO, ACABADA)
     * @return número de tareas en el estado proporcionado
     */
    override fun contarTareasPorEstado(estado: EstadoTarea): Int {
        return actividades
            .filterIsInstance<Tarea>()
            .count { it.estadoTarea == estado }
    }

    /**
     * Obtiene todos los eventos cuya fecha está dentro del rango especificado.
     *
     * @param inicio fecha inicial en formato "yyyy-MM-dd"
     * @param fin fecha final en formato "yyyy-MM-dd"
     * @return lista de eventos entre las fechas inicio y fin (incluyentes)
     */
    override fun obtenerEventosEntreFechas(inicio: String, fin: String): List<Evento> {
        return actividades
            .filterIsInstance<Evento>()
            .filter { it.fecha >= inicio && it.fecha <= fin }
    }

    /**
     * Cuenta cuántas tareas tienen al menos una subtarea asociada.
     *
     * @return número de tareas con subtareas
     */
    override fun contarTareasConSubtareas(): Int {
        return actividades
            .filterIsInstance<Tarea>()
            .count { it.subtareas.isNotEmpty() }
    }
}
