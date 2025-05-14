import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldContain
import io.mockk.*
import org.example.aplicacion.ActividadService
import org.example.aplicacion.HistorialService
import org.example.datos.IActividadRepository
import org.example.datos.IUsuarioRepository
import org.example.dominio.*
import org.junit.jupiter.api.assertThrows

class ActividadServiceTest : DescribeSpec({

    val actividadRepo = mockk<IActividadRepository>()
    val usuarioRepo = mockk<IUsuarioRepository>()
    val historialService = mockk<HistorialService>()
    val dashboardService = mockk<DashboardService>()

    val servicio = ActividadService(actividadRepo, usuarioRepo, historialService, dashboardService)

    describe("Test cambiarEstadoTarea") {
        it("debería cambiar el estado de una tarea correctamente") {
            val tarea = Tarea.creaInstancia("Tarea 1", "etiqueta1")
            every { actividadRepo.obtenerPorId(1) } returns tarea
            every { historialService.registrarAccion(any(), any()) } just Runs

            servicio.cambiarEstadoTarea(1, EstadoTarea.ACABADA)

            tarea.estadoTarea shouldBe EstadoTarea.ACABADA
            verify { historialService.registrarAccion(1, "Estado cambiado: PENDIENTE → COMPLETADA") }
        }

        it("debería lanzar IllegalArgumentException cuando la tarea no existe") {
            every { actividadRepo.obtenerPorId(999) } returns null

            val exception = assertThrows<IllegalArgumentException> {
                servicio.cambiarEstadoTarea(999, EstadoTarea.ACABADA)
            }
            exception.message shouldBe "No existe una tarea con el ID proporcionado"
        }
    }

    describe("Test asignarTarea") {
        it("debería asignar una tarea a un usuario correctamente") {
            val tarea = Tarea.creaInstancia("Tarea 1", "etiqueta1")
            val usuario = mockk<Usuario>()
            every { usuario.obtenerId() } returns 1

            every { actividadRepo.obtenerPorId(1) } returns tarea
            every { usuarioRepo.obtenerPorId(1) } returns usuario
            every { historialService.registrarAccion(any(), any()) } just Runs

            servicio.asignarTarea(1, 1)

            tarea.usuarioAsignado shouldBe usuario
            verify { historialService.registrarAccion(1, "Asignada al usuario #${usuario.obtenerId()}") }
        }

        it("debería lanzar IllegalArgumentException cuando la tarea o el usuario no existen") {
            every { actividadRepo.obtenerPorId(999) } returns null
            every { usuarioRepo.obtenerPorId(999) } returns null

            val exception = assertThrows<IllegalArgumentException> {
                servicio.asignarTarea(999, 999)
            }
            exception.message shouldBe "Tarea no encontrada"
        }
    }

    describe("Test obtenerTareasPorUsuario") {
        it("debería retornar las tareas asignadas a un usuario") {
            val usuario = mockk<Usuario>()
            every { usuario.obtenerId() } returns 1

            val tarea1 = Tarea.creaInstancia("Tarea 1", "etiqueta1").apply { usuarioAsignado = usuario }
            val tarea2 = Tarea.creaInstancia("Tarea 2", "etiqueta2").apply { usuarioAsignado = usuario }

            every { actividadRepo.obtenerTodas() } returns listOf(tarea1, tarea2)

            val result = servicio.obtenerTareasPorUsuario(1)

            result shouldBe listOf(tarea1, tarea2)
        }
    }

    describe("Test obtenerHistorial") {
        it("debería obtener el historial de una actividad correctamente") {
            val historial = listOf(mockk<Historial>())
            every { historialService.obtenerHistorial(1) } returns historial

            val result = servicio.obtenerHistorial(1)

            result shouldBe historial
        }
    }

    describe("Test asociarSubtarea") {
        it("debería asociar una subtarea a una tarea madre") {
            val tareaMadre = Tarea.creaInstancia("Tarea Madre", "etiqueta1")
            val tareaHija = Tarea.creaInstancia("Tarea Hija", "etiqueta2")
            every { tareaHija.obtenerId() } returns 2

            every { actividadRepo.obtenerPorId(1) } returns tareaMadre
            every { actividadRepo.obtenerPorId(2) } returns tareaHija
            every { historialService.registrarAccion(any(), any()) } just Runs

            servicio.asociarSubtarea(1, 2)

            tareaMadre.subtareas shouldContain tareaHija
            verify { historialService.registrarAccion(1, "Subtarea #2 asociada") }
        }

        it("debería lanzar IllegalArgumentException cuando la tarea madre o hija no existen") {
            every { actividadRepo.obtenerPorId(999) } returns null

            val exception = assertThrows<IllegalArgumentException> {
                servicio.asociarSubtarea(999, 999)
            }
            exception.message shouldBe "Tarea madre no encontrada"
        }
    }

    describe("Test filtrarActividades") {
        it("debería filtrar actividades correctamente") {
            val tarea = Tarea.creaInstancia("Tarea 1", "urgente importante")
            val evento = Evento.creaInstancia("Evento 1", "2025-05-14", "Ubicación", "importante")
            val usuario = mockk<Usuario>()
            every { usuario.nombre } returns "Juan"

            tarea.usuarioAsignado = usuario

            every { actividadRepo.obtenerTodas() } returns listOf(tarea, evento)
            every { usuarioRepo.obtenerTodos() } returns listOf(usuario)

            val result = servicio.filtrarActividades(tipo = "TAREA", etiquetas = listOf("urgente"))

            result shouldBe listOf(tarea)
        }
    }
})
