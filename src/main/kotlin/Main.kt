package org.example

import DashboardService
import org.example.aplicacion.ActividadService
import org.example.aplicacion.HistorialService
import org.example.datos.ActividadRepository
import org.example.datos.HistorialRepository
import org.example.datos.UsuarioRepository
import org.example.presentacion.ConsolaUI

/**
 * Punto de entrada de la aplicación.
 *
 * Se encarga de inicializar los repositorios, servicios y la interfaz de usuario,
 * y luego inicia el flujo principal a través de la consola.
 */
fun main() {
    // Repositorio para la gestión de actividades
    val actividadRepo = ActividadRepository()

    // Repositorio para la gestión de usuarios
    val usuarioRepo = UsuarioRepository()

    // Repositorio para el historial de acciones
    val historialRepo = HistorialRepository()

    // Servicio que gestiona la lógica del historial de actividades
    val historialService = HistorialService(historialRepo)

    // Servicio que genera información resumida y paneles (dashboard)
    val dashboardService = DashboardService(actividadRepo)

    // Servicio principal que coordina la lógica de actividades
    val servicio = ActividadService(
        actividadRepo = actividadRepo,
        usuarioRepo = usuarioRepo,
        historialService = historialService,
        dashboardService = dashboardService
    )

    // Interfaz de usuario por consola que inicia la interacción
    ConsolaUI(servicio, dashboardService).iniciar()
}
