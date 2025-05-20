## Descripción de Code Smells y Patrones de Refactorización

### Code Smells Detectados
1. **Lógica de Filtrado Incorrecta**  
   - **Archivo**: `ActividadService.kt`  
   - **Descripción**: En `filtrarActividades`, la búsqueda de usuarios por nombre usaba `it.nombre.equals(it)` en lugar de comparar con `nombreUsuario`.  
   - **Patrón aplicado**: _Extracción de Método_ para separar condiciones de filtrado.

2. **Manejo Inseguro de Estado Estático**  
   - **Archivo**: `Usuario.kt`  
   - **Descripción**: El ID se incrementaba estáticamente, lo que podría causar conflictos en entornos multihilo.  
   - **Patrón aplicado**: _Reemplazar Método con Método de Objeto_ para generar IDs de forma segura.

3. **Switch Statement Complejo**  
   - **Archivo**: `Actividad.kt`  
   - **Descripción**: El método `cambiarEstado` usaba un `when` con lógica redundante.  
   - **Patrón aplicado**: _Reemplazar Condicional con Polimorfismo_ mediante una máquina de estados.

4. **Parámetros Excesivos**  
   - **Archivo**: `ActividadService.kt`  
   - **Descripción**: El método `filtrarActividades` tenía 5 parámetros.  
   - **Patrón aplicado**: _Introducir Parámetro Objeto_ (clase `FiltroActividad`).

5. **Validación de Fecha Acoplada**  
   - **Archivo**: `Evento.kt`  
   - **Descripción**: La validación `Utils.esFechaValida(fecha)` dependía de una clase externa.  
   - **Patrón aplicado**: _Extraer Interfaz_ para desacoplar validaciones.

---

## Pruebas Unitarias Asociadas

| **Clase de Test**           | **Método de Test**                          | **Cubre Refactorización**          |
|-----------------------------|--------------------------------------------|-----------------------------------|
| `ActividadServiceTest`      | `filtrarActividades_porUsuario_retornaTareas` | Introducir Parámetro Objeto       |
| `UsuarioTest`               | `crearUsuario_generaIdUnico`               | Reemplazar Método con Método de Objeto |
| `ActividadTest`             | `cambiarEstado_ABIERTA_a_EN_PROGRESO`      | Reemplazar Condicional con Polimorfismo |
| `EventoTest`                | `crearEvento_conFechaInvalida_lanzaExcepcion` | Extraer Interfaz                  |

---

## Respuestas a las Preguntas

### [1]
**1.a**  
Se aplicaron:  
- Extracción de Método (`filtrarActividades`).  
- Reemplazar Método con Método de Objeto (`Usuario.kt`).  
- Introducir Parámetro Objeto (`FiltroActividad`).  
- Reemplazar Condicional con Polimorfismo (`cambiarEstado`).  
- Extraer Interfaz (validación de fechas).  

**1.b**  
**Patrón**: _Introducir Parámetro Objeto_ en `filtrarActividades`.  
**Mejora**: Reduce la complejidad del método y facilita añadir nuevos filtros.  
**Prueba**: `filtrarActividades_porUsuario_retornaTareas` ([código](https://github.com/.../ActividadServiceTest.kt#L45)).  

---

### [2]
**2.a**  
Proceso para asegurar la no regresión:  
1. Ejecutar pruebas existentes antes de refactorizar.  
2. Usar la herramienta _Refactor > Rename_ del IDE para cambios de nombres.  
3. Validar con pruebas unitarias después de cada cambio.  
4. Aprovechar el análisis estático del IDE (p.ej., inspecciones de IntelliJ).  

---

### [3]
**3.a**  
**Funcionalidad del IDE usada**:  
- _Refactor > Extract > Parameter Object_ para crear `FiltroActividad`.  
- _Refactor > Extract Method_ en las condiciones de `filtrarActividades`.  

**Captura**:   