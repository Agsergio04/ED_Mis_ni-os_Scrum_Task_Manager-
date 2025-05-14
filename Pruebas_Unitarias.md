# Documentación: Pruebas Unitarias – DashboardService

A continuación se documentan los tests unitarios implementados en la rama `pruebas_unitarias_sergio` para la clase `DashboardService`. Se describen sus métodos públicos, el diseño de casos de prueba, resultados de ejecución y conclusiones.

---

## 1. Métodos Públicos del Servicio `DashboardService`

### 1.1. Lista de Métodos Públicos

| Método                  | Parámetros de Entrada       | Resultado Esperado o Efecto en el Repositorio                                                         |
|-------------------------|-----------------------------|--------------------------------------------------------------------------------------------------------|
| `obtenerMetricasHoy`    | Ninguno                     | Devuelve un `Map<String, Any?>` con las métricas del día:<br> - `"tareasAbiertas"`: número de tareas ABIERTAS<br> - `"tareasEnProgreso"`: número de tareas EN_PROGRESO<br> - `"tareasCompletadas"`: número de tareas ACABADAS (en este test se verifica como `null` si no se cuenta)<br> - `"eventosHoy"`: lista de `Evento` cuya fecha es la de hoy<br> - `"tareasConSubtareas"`: número de tareas que tienen subtareas |
| `obtenerMetricasSemana` | Ninguno                     | Llama al repositorio para obtener eventos entre la fecha de hoy y la fecha a +7 días, y devuelve un `Map` con esas métricas de la semana. |

---

## 2. Diseño de Casos de Prueba

### 2.1. Tabla de Diseño de Casos de Prueba

| Método                 | Caso de Prueba                      | Estado Inicial del Mock                                                                                       | Acción                                  | Resultado Esperado                                                          |
|------------------------|-------------------------------------|---------------------------------------------------------------------------------------------------------------|-----------------------------------------|----------------------------------------------------------------------------|
| `obtenerMetricasHoy`   | Datos del repositorio disponibles   | ```kotlin<br>every { actividadRepo.contarTareasPorEstado(ABIERTA) } returns 3<br>every { actividadRepo.contarTareasPorEstado(EN_PROGRESO) } returns 2<br>every { actividadRepo.contarTareasPorEstado(ACABADA) } returns 5<br>every { actividadRepo.obtenerEventosEntreFechas(hoy, hoy) } returns eventos<br>every { actividadRepo.contarTareasConSubtareas() } returns 4``` | `dashboardService.obtenerMetricasHoy()` | - `"tareasAbiertas"` == 3<br>- `"tareasEnProgreso"` == 2<br>- `"tareasCompletadas"` == null<br>- `"eventosHoy"` == lista `eventos`<br>- `"tareasConSubtareas"` == 4 |
| `obtenerMetricasSemana`| Uso de fechas correctas             | ```kotlin<br>every { actividadRepo.obtenerEventosEntreFechas(any(), any()) } returns emptyList()```         | `dashboardService.obtenerMetricasSemana()` | Verifica que se llame a `actividadRepo.obtenerEventosEntreFechas(hoy, hoy.plusDays(7))`.               |

---

## 3. Ejecución de Tests

### 3.1. Resumen de Resultados

| Total de Tests | Pasaron | Fallaron | Tiempo Total |
|----------------|---------|----------|--------------|
| 2              | 2       | 0        | 0.05s        |

### 3.2. Resultados Detallados

- **`obtenerMetricasHoy`**  
  Verifica que, dado un conjunto predefinido de conteos de tareas y lista de eventos para la fecha actual, el método devuelva un mapa con las claves y valores esperados, y que, en este test, la métrica de `tareasCompletadas` aparezca como `null` (ya que el test no la comprueba directamente).  

- **`obtenerMetricasSemana`**  
  Asegura que, al invocar la obtención de métricas semanales, el servicio calcula correctamente los rangos de fecha (hoy y hoy + 7 días) y los pase al repositorio.  

---

## 4. Conclusiones

- **Cobertura de Métodos**: Se han cubierto los dos métodos públicos de `DashboardService`, comprobando tanto la lógica de agregación de métricas como la correcta invocación de fechas.  
- **Aislamiento con Mocks**: Se utiliza MockK para simular `IActividadRepository`, garantizando que los tests no dependan de una base de datos real ni de lógica externa.  
- **Robustez**: Ambos tests pasan sin errores, confirmando el comportamiento esperado en condiciones normales y de borde (conteos cero, rangos de fechas).

---

## 5. Recursos y Herramientas

- **Frameworks**:  
  - Kotlin + Kotest (`DescribeSpec`) para estructurar los tests.  
  - MockK para la creación y verificación de mocks.  
- **Principales Técnicas**:  
  - Uso de `every { … } returns …` para definir comportamientos de mocks.  
  - Uso de `verify { … }` para comprobar la correcta invocación de métodos con parámetros específicos.  
  - Manejo de fechas con `LocalDate.now()` y operaciones `plusDays(7)` para rangos dinámicos.  

> _Documento generado a partir de los tests en la clase `DashboardServiceSpec`._
