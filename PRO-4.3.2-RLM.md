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

[Commit anterior](https://github.com/Agsergio04/ED_Mis_ninios_Scrum_Task_Manager-/tree/29747db11722f837bafa7c79e14acb2319c7a598)

[Commit posterior](https://github.com/Agsergio04/ED_Mis_ninios_Scrum_Task_Manager-/tree/a34fb915177b43e20704b5500b5dab66f93c1a7c)

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

[Commit anterior](https://github.com/Agsergio04/ED_Mis_ninios_Scrum_Task_Manager-/tree/29747db11722f837bafa7c79e14acb2319c7a598)

[Commit posterior](https://github.com/Agsergio04/ED_Mis_ninios_Scrum_Task_Manager-/tree/a34fb915177b43e20704b5500b5dab66f93c1a7c)

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

[Commit anterior](https://github.com/Agsergio04/ED_Mis_ninios_Scrum_Task_Manager-/tree/29747db11722f837bafa7c79e14acb2319c7a598)

[Commit posterior](https://github.com/Agsergio04/ED_Mis_ninios_Scrum_Task_Manager-/tree/a34fb915177b43e20704b5500b5dab66f93c1a7c)

### Beneficios

- Código más compacto y flexible.
- Mayor claridad al vincular opciones con acciones.
- Facilita la adición de nuevos tipos de actividades.

### Pruebas

Comprobé que las opciones válidas funcionaban correctamente y que las inválidas mostraban el mensaje adecuado.

---

# Pruebas Unitarias

## Introducción

Para garantizar que las refactorizaciones aplicadas no afectaran la funcionalidad existente, diseñé un conjunto de pruebas unitarias utilizando **JUnit 5** y el framework **MockK** para simular dependencias y entradas de usuario. Estas pruebas validan que los métodos refactorizados continúan funcionando correctamente con distintos escenarios de entrada.

---

## Detalle de pruebas implementadas

### 1. Prueba: `cambiarEstadoTarea actualiza correctamente el estado`

- **Objetivo:** Verificar que el servicio `ActividadService` recibe correctamente la llamada para cambiar el estado de una tarea con parámetros válidos.
- **Descripción:** Uso un mock relajado para `ActividadService` y verifico que el método `cambiarEstadoTarea` es invocado con los argumentos esperados (ID=5, estado=EN_PROGRESO).
- **Resultado esperado:** La llamada al método se realiza correctamente y no lanza excepciones.
- **Cobertura:** Refactorización de Extracción de Método en `cambiarEstadoTarea()`.

---

### 2. Prueba: `crearActividad llama a crearTarea si elige opcion 1`

- **Objetivo:** Confirmar que cuando la opción seleccionada es 1, se llama al método `crearTarea` con los parámetros adecuados.
- **Descripción:** Mediante un mock relajado, se verifica la invocación de `crearTarea("Hacer ejercicio", "salud;deporte")`.
- **Resultado esperado:** El método es llamado una vez con los parámetros exactos.
- **Cobertura:** Refactorización de Simplificación de Condicional en `crearActividad()`.

---

### 3. Prueba: `crearActividad llama a crearEvento si elige opcion 2`

- **Objetivo:** Validar que al elegir la opción 2, se invoque correctamente `crearEvento` con los parámetros dados.
- **Descripción:** Se simula la llamada a `crearEvento("Evento prueba", "2025-06-01", "Madrid", "etiqueta1;etiqueta2")` y se verifica su ejecución.
- **Resultado esperado:** Confirmación de la invocación con los parámetros esperados.
- **Cobertura:** Refactorización de Simplificación de Condicional en `crearActividad()`.

---

### 4. Prueba: `mostrarMenuFiltrado llama a filtrarActividades con datos correctos`

- **Objetivo:** Asegurar que el método `mostrarMenuFiltrado` construye correctamente el objeto filtro y llama a `filtrarActividades` con los datos esperados.
- **Descripción:** Simulo múltiples líneas de entrada que corresponden a cada campo de filtro y verifico que el método del servicio recibe un objeto `FiltroActividadDTO` con valores correctos.
- **Resultado esperado:** `filtrarActividades` es llamado exactamente una vez con el filtro esperado.
- **Cobertura:** Refactorización de Introducir Objeto Parámetro en `mostrarMenuFiltrado()`.

---

## Herramientas y técnicas usadas

- **MockK:** Para crear mocks relajados de los servicios y controlar las respuestas de funciones dependientes.
- **Simulación de entradas:** Mediante la función `mockkStatic` y la redefinición de `readLine()`, simulo las entradas del usuario necesarias para las pruebas.
- **Verificación:** Uso de `verify { ... }` para asegurar que las llamadas esperadas a los métodos se realizan con los parámetros correctos.

---

## Conclusión

Con estas pruebas unitarias, aseguro que las refactorizaciones mantienen la integridad funcional del código, permitiendo detectar rápidamente cualquier desviación o error introducido. La estrategia de mocks facilita la separación entre la lógica de presentación y los servicios subyacentes, mejorando la mantenibilidad y facilitando futuras ampliaciones.

![Captura](capturas/Captura%20de%20pantalla%202025-05-22%20104516.png)


---


# Respuestas a Preguntas

## [1.a] ¿Qué code smell y patrones de refactorización has aplicado?

Durante el análisis del proyecto detecté cinco code smells principales, de los cuales seleccioné tres para aplicar patrones de refactorización distintos:

- **Code Smells detectados:**
  - Método largo con múltiples responsabilidades en `cambiarEstadoTarea()`.
  - Código duplicado y repetitivo en la lectura de consola.
  - Flujo de entrada complejo con múltiples parámetros sueltos en `mostrarMenuFiltrado()`.
  - Condicionales complejos con múltiples ramas en `crearActividad()` y `cambiarEstadoTarea()`.
  - Baja cohesión en algunas clases que mezclan lógica.

- **Patrones de refactorización aplicados:**
  1. **Extracción de Método** para separar responsabilidades en `cambiarEstadoTarea()`.
  2. **Introducir Objeto Parámetro** creando un DTO para agrupar filtros en `mostrarMenuFiltrado()`.
  3. **Simplificar Condicional** reemplazando un `when` por un mapa de opciones en `crearActividad()`.

Estos patrones han permitido mejorar la legibilidad, cohesión y mantenibilidad del código.

---

## [1.b] Selección de patrón con pruebas unitarias: ¿Por qué mejora o no mejora el código?

He seleccionado el patrón **Extracción de Método** aplicado en `cambiarEstadoTarea()`, que está cubierto por pruebas unitarias detalladas.

- **Mejora que aporta:**
  - Se separan las responsabilidades en funciones pequeñas y claras (`solicitarIdTarea()` y `solicitarNuevoEstado()`), facilitando la comprensión y el mantenimiento.
  - Facilita la reutilización y la posible ampliación de la funcionalidad.
  - Reduce la complejidad del método original, disminuyendo el riesgo de errores.
  - Permite realizar pruebas unitarias específicas y focalizadas, mejorando la cobertura y la detección de fallos.

---

## [2.a] Proceso para asegurar que la refactorización no afecta código previo

Para garantizar que la refactorización no introduce regresiones ni afecta la funcionalidad existente, sigo este proceso:

1. **Análisis previo:** Identifico claramente qué partes del código se ven afectadas y diseño pruebas unitarias específicas para ellas si no existen.
2. **Pruebas antes y después:** En al menos una refactorización, genero pruebas unitarias antes del cambio para asegurar que la funcionalidad actual está cubierta.
3. **Uso de mocks:** Para aislar las dependencias, empleo mocks que me permitan controlar el comportamiento de servicios externos o componentes relacionados.
4. **Ejecución continua:** Después de aplicar cada refactorización, ejecuto todas las pruebas unitarias para verificar que ninguna falla.
5. **Revisión de resultados:** Analizo los resultados para detectar cualquier comportamiento inesperado, y si se detecta, ajusto el código o las pruebas hasta resolverlo.

Este proceso me da confianza de que los cambios mejoran el código sin romper funcionalidades ya implementadas.

---

## [3.a] Funcionalidad del IDE utilizada para aplicar la refactorización seleccionada

Para la refactorización de **Extracción de Método** en `cambiarEstadoTarea()`, utilicé la funcionalidad nativa de **IntelliJ IDEA**:

- Accedí al menú `Refactor > Extract > Method` tras seleccionar el bloque de código que quería extraer.
- IntelliJ me guió en la creación del nuevo método, ajustando automáticamente las variables y parámetros necesarios.
- El IDE realiza análisis estático para asegurar que la extracción es segura, evitando errores de compilación.
- Esta funcionalidad ofrece previsualización de cambios y permite renombrar el método durante la extracción.

A continuación, una captura de pantalla de la herramienta en acción durante el proceso:

![Captura](capturas/Captura%20de%20pantalla%202025-05-22%20010647.png)


