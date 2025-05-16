# Documentación: Pruebas Unitarias – HistorialService

A continuación se documentan los tests unitarios implementados en la clase `HistorialServiceTest`. Se describen sus métodos públicos, el diseño de casos de prueba, resultados de ejecución y conclusiones.

---

## 1. Métodos Públicos del Servicio `HistorialService`

### 1.1. Lista de Métodos Públicos

| Método                | Parámetros de Entrada      | Resultado Esperado o Efecto en el Repositorio                                          |
|-----------------------|---------------------------|----------------------------------------------------------------------------------------|
| `registrarAccion`      | `idActividad: Int`, `descripcion: String` | Crea un objeto `Historial` con la fecha actual (obtenida de `Utils.obtenerFechaActual()`) y guarda en el repositorio a través de `agregar`. Propaga excepciones del repositorio. |
| `obtenerHistorial`     | `idActividad: Int`        | Obtiene una lista de objetos `Historial` filtrados por `idActividad` desde el repositorio. Propaga excepciones del repositorio. |

---

## 2. Diseño de Casos de Prueba

### 2.1. Tabla de Diseño de Casos de Prueba

| Método             | Caso de Prueba                         | Estado Inicial del Mock                                              | Acción                       | Resultado Esperado                                                  |
|--------------------|---------------------------------------|---------------------------------------------------------------------|------------------------------|-------------------------------------------------------------------|
| `registrarAccion`   | Guardar historial con datos correctos | Mock del repositorio relajado y mock estático para fecha fija       | `registrarAccion(100, "Inicio de proceso")` | Verifica que `repoMock.agregar` reciba un `Historial` con los valores correctos de `idActividad`, `descripcion` y `fecha`. |
| `registrarAccion`   | Propaga la excepción al agregar          | Configura `repoMock.agregar` para lanzar `RuntimeException`         | `registrarAccion(5, "Error simulado")`         | La excepción lanzada por el repositorio es propagada correctamente. |
| `obtenerHistorial`  | Retorna una lista de historial válida      | Configura `repoMock.obtenerPorActividad` para devolver lista con historiales | `obtenerHistorial(1)`                   | Retorna la lista esperada de historiales.                          |
| `obtenerHistorial`  | Retorna una lista vacía si no hay registros | Configura `repoMock.obtenerPorActividad` para devolver lista vacía  | `obtenerHistorial(99)`                  | Retorna una lista vacía.                                               |
| `obtenerHistorial`  | Propagar una excepción al obtener historial | Configura `repoMock.obtenerPorActividad` para lanzar `RuntimeException` | `obtenerHistorial(5)`                   | La excepción lanzada por el repositorio es propagada correctamente. |

---

## 3. Ejecución de Tests

### 3.1. Resumen de Resultados

| Total de Tests | Pasaron | Fallaron | Tiempo Total |
|----------------|---------|----------|--------------|
| 5              | 5       | 0        | 0.04s        |

### 3.2. Resultados Detallados

- **`registrarAccion` con datos correctos**  
  Verifica que el servicio crea correctamente el objeto `Historial` con la fecha fija simulada y llama al repositorio para agregarlo.

- **`registrarAccion` propagación de excepción**  
  Confirma que una excepción lanzada por el repositorio se propaga sin ser atrapada dentro del servicio.

- **`obtenerHistorial` retorna lista válida**  
  Comprueba que el método retorne exactamente la lista que el repositorio mock devuelve.

- **`obtenerHistorial` retorna lista vacía**  
  Asegura que se retorne una lista vacía cuando no hay registros para la actividad consultada.

- **`obtenerHistorial` propagación de excepción**  
  Verifica que las excepciones del repositorio al obtener datos también se propaguen correctamente.

---

## 4. Conclusiones

- **Cobertura Completa**: Se probaron los métodos públicos clave de `HistorialService`.
- **Uso de Mocks y Simulación de Fecha**: La combinación de mocks para el repositorio y simulación estática de fecha permite tests aislados.
- **Tests Limpios y Mantenibles**: Cada caso de prueba está claramente enfocado, usando Kotest y MockK para facilitar la legibilidad y robustez.
- **Todo pasó bien**: Todos los tests definidos se ejecutaron correctamente sin fallos.

---

## 5. Recursos y Herramientas

- **Frameworks**:  
  - Kotlin + Kotest (`DescribeSpec`) para tests.  
  - MockK para creación y verificación de mocks.  
- **Técnicas Principales**:  
  - Uso de `every { ... } returns ...` para comportamiento de mocks.  
  - Uso de `verify { ... }` para validar mocks con argumentos específicos.  
  - Simulación de métodos para controlar dependencias.

> _Documento generado a partir de los tests en la clase `HistorialServiceTest`._
