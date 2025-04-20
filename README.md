# 📝 ACTA DE REUNIÓN CON EL CLIENTE

**Proyecto**: TaskManager  
**Fecha**: 07/04/2025  
**Cliente**: Dpto. de Coordinación de Proyectos  
**Representante del cliente**: Eduardo Fernández, Responsable de Operaciones  
**Asistentes**: Equipo de desarrollo de TaskManager (vosotros)  
**Duración**: 45 minutos  

---

## 🗣️ Resumen de la Reunión
Durante la reunión, el cliente expuso nuevas necesidades funcionales detectadas tras el uso interno del sistema TaskManager en proyectos colaborativos. A continuación se detallan los puntos clave que deberán analizarse y traducirse en tareas de desarrollo.

---

## 📌 Nuevas Necesidades Funcionales Detectadas

### 1. Gestión de estados avanzados en tareas
- Estados posibles: **ABIERTA**, **EN_PROGRESO**, **FINALIZADA**.
- El usuario debe poder cambiar el estado manualmente.
- **Restricción**: No cerrar una tarea madre si tiene subtareas abiertas.
- **Automatización**: Cierre automático de la tarea madre al finalizar todas las subtareas.

### 2. Asignación de tareas a usuarios
- Creación y registro de usuarios en el sistema.
- Asignación de tareas a usuarios específicos.
- Consulta de tareas asignadas por usuario.

### 3. Etiquetas en actividades
- Asociación de etiquetas a tareas y eventos (ej: `urgente`, `documentación`).
- **Formato de entrada**: Etiquetas separadas por `;`.
- Visualización de etiquetas en listados.

### 4. Filtrado por distintos campos
- Criterios de filtrado:
  - Tipo (Tarea/Evento).
  - Estado.
  - Etiquetas.
  - Usuario asignado.
  - Fecha (hoy, mañana, semana, mes).
- Acceso mediante menú o módulo específico.

### 5. Gestión de subtareas
- Subtareas asociadas a una tarea principal (**un solo nivel de profundidad**).
- **Restricción de cierre**: Bloquear cierre si hay subtareas abiertas.
- Visualización de subtareas desde la tarea principal.
- Subtareas como entidades completas pero dependientes jerárquicamente.

### 6. Panel de control (Dashboard)
- **Métricas**:
  - Total de tareas madre con subtareas.
  - Distribución por estados.
  - Eventos programados (hoy, mañana, semana, mes).
- Acceso desde el menú principal.

### 7. Historial de actividades
- Registro de acciones clave: cambios de estado, asignaciones, cierres.
- **Detalles del historial**: Fecha y descripción de la acción.
- Consulta desde la aplicación.

---

## 📎 Notas adicionales del cliente
- La aplicación **sigue siendo por consola**.
- Posibilidad futura de exportar datos (no prioritaria).
- Diseño sencillo pero escalable.

---

## ✅ Acuerdos de cierre
- Priorización de funcionalidades según criterio del equipo.
- Integración de nuevas funcionalidades en el menú existente.
- Mantenimiento del correcto funcionamiento del sistema actual.

---

## 📌 Siguiente paso
El equipo de desarrollo debe:
1. Analizar las funcionalidades propuestas.
2. Identificar clases y servicios a modificar/crear.
3. Elaborar un plan de implementación por iteraciones.
4. Implementar asegurando integración y calidad.

---

# Anexo: Resumen de la Funcionalidad Base de TaskManager

## 🎯 Objetivo principal
Gestionar actividades (Tareas/Eventos) en proyectos colaborativos mediante consola interactiva.

## 🧱 Funcionalidades disponibles
1. **Crear actividad**:
   - Tarea: Descripción + estado inicial `ABIERTA`.
   - Evento: Descripción + fecha + ubicación.
   - ID único generado automáticamente.
   - Uso de `creaInstancia()` en lugar de constructores públicos.

2. **Listar actividades**:
   - Visualización de ID, descripción y detalles específicos:
     - Tareas: Estado (`ABIERTA`).
     - Eventos: Fecha y ubicación.

3. **Menú principal**:
   - Opciones básicas: Crear, Listar, Salir.

## ⚙️ Características técnicas
- Herencia: `Tarea` y `Evento` heredan de `Actividad`.
- Arquitectura por capas:
  - Presentación (consola).
  - Lógica de negocio (`ActividadService`).
  - Persistencia en memoria (`List<Actividad>`).
- Diseño SOLID (SRP, DIP).

---

# 📘 Estrategias para Descomponer Historias de Usuario Grandes

## 🔟 Estrategias clave
1. **Pasos de flujo de trabajo**  
   Ej: Descomponer "pago de carrito" en login, confirmación, métodos de pago, email de confirmación.

2. **Reglas de negocio**  
   Ej: Restricciones de órdenes menores a 10€ o envíos internacionales.

3. **Flujos de éxito/fallo**  
   Ej: Login exitoso vs. recuperación de contraseña.

4. **Plataformas/Dispositivos**  
   Ej: Desktop, móvil, pantalla táctil.

5. **Tipos de datos/parámetros**  
   Ej: Búsqueda de productos por precio, color, categoría.

6. **Operaciones CRUD**  
   Ej: Crear, actualizar, eliminar productos.

7. **Casos de prueba**  
   Ej: Asignación de tareas con empleados enfermos o sin carga.

8. **Roles de usuario**  
   Ej: Cliente, periodista, editor en sistema de artículos.

9. **Optimización progresiva**  
   Ej: Búsqueda básica vs. autocompletado por GPS.

10. **Compatibilidad**  
    Ej: Funcionamiento en navegadores modernos vs. IE7.

---

## 💡 Otras estrategias
- Criterios de aceptación.
- Dificultad de implementación.
- Requisitos SEO/usabilidad.

---

**Enlace recomendado**: [10 estrategias para descomponer historias](https://medium.com/the-liberators/10-powerful-strategies-for-breaking-down-user-stories-in-scrum-with-cheatsheet-2cd9aae7d0eb)

---

## Responsabilidad 

-Rocio : Apatados 5
-Pablo : Apatados 2
-Sergio : Apartados 1 y 3 
-Juan : Apatados 6
-Alfonso : Apatados 7
