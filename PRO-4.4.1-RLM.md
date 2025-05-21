# Generación de Documentación con Dokka

````
Para esta práctica he decidido documentar archivo Main.kt, así como las tres clases incluidas en el package
'aplicacion': ActividadService, DashboardService y HistorialService.
````

Acontinuación explico los pasos que he seguido junto a capturas de pantalla como prueba:

---

## Documentar el código en KDoc

Primero revisé las clases y su funcionamiento para, posteriormente, añadir bloques de documentación usando la sintaxis KDoc para describir el propósito de cada clase, el objetivo y 
funcionamiento de cada método y, dentro de cada uno de ellos, indicar los parámetros de entrada y la salida esperada.
Se revisaron varias clases del proyecto y se añadieron bloques de documentación usando la sintaxis 

[CAPTURAS]

## Configuración de Dokka en el proyecto Kotlin

Para poder generar la documentación automáticamente con Dokka, realicé una serie de cambios en el archivo ``build.gradle.kts``:

- Integré el plugin Dokka ``id("org.jetbrains.dokka") version "1.8.10"``.
- Configuré la tarea dokkaHtml ``tasks.dokkaHtml.configure { outputDirectory.set(buildDir.resolve("dokka"))}``.
- Ajusté la versión del plugin de Kotlin a ``1.8.21``` para asegurar la compatibilidad.


## Generación de documentación

Primero sincronicé Gradle y, luego, me moví a su panel lateral para ejecutar la tarea dokkaHtml: ``Tasks > dokka > dokkaHtml``. Pulse dos veces en dokkaHtml para ejecutarlo.

[captura]

Posteriormente, me dirigí a la terminal para generar la documentación con el comando ``./gradlew dokkaHtml``.

[captura]

---

## Capturas de la página generada:

````
https://agsergio04.github.io/ED_Mis_ninios_Scrum_Task_Manager-/task_management_-scrum/org.example/main.html
````
