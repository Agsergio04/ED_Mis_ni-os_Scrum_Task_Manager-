package org.example

import org.example.aplicacion.ActividadService
import org.example.datos.ActividadRepository
import org.example.datos.IActividadRepository
import org.example.datos.IUsuarioRepository
import org.example.datos.UsuarioRepository
import org.example.presentacion.ConsolaUI

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
// En el punto de entrada de la aplicación se configuran las implementaciones
fun main() {
    val actividadRepo = ActividadRepository()
    val usuarioRepo = UsuarioRepository()
    val servicio = ActividadService(actividadRepo, usuarioRepo)

    ConsolaUI(servicio).iniciar()
}