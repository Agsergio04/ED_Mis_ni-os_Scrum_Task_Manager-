package org.example.dominio

/**
 * Representa los posibles estados de una tarea en el sistema.
 *
 * Los estados siguen un ciclo de vida típico de una tarea:
 * - [ABIERTA]: La tarea ha sido creada y aún no ha comenzado.
 * - [EN_PROGRESO]: La tarea está actualmente en ejecución.
 * - [ACABADA]: La tarea ha sido completada.
 *
 * Esta enumeración se utiliza para controlar y reflejar el estado actual de una tarea.
 */
enum class EstadoTarea {
    /** La tarea ha sido creada y aún no ha comenzado. */
    ABIERTA,

    /** La tarea está actualmente en ejecución. */
    EN_PROGRESO,

    /** La tarea ha sido completada. */
    ACABADA
}
