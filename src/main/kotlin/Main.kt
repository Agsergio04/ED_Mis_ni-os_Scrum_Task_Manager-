package org.example  

import org.example.aplicacion.ActividadService  
import org.example.aplicacion.HistorialService  
import org.example.datos.ActividadRepository  
import org.example.datos.HistorialRepository  
import org.example.datos.UsuarioRepository  
import org.example.presentacion.ConsolaUI  

fun main() {  
    val actividadRepo = ActividadRepository()  
    val usuarioRepo = UsuarioRepository()  
    val historialRepo = HistorialRepository()
    val historialService = HistorialService(historialRepo)
    val servicio = ActividadService(  
        actividadRepo = actividadRepo,  
        usuarioRepo = usuarioRepo,  
        historialService = historialService
    )  
    ConsolaUI(servicio).iniciar()  
}  
