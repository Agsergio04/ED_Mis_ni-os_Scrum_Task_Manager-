package org.example.aplicacion

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.example.datos.IHistorialRepository
import org.example.dominio.Historial
import org.example.utilidades.Utils

class HistorialServiceTest : DescribeSpec({
    // Creo un mock del repositorio
    val repoMock = mockk<IHistorialRepository>(relaxed = true)
    val service = HistorialService(repoMock)

    // Fijo una fecha determinista para no depender del sistema real
    val fechaFija = "2025-05-16"

    beforeContainer {
        // Simulo el método estático Utils.obtenerFechaActual para devolver fechaFija
        mockkStatic(Utils::class)
        every { Utils.obtenerFechaActual() } returns fechaFija
    }

    afterTest {
        clearAllMocks()
    }

    describe("Registrar acción en el historial") {
        it("Guardar historial con datos correctos") {
            service.registrarAccion(100, "Inicio de proceso")

            verify {
                repoMock.agregar(match {
                    it.idActividad == 100 &&
                    it.descripcion == "Inicio de proceso" &&
                    it.fecha == fechaFija
                })
            }
        }

        it("Propago la excepción si el repositorio falla al agregar") {
            every { repoMock.agregar(any()) } throws RuntimeException("Fallo en la base de datos")

            shouldThrow<RuntimeException> {
                service.registrarAccion(5, "Error simulado")
            }
        }
    }

    describe("Obtener historial de actividad") {
        it("Retorno la lista que el repositorio proporciona") {
            val historiales = listOf(
                Historial(fechaFija, "Acción A", 1),
                Historial(fechaFija, "Acción B", 1)
            )
            every { repoMock.obtenerPorActividad(1) } returns historiales

            val resultado = service.obtenerHistorial(1)

            resultado.shouldContainExactly(historiales)
        }

        it("Retorno lista vacía si no existen registros para la actividad") {
            every { repoMock.obtenerPorActividad(99) } returns emptyList()

            val resultado = service.obtenerHistorial(99)
            resultado.shouldBe(emptyList())
        }

        it("Propago la excepción si el repositorio lanza un error") {
            every { repoMock.obtenerPorActividad(5) } throws RuntimeException("Error lectura")

            shouldThrow<RuntimeException> {
                service.obtenerHistorial(5)
            }
        }
    }
})
