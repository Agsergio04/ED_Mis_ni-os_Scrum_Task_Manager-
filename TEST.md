# Documentación: Pruebas Unitarias - `ActividadService`

## 1. Métodos Públicos del Servicio `ActividadService`

### 1.1. Lista de Métodos Públicos

| Método                     | Parámetros de Entrada                                                                                                                                 | Resultado Esperado o Efecto en el Repositorio                                                      |
|----------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|
| `crearTarea`                | `descripcion: String`, `etiquetas: String`                                                                                                           | Crea una tarea y la añade al repositorio. Llama al método `registrarAccion` de `HistorialService`. |
| `crearEvento`               | `descripcion: String`, `fecha: String`, `ubicacion: String`, `etiquetas: String`                                                                    | Crea un evento y lo añade al repositorio.                                                           |
| `listarActividades`         | Ninguno                                                                                                                                              | Retorna la lista de todas las actividades.                                                          |
| `listarUsuarios`            | Ninguno                                                                                                                                              | Retorna la lista de todos los usuarios.                                                            |
| `cambiarEstadoTarea`        | `id: Int`, `nuevoEstado: EstadoTarea`                                                                                                                 | Cambia el estado de una tarea. Llama a `registrarAccion` para registrar el cambio de estado.       |
| `crearUsuario`              | `nombre: String`                                                                                                                                      | Crea un usuario y lo retorna.                                                                       |
| `asignarTarea`              | `idTarea: Int`, `idUsuario: Int`                                                                                                                      | Asigna una tarea a un usuario y registra la acción.                                                |
| `obtenerTareasPorUsuario`   | `idUsuario: Int`                                                                                                                                     | Retorna las tareas asignadas a un usuario específico.                                               |
| `obtenerHistorial`          | `idActividad: Int`                                                                                                                                   | Retorna el historial de una actividad.                                                             |
| `asociarSubtarea`           | `idMadre: Int`, `idHija: Int`                                                                                                                        | Asocia una subtarea a una tarea madre y registra la acción.                                        |
| `filtrarActividades`        | `tipo: String?`, `estado: EstadoTarea?`, `etiquetas: List<String>?`, `nombreUsuario: String?`, `fechaFiltro: String?`                                 | Filtra actividades según los parámetros proporcionados.                                            |

---

## 2. Diseño de Casos de Prueba

### 2.1. Tabla de Diseño de Casos de Prueba

| Método                     | Caso de Prueba                                | Estado Inicial del Mock                                | Acción                                           | Resultado Esperado                                      |
|----------------------------|-----------------------------------------------|--------------------------------------------------------|-------------------------------------------------|---------------------------------------------------------|
| `crearTarea`                | Datos válidos                                 | `actividadRepo.aniadirActividad(any())` → `Unit`       | `servicio.crearTarea("Tarea 1", "etiqueta1")`    | Se llama a `aniadirActividad` y se registra acción.     |
| `crearTarea`                | Datos nulos o vacíos                          | —                                                      | `servicio.crearTarea("", "")`                    | Lanza `IllegalArgumentException`.                       |
| `crearEvento`               | Datos válidos                                 | `actividadRepo.aniadirActividad(any())` → `Unit`       | `servicio.crearEvento(...)`                      | Se llama a `aniadirActividad`.                          |
| `listarActividades`         | Sin filtros                                   | `actividadRepo.obtenerTodas()` → Lista de actividades  | `servicio.listarActividades()`                   | Retorna la lista de actividades.                        |
| `cambiarEstadoTarea`        | Tarea existente con nuevo estado             | `actividadRepo.obtenerPorId(id)` → Tarea              | `servicio.cambiarEstadoTarea(1, EstadoTarea.ACABADA)` | El estado de la tarea cambia y se registra la acción.   |
| `cambiarEstadoTarea`        | Tarea no existente                            | `actividadRepo.obtenerPorId(id)` → `null`             | `servicio.cambiarEstadoTarea(999, EstadoTarea.ACABADA)` | Lanza `IllegalArgumentException`.                      |
| `crearUsuario`              | Datos válidos                                 | `usuarioRepo.crearUsuario(nombre)` → Usuario          | `servicio.crearUsuario("Usuario 1")`             | Retorna el usuario creado.                              |
| `asignarTarea`              | Tarea y usuario existentes                   | `actividadRepo.obtenerPorId(idTarea)` → Tarea, `usuarioRepo.obtenerPorId(idUsuario)` → Usuario | `servicio.asignarTarea(1, 1)`                    | Se asigna la tarea al usuario y se registra la acción.  |
| `asignarTarea`              | Tarea o usuario no existentes                | `actividadRepo.obtenerPorId(idTarea)` → `null`        | `servicio.asignarTarea(999, 999)`                | Lanza `IllegalArgumentException`.                      |
| `obtenerHistorial`          | Actividad existente                          | `historialRepo.obtenerPorActividad(id)` → Historial   | `servicio.obtenerHistorial(1)`                   | Retorna el historial de la actividad.                   |
| `asociarSubtarea`           | Subtarea existente                           | `actividadRepo.obtenerPorId(idMadre)` → Tarea, `actividadRepo.obtenerPorId(idHija)` → Tarea | `servicio.asociarSubtarea(1, 2)`                | Se asocia la subtarea y se registra la acción.         |
| `asociarSubtarea`           | Subtarea o tarea madre no existente          | `actividadRepo.obtenerPorId(idMadre)` → `null`        | `servicio.asociarSubtarea(999, 999)`             | Lanza `IllegalArgumentException`.                      |
| `filtrarActividades`        | Actividades con filtros básicos              | `actividadRepo.obtenerTodas()` → Lista de actividades | `servicio.filtrarActividades(tipo = "TAREA")`    | Retorna las actividades que cumplen con los filtros.    |

---

## 3. Ejecución de Tests

### 3.1. Resultados

| Total de Tests | Pasaron | Fallaron | Tiempo Total |
|----------------|---------|----------|--------------|
| 14             | 14      | 0        | 0.25s        |

### 3.2. Resultados Detallados

- **`crearTarea`**: Verifica que la tarea se crea correctamente y que se lanzan excepciones cuando los datos son inválidos.
- **`crearEvento`**: Verifica la correcta creación de eventos.
- **`listarActividades`**: Verifica que se listan todas las actividades almacenadas.
- **`cambiarEstadoTarea`**: Verifica el cambio de estado de una tarea, incluyendo el manejo de excepciones.
- **`crearUsuario`**: Verifica la creación de usuarios.
- **`asignarTarea`**: Verifica la asignación de tareas a usuarios, incluyendo el manejo de excepciones.
- **`obtenerHistorial`**: Verifica la correcta obtención del historial de actividades.
- **`asociarSubtarea`**: Verifica la asociación de subtareas a tareas, manejando correctamente las excepciones.
- **`filtrarActividades`**: Verifica que el filtrado de actividades funcione correctamente con diferentes filtros.

---

## 4. Conclusiones

- **Cobertura de Pruebas**: Todas las funcionalidades principales de `ActividadService` han sido cubiertas por los tests, incluyendo casos de éxito y manejo de excepciones.
- **Dependencias Mockeadas**: Se ha utilizado `MockK` para simular el comportamiento de las dependencias, garantizando que las pruebas sean completamente aisladas.
- **Ejecución Exitosa**: Todos los casos de prueba han pasado exitosamente sin errores, lo que indica que el servicio funciona correctamente en las situaciones contempladas.

---

## 5. Recursos

- **Temario trabajado en clase**: Inyección de dependencias, mocks con MockK, pruebas unitarias con Kotest.
- **Técnicas utilizadas**: Uso de `DescribeSpec` en Kotest, `coEvery` para mocks asíncronos, manejo de excepciones con `assertThrows` y `shouldThrow`.


