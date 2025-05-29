# Task - Manager Scrum Debugging

## ERROR 1 — Asignación de usuario a eventos

### Descripción del problema

Al intentar asignar un usuario a un evento, observamos que no se gestionaba correctamente. Esto se debía a que el sistema solo contemplaba clases de tipo `Tarea`, y no permitía aplicar la lógica de asignación sobre clases de tipo `Evento`.

### Diagnóstico y solución

Tras revisar el código de la clase `Evento`, observamos que, para permitir la asignación de usuarios, era necesario modificar también el servicio `ActividadService`. Este servicio originalmente estaba limitado a objetos de tipo `Tarea`. Para resolverlo:

1. **Identificamos la limitación** en el método correspondiente dentro de `ActividadService`, donde se comprobaba la clase del objeto.
2. **Ampliamos el alcance de la comprobación** para que aceptara también instancias de la clase `Evento`, utilizando comprobaciones con `is Evento` y `is Tarea`.
3. **Aplicamos un punto de interrupción** en la línea donde se asignaba el usuario para comprobar si la ejecución llegaba correctamente al bloque modificado.
4. **Verificamos el comportamiento esperado**, observando que tras ejecutar en modo Debug, la asignación de usuario a un evento se realizaba correctamente.

### Conclusión

Este cambio requirió tanto adaptación del código como verificación mediante el depurador. Nos apoyamos en técnicas como análisis de clases en tiempo de ejecución y pruebas paso a paso usando breakpoints, garantizando así que el sistema pudiera trabajar de forma polimórfica con distintas subclases de `Actividad`.


## ERROR 2 — Estado de la tarea no se actualiza correctamente

### Descripción del problema

El cambio de estado de una tarea a “ACABADA” no se realizaba correctamente. Al modificar el estado y luego listar las tareas, estas seguían apareciendo con el estado “ABIERTA”.

### Procedimiento de depuración aplicado

Se utilizó el depurador de IntelliJ IDEA, aplicando técnicas como:

- **Breakpoints**
- **Inspección de variables**
- **Ejecución paso a paso**

Pasos realizados:

1. Se colocó un breakpoint en el método `cambiarEstado` de `Actividad.kt`.
2. Al ejecutar el programa en modo Debug, se creó una tarea con estado “ACABADA”.
3. En el punto de interrupción, se inspeccionó la variable `estado` y se confirmó que no se estaba asignando correctamente.
4. El valor se mantenía como “ABIERTA” tras finalizar el método.
   
![image](https://github.com/user-attachments/assets/3161c6e8-02f9-4c9d-ad71-2c334f3b74f4)
![image](https://github.com/user-attachments/assets/9fd6a478-6774-4f79-ab80-2f98c037be43)


### Solución

Se corrigió el método `cambiarEstado` para que asignara correctamente el nuevo estado:

![image](https://github.com/user-attachments/assets/a68aed60-9039-4269-b549-88266a063a23)
![image](https://github.com/user-attachments/assets/83f0cb75-244a-46c6-842c-da8bb94684eb)


```kotlin
fun cambiarEstado(estado: EstadoTarea) {
    this.estado = estado
}
```

Se repitió el proceso de depuración y se comprobó que el estado se actualizaba como esperado.

### Conclusión técnica

El uso del depurador permitió verificar en tiempo real la asignación del estado. Esta experiencia reforzó el uso del seguimiento por breakpoints y la inspección de propiedades del objeto como herramientas efectivas de diagnóstico.


## ERROR 3 — ID de usuario constante

### Descripción del problema

Cada vez que se creaba un nuevo usuario, este obtenía siempre el mismo identificador (`ID = 1`), lo cual causaba duplicidades y errores de gestión.

### Diagnóstico y solución

Revisando el método de generación de ID, detectamos que no se estaba incrementando correctamente. Aplicamos una solución sencilla:

1. **Inspeccionamos la función que generaba los IDs**, y observamos que el valor no persistía entre llamadas.
2. **Modificamos el método** para que cada vez que se generara un nuevo ID, este se incrementara en 1.
3. Verificamos mediante el depurador que el valor retornado en cada nueva creación de usuario era distinto al anterior.

### Conclusión

Se trató de un error común en lógica de generación incremental. El depurador ayudó a confirmar que el valor retornado por la función de ID cambiaba correctamente con cada invocación.


## ERROR 4 — Historial no muestra registros

### Descripción del problema

Al seleccionar la opción “Ver historial de actividad” desde `ConsolaUI`, no se mostraba ningún resultado, aunque se hubieran creado o modificado tareas previamente.

### Diagnóstico

El flujo de ejecución era correcto, pero el repositorio de historial devolvía una lista vacía. Activamos el **logging en nivel DEBUG** y obtuvimos:

```log
[DEBUG] No se registró ninguna acción
```

Esto nos indicó que nunca se llamaba al método `historialService.registrarAccion(...)`.

### Solución

1. Añadimos una llamada explícita a `registrarAccion()` en los métodos de creación y actualización de tareas.
2. Agregamos también un log del ID de la tarea creada para facilitar la trazabilidad posterior.
3. Volvimos a ejecutar el programa y observamos, tanto en la salida como en el archivo `app.log`, que ahora se registraban correctamente las acciones realizadas.

### Conclusión

El uso de logging fue clave en esta depuración. A través del registro en tiempo real, pudimos identificar que el flujo era correcto, pero faltaba una llamada esencial. Esta técnica complementa al uso de breakpoints, sobre todo en casos donde se necesita trazabilidad persistente.


## ERROR 5 — dashboardService no accesible desde ConsolaUI

### Descripción del problema

Aunque `dashboardService` se pasaba al constructor de `ConsolaUI`, no se almacenaba como propiedad. Por ello, al llamar al método `mostrarDashboard()`, se producía un error al intentar acceder al servicio.

#### Fragmentos de código antes de la resolución

![image](https://github.com/user-attachments/assets/ebd842f9-6300-4676-be6c-5acf37f9d302)
![image](https://github.com/user-attachments/assets/f58032ea-9915-4f33-9ec9-08ed532080e0)
![image](https://github.com/user-attachments/assets/c2eec673-eb61-49e9-8c93-1446a2bc3184)ç

### Diagnóstico y solución

1. Revisamos la definición de la clase `ConsolaUI` y detectamos que `dashboardService` no se guardaba como propiedad.
2. Al ejecutar en modo Debug, el valor de `dashboardService` era `null`.
3. Solucionamos el error declarando `dashboardService` como una propiedad de clase y asignándole el valor del constructor.

#### Fragmentos de código despues de la resolución

![image](https://github.com/user-attachments/assets/b0c17b64-0e1f-4959-8096-5c93e99dcc84)
![image](https://github.com/user-attachments/assets/98b4c19c-7549-4a9c-959d-ad80de692f64)

### Conclusión

Este error fue detectado al intentar ejecutar una funcionalidad nueva que dependía de una propiedad no inicializada. El debugger mostró el valor `null`, confirmando la causa. La solución fue simple pero representativa del valor del análisis estructural del código y el seguimiento del flujo de datos.
