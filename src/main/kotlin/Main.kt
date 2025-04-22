package org.example

import org.example.aplicacion.ActividadService
import org.example.datos.ActividadRepository
import org.example.datos.IActividadRepository
import org.example.datos.IUsuarioRepository
import org.example.datos.UsuarioRepository
import org.example.presentacion.ConsolaUI

fun main() {
    val actividadRepo = ActividadRepository()
    val usuarioRepo = UsuarioRepository()
    val servicio = ActividadService(actividadRepo, usuarioRepo)

    ConsolaUI(servicio).iniciar()
}