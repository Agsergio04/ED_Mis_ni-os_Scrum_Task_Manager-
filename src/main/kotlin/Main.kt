package org.example

import org.example.aplicacion.ActividadService
import org.example.aplicacion.DashboardService
import org.example.aplicacion.HistorialService
import org.example.datos.ActividadRepository
import org.example.datos.HistorialRepository
import org.example.datos.UsuarioRepository
import org.example.presentacion.ConsolaUI

/**
 * Punto de entrada de la aplicación de gestión de actividades.
 *
 * Configura las dependencias in-memory: repositorios de actividades, usuarios e historial,
 * los servicios de historial y dashboard, y por último el servicio principal de actividades.
 * Inicializa la interfaz de consola para la interacción con el usuario.
 */
fun main() {
    // Repositorios en memoria
    val actividadRepo   = ActividadRepository()
    val usuarioRepo     = UsuarioRepository()
    val historialRepo   = HistorialRepository()

    // Servicios de dominio
    val historialService = HistorialService(historialRepo)
    val dashboardService = DashboardService(actividadRepo)
    val servicio = ActividadService(
        actividadRepo    = actividadRepo,
        usuarioRepo      = usuarioRepo,
        historialService = historialService,
        dashboardService = dashboardService
    )

    // Lanzamos la UI de consola
    ConsolaUI(servicio, dashboardService).iniciar()
}
