import org.example.aplicacion.ActividadService
import org.example.aplicacion.HistorialService
import org.example.datos.IActividadRepository
import org.example.datos.IUsuarioRepository
import org.example.dominio.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import io.kotest.matchers.*



class ActividadServiceTest : DescribeSpec({

    val actividadRepo = mockk<IActividadRepository>(relaxed = true)
    val usuarioRepo = mockk<IUsuarioRepository>(relaxed = true)
    val historialService = mockk<HistorialService>(relaxed = true)
    val dashboardService = mockk<DashboardService>(relaxed = true)

    val servicio = ActividadService(actividadRepo, usuarioRepo, historialService, dashboardService)

    describe("ActividadService") {

        describe("crearTarea") {
            it("debe llamar al método aniadirActividad y crear una tarea") {
                val descripcion = "Tarea 1"
                val etiquetas = "etiqueta1"
                coEvery { actividadRepo.aniadirActividad(any()) } returns Unit

                servicio.crearTarea(descripcion, etiquetas)

                coVerify { actividadRepo.aniadirActividad(any()) }
            }

            it("debe lanzar IllegalArgumentException cuando los datos son nulos") {
                val exception = assertThrows<IllegalArgumentException> {
                    servicio.crearTarea("", "")
                }
                exception.message shouldBe "Descripción y etiquetas no pueden ser nulos o vacíos"
            }
        }

        describe("crearEvento") {
            it("debe crear un evento correctamente") {
                val descripcion = "Evento 1"
                val fecha = "2025-05-14"
                val ubicacion = "Ubicación"
                val etiquetas = "etiqueta1"
                coEvery { actividadRepo.aniadirActividad(any()) } returns Unit

                servicio.crearEvento(descripcion, fecha, ubicacion, etiquetas)

                coVerify { actividadRepo.aniadirActividad(any()) }
            }
        }

        describe("listarActividades") {
            it("debe listar todas las actividades correctamente") {
                val actividades = listOf(mockk<Actividad>(), mockk<Actividad>())
                coEvery { actividadRepo.obtenerTodas() } returns actividades

                val result = servicio.listarActividades()

                result shouldBe actividades
            }
        }

        describe("cambiarEstadoTarea") {
            it("debe cambiar el estado de una tarea correctamente") {
                val tarea = Tarea.creaInstancia("Tarea 1", "etiqueta1")
                coEvery { actividadRepo.obtenerPorId(1) } returns tarea
                coEvery { historialService.registrarAccion(any(), any()) } just Runs

                servicio.cambiarEstadoTarea(1, EstadoTarea.ACABADA)

                tarea.estadoTarea shouldBe EstadoTarea.ACABADA
                coVerify { historialService.registrarAccion(1, "Estado cambiado: PENDIENTE → COMPLETADA") }
            }

            it("debe lanzar IllegalArgumentException cuando la tarea no existe") {
                coEvery { actividadRepo.obtenerPorId(999) } returns null

                val exception = assertThrows<IllegalArgumentException> {
                    servicio.cambiarEstadoTarea(999, EstadoTarea.ACABADA)
                }
                exception.message shouldBe "No existe una tarea con el ID proporcionado"
            }
        }

        describe("crearUsuario") {
            it("debe crear un usuario correctamente") {
                val nombre = "Usuario 1"
                val usuario = mockk<Usuario>()
                coEvery { usuarioRepo.crearUsuario(nombre) } returns usuario

                val result = servicio.crearUsuario(nombre)

                result shouldBe usuario
                coVerify { usuarioRepo.crearUsuario(nombre) }
            }
        }

        describe("asignarTarea") {
            it("debe asignar una tarea a un usuario correctamente") {
                val tarea = Tarea.creaInstancia("Tarea 1", "etiqueta1")
                val usuario = mockk<Usuario>()
                coEvery { actividadRepo.obtenerPorId(1) } returns tarea
                coEvery { usuarioRepo.obtenerPorId(1) } returns usuario
                coEvery { historialService.registrarAccion(any(), any()) } just Runs

                servicio.asignarTarea(1, 1)

                tarea.usuarioAsignado shouldBe usuario
                coVerify { historialService.registrarAccion(1, "Asignada al usuario #${usuario.obtenerId()}") }
            }

            it("debe lanzar IllegalArgumentException cuando la tarea o el usuario no existen") {
                coEvery { actividadRepo.obtenerPorId(999) } returns null
                coEvery { usuarioRepo.obtenerPorId(999) } returns null

                val exception = assertThrows<IllegalArgumentException> {
                    servicio.asignarTarea(999, 999)
                }
                exception.message shouldBe "Tarea no encontrada"
            }
        }

        describe("obtenerHistorial") {
            it("debe obtener el historial de una actividad correctamente") {
                val historial = listOf(mockk<Historial>())
                coEvery { historialService.obtenerHistorial(1) } returns historial

                val result = servicio.obtenerHistorial(1)

                result shouldBe historial
            }
        }

        describe("asociarSubtarea") {
            it("debe asociar una subtarea a una tarea madre") {
                val tareaMadre = Tarea.creaInstancia("Tarea Madre", "etiqueta1")
                val tareaHija = Tarea.creaInstancia("Tarea Hija", "etiqueta2")
                coEvery { actividadRepo.obtenerPorId(1) } returns tareaMadre
                coEvery { actividadRepo.obtenerPorId(2) } returns tareaHija
                coEvery { historialService.registrarAccion(any(), any()) } just Runs

                servicio.asociarSubtarea(1, 2)

                tareaMadre.subtareas shouldContain tareaHija
                coVerify { historialService.registrarAccion(1, "Subtarea #${tareaHija.obtenerId()} asociada") }
            }

            it("debe lanzar IllegalArgumentException cuando la tarea madre o hija no existen") {
                coEvery { actividadRepo.obtenerPorId(999) } returns null

                val exception = assertThrows<IllegalArgumentException> {
                    servicio.asociarSubtarea(999, 999)
                }
                exception.message shouldBe "Tarea madre no encontrada"
            }
        }

        describe("filtrarActividades") {
            it("debe filtrar actividades correctamente") {
                val actividades = listOf(mockk<Actividad>(), mockk<Actividad>())
                coEvery { actividadRepo.obtenerTodas() } returns actividades

                val result = servicio.filtrarActividades(tipo = "TAREA")

                result.size shouldBe 2
            }
        }
    }
})


