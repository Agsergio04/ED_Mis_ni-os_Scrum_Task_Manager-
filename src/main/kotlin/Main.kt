package org.example

import org.example.aplicacion.ActividadRepository
import org.example.aplicacion.ActividadService
import org.example.presentacion.ConsolaUI

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val repositorio = ActividadRepository()
    val servicio = ActividadService(repositorio)
    val consola = ConsolaUI(servicio)

    consola.iniciar()
}
