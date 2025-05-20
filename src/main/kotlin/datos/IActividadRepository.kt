package org.example.datos

import org.example.dominio.Actividad
import org.example.dominio.EstadoTarea
import org.example.dominio.Evento

/**
 * Interfaz para el repositorio de actividades.
 * Define las operaciones CRUD y consultas especializadas para tareas y eventos.
 */
interface IActividadRepository {

    /**
     * Agrega una nueva actividad al repositorio.
     *
     * @param actividad instancia de Actividad (Tarea o Evento) a almacenar.
     */
    fun aniadirActividad(actividad: Actividad)

    /**
     * Recupera todas las actividades registradas.
     *
     * @return lista de todas las instancias de Actividad.
     */
    fun obtenerTodas(): List<Actividad>

    /**
     * Busca una actividad por su identificador único.
     *
     * @param id identificador de la actividad.
     * @return la actividad correspondiente, o null si no existe.
     */
    fun obtenerPorId(id: Int): Actividad?

    /**
     * Cuenta las tareas que se encuentran en un estado específico.
     *
     * @param estado estado de la tarea (ABIERTA, EN_PROGRESO, ACABADA).
     * @return número de tareas en el estado indicado.
     */
    fun contarTareasPorEstado(estado: EstadoTarea): Int

    /**
     * Recupera los eventos cuya fecha está dentro de un rango.
     *
     * @param inicio fecha inicial en formato "yyyy-MM-dd".
     * @param fin fecha final en formato "yyyy-MM-dd".
     * @return lista de eventos programados entre las fechas inicio y fin (ambas incluidas).
     */
    fun obtenerEventosEntreFechas(inicio: String, fin: String): List<Evento>

    /**
     * Cuenta cuántas tareas tienen al menos una subtarea asociada.
     *
     * @return número de tareas que contienen una o más subtareas.
     */
    fun contarTareasConSubtareas(): Int
}
