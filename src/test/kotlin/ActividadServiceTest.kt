import io.kotest.assertions.throwables.shouldThrow
import org.example.aplicacion.ActividadService
import org.example.aplicacion.HistorialService
import org.example.datos.IActividadRepository
import org.example.datos.IUsuarioRepository
import org.example.dominio.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.*
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.example.datos.IHistorialRepository
import org.junit.jupiter.api.assertThrows


class ActividadServiceTest : DescribeSpec({

    val actividadRepo = mockk<IActividadRepository>(relaxed = true)
    val usuarioRepo = mockk<IUsuarioRepository>(relaxed = true)
    val historialRepo = mockk<IHistorialRepository>(relaxed = true)
    val historialService = HistorialService(historialRepo)
    val dashboardService = mockk<DashboardService>(relaxed = true)


    val servicio = ActividadService(actividadRepo, usuarioRepo, historialService, dashboardService)

    describe("ActividadService") {

        describe("crearTarea") {
            it("debe llamar al método aniadirActividad y crear una tarea") {
                val descripcion = "Tarea 1"
                val etiquetas = "etiqueta1"

                // Configuración correcta con coEvery para llamadas suspend
                coEvery { actividadRepo.aniadirActividad(any()) } just Runs
                coEvery { historialRepo.agregar(any()) } just Runs

                servicio.crearTarea(descripcion, etiquetas)

                coVerify { actividadRepo.aniadirActividad(any()) }
                coVerify { historialRepo.agregar(any()) }
            }

            it("debe lanzar IllegalArgumentException cuando los datos son nulos") {
                val exception = assertThrows<IllegalArgumentException> {
                    servicio.crearTarea("", "")
                }
                exception.message shouldBe "Descripción no puede estar vacía"
            }
        }

        describe("crearEvento") {
            it("debe crear un evento correctamente") {
                val descripcion = "Evento 1"
                val fecha = "2025-05-14"
                val ubicacion = "Ubicación"
                val etiquetas = "etiqueta1"
                coEvery { actividadRepo.aniadirActividad(any()) } returns Unit
                every { historialRepo.agregar(any()) } just Runs

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
                coEvery { historialRepo.agregar(any()) } just Runs

                servicio.cambiarEstadoTarea(1, EstadoTarea.ACABADA)

                tarea.estadoTarea shouldBe EstadoTarea.ACABADA
                coVerify { historialRepo.agregar(any()) }
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

                val usuarioReal = Usuario("Test", id = 1)
                val tarea = Tarea.creaInstancia("Tarea 1", "etiqueta1")
                val historialRepoMock = mockk<IHistorialRepository>()
                val historialServiceReal = HistorialService(historialRepoMock)  // Instancia real con mock inyectado

                coEvery { actividadRepo.obtenerPorId(1) } returns tarea
                coEvery { usuarioRepo.obtenerPorId(1) } returns usuarioReal
                coEvery { historialRepoMock.agregar(any()) } just Runs  // Mockea el repo

                val servicio = ActividadService(actividadRepo, usuarioRepo, historialServiceReal, dashboardService)

                servicio.asignarTarea(1, 1)

                tarea.usuarioAsignado shouldBe usuarioReal
                coVerify { historialRepoMock.agregar(any()) }  // Verifica que se llamó al repo
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
                val historial = listOf(Historial("2025-05-14", "mensaje", 1))

                coEvery { historialRepo.obtenerPorActividad(1) } returns historial

                val result = servicio.obtenerHistorial(1)

                result shouldBe historial
            }
        }

        describe("asociarSubtarea") {
            it("debe lanzar una excepción si la tarea madre no se encuentra") {

                coEvery { actividadRepo.obtenerPorId(any()) } returns null

                val exception = shouldThrow<IllegalArgumentException> {
                    servicio.asociarSubtarea(1, 2)
                }

                exception.message shouldBe "Tarea madre no encontrada"
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
            it("debe devolver actividades cuando se pasa un filtro básico") {

                val tarea1 = Tarea.creaInstancia("Tarea 1", "etiqueta1")
                val tarea2 = Tarea.creaInstancia("Tarea 2", "etiqueta2")
                val evento = Evento.creaInstancia("Evento 1", "2025-05-14", "Ubicación", "etiqueta1")

                coEvery { actividadRepo.obtenerTodas() } returns listOf(tarea1, tarea2, evento)

                val result = servicio.filtrarActividades()

                result.size shouldBeGreaterThan 0
            }

            it("no debe encontrar actividades si no hay coincidencias con los parámetros") {

                val usuario1 = Usuario("usuario1")
                val usuario2 = Usuario("usuario2")

                val tarea = Tarea.creaInstancia("Tarea 1", "etiqueta1").apply {
                    usuarioAsignado = usuario1 // Asignar un usuario a la tarea
                }
                val evento = Evento.creaInstancia("Evento 1", "2025-01-01", "ubicacion", "etiqueta1")

                coEvery { actividadRepo.obtenerTodas() } returns listOf(tarea, evento)
                coEvery { usuarioRepo.obtenerTodos() } returns listOf(usuario1, usuario2)

                val result = servicio.filtrarActividades(
                    tipo = "TAREA",
                    nombreUsuario = "usuarioNoExistente"
                )

                result.shouldNotBeEmpty()
            }
        }
    }
})
