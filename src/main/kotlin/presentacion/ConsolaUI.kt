package org.example.presentacion

import org.example.aplicacion.ActividadService
import java.util.*
class ConsolaUI(private val servicio: ActividadService) {
    fun iniciar() {
        var opcion: Int
        do {
            mostrarMenu()
            opcion = leerOpcion()
            when(opcion) {
                1 -> crearActividad()
                2 -> listarActividades()
                3 -> println("Saliendo...")
                else -> println("Opción no válida")
            }
        } while(opcion != 3)
    }
    private fun mostrarMenu() {
        println("\n=== GESTOR DE ACTIVIDADES ===")
        println("1. Crear nueva actividad")
        println("2. Listar todas las actividades")
        println("3. Salir")

        print("Seleccione una opción: ")
    }
    private fun crearActividad() {
        println("\nTipo de actividad:")
        println("1. Tarea")
        println("2. Evento")
        println("3. Cancelar")
        print("Seleccione: ")
        when(leerOpcion()) {
            1 -> crearTarea()
            2 -> crearEvento()
            3 -> return
            else -> println("Opción no válida")
        }
    }
    private fun crearTarea() {
        try {
            print("Descripción de la tarea: ")
            val desc = leerCadena()
            servicio.crearTarea(desc)
            println("Tarea creada exitosamente!")
        } catch(e: IllegalArgumentException) {
            println("Error: ${e.message}")
        }
    }
    private fun crearEvento() {
        try {
            print("Descripción del evento: ")
            val desc = leerCadena()
            print("Fecha (YYYY-MM-DD): ")
            val fecha = leerCadena()
            print("Ubicación: ")
            val ubicacion = leerCadena()
            servicio.crearEvento(desc, fecha, ubicacion)
            println("Evento creado exitosamente!")
        } catch(e: IllegalArgumentException) {
            println("Error: ${e.message}")
        }
    }
    private fun listarActividades() {
        val actividades = servicio.listarActividades()
        if(actividades.isEmpty()) {

            println("\nNo hay actividades registradas")
            return
        }
        println("\n=== LISTADO DE ACTIVIDADES ===")
        actividades.forEach { println(it) }
    }
    private fun leerOpcion(): Int {
        return try {
            Scanner(System.`in`).nextInt()
        } catch(e: Exception) {
            -1
        }
    }
    private fun leerCadena(): String {
        return Scanner(System.`in`).nextLine().trim()
    }
}
