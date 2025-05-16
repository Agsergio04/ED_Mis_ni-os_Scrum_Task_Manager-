# Alfonso Castejón Moreno 1DAWB<br>

## Descripción:<br>
### La actividad consiste en instalar y usar un analizador de código estático (Detekt o Ktlint) en el proyecto que vienes desarrollando, capturar evidencias gráficas, detectar y clasificar errores, aplicar soluciones y explorar las posibilidades de configuración de la herramienta elegida.<br>
### enlace al repositorio actualizado: https://github.com/Agsergio04/ED_Mis_ninios_Scrum_Task_Manager-/tree/AJCM_Linting/src/main/kotlin
### enlace al repositorio sin actualizar: https://github.com/Agsergio04/ED_Mis_ninios_Scrum_Task_Manager-/tree/master/src/main/kotlin

## Instalación KTLINT<br>
![img1.png](img/ACM/img1.png)<br>
![img2.png](img/ACM/img2.png)<br>

## 1 Falta una coma al final del último parámetro en una declaración multilínea. <br>
![3.png](img/ACM/3.png)<br>
![4.png](img/ACM/4.png)<br>

## 2 Importación sin uso<br>
![5.png](img/ACM/5.png)<br>
![6.png](img/ACM/6.png)<br>
![7.png](img/ACM/7.png)<br>

## 3 Variable sin uso<br>
![8.png](img/ACM/8.png)<br>
![9.png](img/ACM/9.png)<br>
![img.png](img/ACM/10.png)<br>

## 4 Espacios innecesarios
![img.png](img/ACM/11.png)<br>
![img.png](img/ACM/12.png)<br>
![img.png](img/ACM/13.png)<br>

## 5 Usas una importación comodín
![img.png](img/ACM/14.png)<br>
![img.png](img/ACM/15.png)<br>
![img.png](img/ACM/16.png)<br>

## Modificación de configuración: Wildcard Imports en Detekt
- Regla: WildcardImport controla el uso de imports con *.
- Por defecto: está activa, lo que prohíbe usar wildcard imports.
- Cambio: para permitir wildcard imports:
``
style:
WildcardImport:
active: false
``

# [1]<br>
## 1.a ¿Que herramienta has usado, y para que sirve?<br>
### He utilizado la herramienta ktlint, y sirve para  para analizar y formatear automáticamente el código.<br>
## 1.b ¿Cuales son sus características principales?<br>
### Linter, formateador, sigue las reglas de estilo de Kotlin, fácil integración con IDEs, detecta errores comunes de estilo.<br>
## 1.c ¿Qué beneficios obtengo al utilizar dicha herramienta?<br>
### Más fácil y rápido de ordenar código que si lo haces a mano.<br>
# [2]<br>
## 2.a De los errores/problemas que la herramienta ha detectado y te ha ayudado a solucionar, ¿cual es el que te ha parecido que ha mejorado más tu código?<br>
### Eliminar importaciones que no son útiles.<br>
## 2.b ¿La solución que se le ha dado al error/problema la has entendido y te ha parecido correcta?<br>
### Si, ya que ayuda a limpiar código.<br>
## 2.c ¿Por qué se ha producido ese error/problema?<br>
### Por no realizar buenas practicas en el código.<br>
# [3]<br>
## 3.a ¿Que posibilidades de configuración tiene la herramienta?<br>
### - Desactivar reglas específicas vía .editorconfig<br>
### - Permitir o prohibir wildcard imports (import *)<br>
### - Ajustar el tamaño de tabulación (espacios vs tabs)<br>
## 3.b De esas posibilidades de configuración, ¿cuál has configurado para que sea distinta a la que viene por defecto?<br>
### - Permitir o prohibir wildcard imports (import *)<br>
## 3.c Pon un ejemplo de como ha impactado en tu código, enlazando al código anterior al cambio, y al posterior al cambio,<br>
### han disminuido todas las alertas que aparecían, como se ven en las imágenes y en los enlaces de esta tarea.<br>
# [4]<br>
## 4 ¿Qué conclusiones sacas después del uso de estas herramientas?<br>
### Es una herramienta muy útil si quieres mejorar la legibilidad del código y minimizar errores.<br>
