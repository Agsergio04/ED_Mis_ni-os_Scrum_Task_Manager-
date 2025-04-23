package org.example.presentacion

import org.example.aplicacion.ActividadService
import org.example.dominio.EstadoTarea
import java.util.*
class ConsolaUI(private val servicio: ActividadService) {  
    private fun mostrarMenu() {  
    println("\n=== GESTOR DE ACTIVIDADES ===")  
    println("1. Crear nueva actividad")  
    println("2. Listar todas las actividades")  
    println("3. Cambiar estado de tarea")  
    println("4. Crear usuario")  
    println("5. Asignar tarea a usuario")  
    println("6. Listar tareas por usuario")  
    println("7. Ver historial de actividad") 
    println("8. Panel de control (Dashboard)")
    println("9. Asociar subtarea a tarea madre")
    println("10. Salir")
    print("Seleccione una opción: ")
}

fun iniciar() {  
    var opcion: Int  
    do {  
        mostrarMenu()  
        opcion = leerOpcion()  
        when(opcion) {  
            1 -> crearActividad()  
            2 -> listarActividades()  
            3 -> cambiarEstadoTarea()  
            4 -> crearUsuario()  
            5 -> asignarTarea()  
            6 -> listarTareasPorUsuario()  
            7 -> verHistorial()
            8 -> mostrarDashboard()
            9 -> asociarSubtarea()
            10 -> println("Saliendo...")  
            else -> println("Opción no válida")  
        }  
    } while(opcion != 10)
}

    private fun crearUsuario() {
        try {
            print("Nombre del usuario: ")
            val nombre = leerCadena()
            val usuario = servicio.crearUsuario(nombre)
            println("Usuario creado con ID: ${usuario.obtenerId()}")
        } catch(e: Exception) {
            println("Error al crear usuario: ${e.message}")
        }
    }

    private fun asignarTarea() {
        try {
            print("ID de la tarea: ")
            val idTarea = leerCadena().toInt()
            print("ID del usuario: ")
            val idUsuario = leerCadena().toInt()
            servicio.asignarTarea(idTarea, idUsuario)
            println("Tarea asignada correctamente")
        } catch(e: NumberFormatException) {
            println("Error: ID debe ser un número")
        } catch(e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun listarTareasPorUsuario() {
        try {
            print("ID del usuario: ")
            val idUsuario = leerCadena().toInt()
            val tareas = servicio.obtenerTareasPorUsuario(idUsuario)
            if (tareas.isEmpty()) {
                println("No hay tareas asignadas a este usuario")
            } else {
                println("\n=== TAREAS ASIGNADAS ===")
                tareas.forEach { println(it.obtenerDetalle()) }
            }
        } catch(e: NumberFormatException) {
            println("Error: ID debe ser un número")
        }
    }


    private fun cambiarEstadoTarea() {
        try {
            println("\n=== CAMBIAR ESTADO DE TAREA ===")
            print("Ingrese el ID de la tarea: ")
            val id = leerCadena().toInt()
            print("Seleccione el nuevo estado (1. ABIERTA, 2. EN_PROGRESO, 3. ACABADA): ")
            when(leerOpcion()) {
                1 -> servicio.cambiarEstadoTarea(id, EstadoTarea.ABIERTA)
                2 -> servicio.cambiarEstadoTarea(id, EstadoTarea.EN_PROGRESO)
                3 -> servicio.cambiarEstadoTarea(id, EstadoTarea.ACABADA)
                else -> println("Opción no válida")
            }
            println("Estado actualizado exitosamente!")
        } catch(e: NumberFormatException) {
            println("Error: ID debe ser un número")
        } catch(e: IllegalArgumentException) {
            println("Error: ${e.message}")
        }
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

private fun verHistorial() {  
        try {  
            print("ID de la actividad: ")  
            val id = leerCadena().toInt()  
            val historial = servicio.obtenerHistorial(id)  
            if (historial.isEmpty()) {  
                println("No hay registros para esta actividad")  
            } else {  
                println("\n=== HISTORIAL (#$id) ===")  
                historial.forEach {  
                    println("${it.fecha} - ${it.descripcion}")  
                }  
            }  
        } catch(e: NumberFormatException) {  
            println("Error: ID debe ser un número")  
        } catch(e: Exception) {  
            println("Error: ${e.message}")  
        }  
    }  


fun asociarSubtarea() {
    try {
        print("ID de la tarea madre: ")
        val idMadre = leerCadena().toInt()
        print("ID de la subtarea: ")
        val idHija = leerCadena().toInt()
        servicio.asociarSubtarea(idMadre, idHija)
        println("Subtarea asociada correctamente")
    } catch(e: Exception) {
        println("Error: ${e.message}")
    }
}


private fun mostrarDashboard() {
    println("\n=== PANEL DE CONTROL ===")
    
    
    val metricasHoy = servicio.dashboardService.obtenerMetricasHoy()
    println("\n📊 ESTADO ACTUAL DE TAREAS:")
    println("  - Abiertas: ${metricasHoy["tareasAbiertas"]}")
    println("  - En progreso: ${metricasHoy["tareasEnProgreso"]}")
    println("  - Finalizadas: ${metricasHoy["tareasFinalizadas"]}")
    
   
    val eventosHoy = metricasHoy["eventosHoy"] as List<Evento>
    println("\n📅 EVENTOS PARA HOY (${eventosHoy.size}):")
    eventosHoy.take(3).forEach { 
        println("  - ${it.obtenerDetalle()}") 
    }
    if (eventosHoy.size > 3) {
        println("  ... y ${eventosHoy.size - 3} más")
    }
    
   
    val metricasSemana = servicio.dashboardService.obtenerMetricasSemana()
    val eventosSemana = metricasSemana["eventosSemana"] as List<Evento>
    println("\n🗓️ EVENTOS ESTA SEMANA (${eventosSemana.size}):")
    eventosSemana.take(3).forEach { 
        println("  - ${it.obtenerDetalle()}") 
    }
    if (eventosSemana.size > 3) {
        println("  ... y ${eventosSemana.size - 3} más")
    }
    
    
    println("\n🔗 TAREAS CON SUBTAREAS: ${metricasHoy["tareasConSubtareas"]}")
    
    println("\nPresione Enter para continuar...")
    leerCadena()
}
