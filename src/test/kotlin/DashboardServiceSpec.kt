package org.example.aplicacion

import DashboardService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.datos.IActividadRepository
import org.example.dominio.EstadoTarea
import org.example.dominio.Evento
import java.time.LocalDate

class DashboardServiceSpec : DescribeSpec({
    val actividadRepo = mockk<IActividadRepository>()
    val dashboardService = DashboardService(actividadRepo)

    beforeTest { clearAllMocks() }

    describe("obtenerMetricasHoy") {
        context("con datos del repositorio") {
            it("debe retornar métricas correctas") {
                val hoy = LocalDate.now().toString()
                // Configurar mocks para contar tareas
                every { actividadRepo.contarTareasPorEstado(EstadoTarea.ABIERTA) } returns 3
                every { actividadRepo.contarTareasPorEstado(EstadoTarea.EN_PROGRESO) } returns 2
                every { actividadRepo.contarTareasPorEstado(EstadoTarea.ACABADA) } returns 5
                // Eventos como objetos Evento
                val eventos = listOf(
                    Evento.creaInstancia("E1", hoy, "Lugar1", "tag"),
                    Evento.creaInstancia("E2", hoy, "Lugar2", "tag"),
                )
                every { actividadRepo.obtenerEventosEntreFechas(hoy, hoy) } returns eventos
                every { actividadRepo.contarTareasConSubtareas() } returns 4

                // Ejecutar
                val metricas = dashboardService.obtenerMetricasHoy()

                // Verificar
                metricas["tareasAbiertas"] shouldBe 3
                metricas["tareasEnProgreso"] shouldBe 2
                metricas["tareasCompletadas"] shouldBe null
                metricas["eventosHoy"] shouldBe eventos
                metricas["tareasConSubtareas"] shouldBe 4
            }
        }
    }

    describe("obtenerMetricasSemana") {
        context("al solicitar eventos de la semana") {
            it("debe usar fechas correctas") {
                val hoy = LocalDate.now()
                val finSemana = hoy.plusDays(7).toString()
                every { actividadRepo.obtenerEventosEntreFechas(any(), any()) } returns emptyList()

                dashboardService.obtenerMetricasSemana()

                verify {
                    actividadRepo.obtenerEventosEntreFechas(hoy.toString(), finSemana)
                }
            }
        }
    }
})
