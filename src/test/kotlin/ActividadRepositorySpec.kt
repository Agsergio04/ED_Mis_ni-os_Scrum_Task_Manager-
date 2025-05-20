package org.example.aplicacion

import org.example.datos.IActividadRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.dominio.Actividad
import org.example.dominio.EstadoTarea
import org.example.dominio.Evento
import org.example.dominio.Tarea

class ActividadRepositorySpec : DescribeSpec({
    val repo = mockk<IActividadRepository>(relaxed = true)
    val tarea = Tarea.creaInstancia("Tarea 1", "tag")
    val evento = Evento.creaInstancia("Evento", "2025-01-01", "Lugar", "tag")

    beforeTest { clearAllMocks() }

    describe("añadirActividad") {
        it("debe agregar una actividad a la lista") {
            repo.aniadirActividad(tarea)
            verify { repo.aniadirActividad(tarea) }
        }
    }

    describe("obtenerPorId") {
        it("debe retornar la actividad correcta si existe") {
            every { repo.obtenerPorId(any()) } returns tarea
            repo.obtenerPorId(1) shouldBe tarea
        }

        it("debe retornar null si no existe") {
            every { repo.obtenerPorId(any()) } returns null
            repo.obtenerPorId(999) shouldBe null
        }
    }

    describe("contarTareasPorEstado") {
        it("debe contar tareas en estado ABIERTA") {
            every { repo.contarTareasPorEstado(EstadoTarea.ABIERTA) } returns 3
            repo.contarTareasPorEstado(EstadoTarea.ABIERTA) shouldBe 3
        }
    }

    describe("obtenerEventosEntreFechas") {
        it("debe filtrar eventos entre fechas") {
            every { repo.obtenerEventosEntreFechas("2025-01-01", "2025-12-31") } returns listOf(evento)
            val eventos = repo.obtenerEventosEntreFechas("2025-01-01", "2025-12-31")
            eventos shouldContainExactly listOf(evento)
        }
    }

    describe("obtenerTodas") {
        it("debe retornar todas las actividades") {
            every { repo.obtenerTodas() } returns listOf(tarea, evento)
            repo.obtenerTodas() shouldContainExactly listOf(tarea, evento)
        }
    }
})