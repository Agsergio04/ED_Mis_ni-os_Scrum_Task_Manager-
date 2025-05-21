# Aplicación de Code Smells y Patrones de Refactorización en el Task Manager

## Análisis inicial del código

Como primer paso del proceso de mejora del código, realicé un análisis completo del repositorio utilizando la herramienta de inspección de código que proporciona **IntelliJ IDEA**. Para ello, accedí a la opción `Code > Inspect Code...` y seleccioné el análisis del proyecto completo. Esta herramienta me permitió detectar _code smells_, construcciones innecesarias, errores de estilo, problemas gramaticales en archivos Markdown, así como posibles vulnerabilidades en las dependencias del proyecto.

![Captura1](capturas/Captura%20de%20pantalla%202025-05-21%20192438.png)
![Captura2](capturas/Captura%20de%20pantalla%202025-05-21%20192041.png)

---

<br>

## Resultados del análisis

A continuación, detallo las principalesadvertencias obtenidos tras la inspección automática:

### Advertencias en Java (6)

- **Redundancia en declaraciones**: Se detectaron métodos que devuelven valores constantes o usan parámetros que no se modifican, lo cual indica que se puede simplificar el código.
- **Elementos no utilizados**: Aparecen clases, métodos o variables sin uso, candidatos claros para eliminación.

### Advertencias en Kotlin (7 advertencias y 12 advertencias leves)

- **Inconsistencia en la estructura de paquetes**: El archivo `DashboardService.kt` presenta una discrepancia entre el `package` declarado y su ubicación física, lo que puede dificultar la mantenibilidad.
- **Código redundante y símbolos innecesarios**:
  - Clases como `ActividadService.kt`, `ConsolaUI.kt` y `Tarea.kt` contienen `import` que no se utilizan.
  - Existen símbolos no utilizados en `ActividadService`, `ActividadServiceTest` y `Tarea`.
- **Problemas de visibilidad**:
  - Se identificaron miembros públicos que podrían ser `private` en varias clases (`ActividadService`, `ConsolaUI`, `Evento`, `Tarea`), lo cual mejoraría la encapsulación.
- **Uso de métodos obsoletos**:
  - En `ConsolaUI` se utiliza `readLine()`, una función heredada de Java propensa a fallos. Opté por recomendar `readln()` o `readlnOrNull()`, más seguras y propias de Kotlin.
- **Condicionales complejos**:
  - En `ActividadRepository.kt` se emplean estructuras del tipo `x >= a && x <= b`, que se pueden simplificar con `x in a..b`.

### Markdown (3 advertencias leves)

- **Formato incorrecto en tablas**: Se detectaron errores menores en archivos como `README.md` y `TEST.md`.

### Revisión de texto

- **Errores gramaticales**: Se señalaron 10 errores que afectan a la claridad.
- **Errores tipográficos**: Se contabilizaron 60 errores que es necesario corregir para mejorar la calidad de la documentación.

### Seguridad (2 advertencias)

- **Dependencias vulnerables**:
  - `logback-classic:1.2.6` (CVE-2023-6378)
  - `logback-core:1.2.6` (CVE-2021-42550), incluida de forma transitiva.

---

<br>

## Code Smells detectados

| Nº | Code Smell                                    | Ubicación                           | Refactorización sugerida                                      |
|----|-----------------------------------------------|-------------------------------------|---------------------------------------------------------------|
| 1  | Método largo con múltiples responsabilidades  | `cambiarEstadoTarea()`              | Extracción de Método                                          |
| 2  | Código duplicado de lectura de consola        | Uso repetido de `Scanner(System.in)`| Método reutilizable / Introducir método                      |
| 3  | Flujo de entrada complejo                     | `mostrarMenuFiltrado()`             | Introducir Objeto Parámetro                                   |
| 4  | Condicional complejo con múltiples ramas      | `crearActividad()` y `cambiarEstadoTarea()` | Simplificación de condicional                     |
| 5  | Baja cohesión y mezcla de lógica              | `mostrarDashboard()` y otros        | Extraer clase o mover método (opcional)                      |


---

<br>

## Refactorización 1: `cambiarEstadoTarea()` – Extracción de Método

### Problema identificado

Detecté que el método `cambiarEstadoTarea()` agrupaba múltiples responsabilidades: lectura del ID, selección del nuevo estado y actualización de la tarea. Esta falta de cohesión dificultaba la lectura y el mantenimiento.

### Refactorización aplicada

Apliqué el patrón **Extracción de Método**, separando la lógica en dos funciones privadas:

```kotlin
private fun cambiarEstadoTarea() {
    try {
        println("\n=== CAMBIAR ESTADO DE TAREA ===")
        val id = solicitarIdTarea()
        val nuevoEstado = solicitarNuevoEstado() ?: run {
            println("Opción no válida")
            return
        }
        servicio.cambiarEstadoTarea(id, nuevoEstado)
        println("Estado actualizado exitosamente!")
    } catch(e: NumberFormatException) {
        println("Error: ID debe ser un número")
    } catch(e: IllegalArgumentException) {
        println("Error: ${e.message}")
    }
}

private fun solicitarIdTarea(): Int {
    print("Ingrese el ID de la tarea: ")
    return leerCadena().toInt()
}

private fun solicitarNuevoEstado(): EstadoTarea? {
    print("Seleccione el nuevo estado (1. ABIERTA, 2. EN_PROGRESO, 3. ACABADA): ")
    return when(leerOpcion()) {
        1 -> EstadoTarea.ABIERTA
        2 -> EstadoTarea.EN_PROGRESO
        3 -> EstadoTarea.ACABADA
        else -> null
    }
}
```

### Herramienta utilizada

Para realizar la extracción, utilicé la funcionalidad de IntelliJ IDEA (`Refactor > Extract > Method`) de forma guiada y segura.

### Pruebas

Implementé pruebas unitarias que simulan entradas válidas e inválidas, comprobando que el comportamiento del método permanece correcto tras la refactorización.


---

<br>

## Refactorización 2: `mostrarMenuFiltrado()` – Introducir Objeto Parámetro

### Problema identificado

El método recibía múltiples parámetros sueltos, lo que generaba duplicidad, poca cohesión y menor legibilidad.

### Refactorización aplicada

Definí un DTO llamado `FiltroActividadDTO` que encapsula todos los filtros:

```kotlin
data class FiltroActividadDTO(
    val tipo: String? = null,
    val estado: EstadoTarea? = null,
    val etiquetas: List<String>? = null,
    val nombreUsuario: String? = null,
    val fechaFiltro: String? = null
)
```

En el método, construyo este objeto y lo paso directamente al servicio:

```kotlin
val filtro = FiltroActividadDTO(tipo, estado, etiquetas, nombreUsuario, fechaFiltro)
val filtradas = servicio.filtrarActividades(filtro)
```

### Beneficios

- Reducción de parámetros sueltos.
- Código más limpio y mantenible.
- Mayor reutilización del objeto `DTO` (por ejemplo, en pruebas o API REST).

### Pruebas

Verifiqué que los filtros aplicados ofrecían los mismos resultados que antes, asegurando la continuidad del funcionamiento.

---

<br>

## Refactorización 3: `crearActividad()` – Simplificar Condicional

### Problema identificado

El uso de `when` con varias ramas generaba un bloque largo, poco flexible y difícil de escalar.

### Refactorización aplicada

Reemplacé el `when` por un mapa de opciones con lambdas:

```kotlin
val opcion = leerOpcion()

        val opciones = mapOf(
            1 to { crearTarea() },
            2 to { crearEvento() },
            3 to { println("Creación cancelada.") }
        )

        val accion = opciones[opcion] ?: { println("Opción no válida") }
        accion()
```

### Beneficios

- Código más compacto y flexible.
- Mayor claridad al vincular opciones con acciones.
- Facilita la adición de nuevos tipos de actividades.

### Pruebas

Comprobé que las opciones válidas funcionaban correctamente y que las inválidas mostraban el mensaje adecuado.
