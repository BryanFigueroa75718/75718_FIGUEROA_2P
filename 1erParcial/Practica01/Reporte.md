# Practica 1 - Seccion Critica

*Universidad Autonoma de Campecne*
*Concurrencia y Paralelismo*

**Docente: Sergio Andres Noh Puch**
**Alumno: Pedro Alejandro Chavez Rivero**

La intencion de esta practica es que nosotros seamos capaces de implementar el pseudocódigo proporcionado por el docente y experimentar con diferentes mecanismos de sincronización para entender y explicar los conceptos solicitados a continuación. En esta practica se utilizó el editor VSCode y para la ejecucion la terminal de Ubuntu de WSL.

## 1. Condición de carrera:
Como ya vimos en clase una condición de carrera es cuando dos o mas hilos intentan acceder a un recurso compartido al mismo tiempo. Esto sucede por que la CPU, al gestionar de manera impredecible los tiempos asignados a cada hilo acaba mezclando el orden de las operaciones. Esto puede causar que unos hilos sobreescriban a otros dando resultados inconsistentes o en algunos casos incorrectos.

## 2. Identifica las instucciones:

* movl: Es el encargado de mover el valor actual de la memoria principal a un registro de la CPU. Por ejemplo, en el archivo generado despues de que ejecuté el `.c` podemos encontrar el registro `%eax`.

* addl: Esta instrucción lo único que hace es realizar una operación matemática de manera directa en el registro de la CPU, para ser mas específicos esta suma 1.

* subl: Funciona de manera similar a la anterior instucción pero en lugar de sumar 1 esta resta 1.

## Explica el concepto de sección crítica:
A lo que nos referimos como sección crítica se trata en realidad de un bloque de código específico, aqui los hilos del procesador acceden a una serie de recursos compartidos que como ya vimos, al generar una concurrencia al acceso de estos llega a causar conflictos.
En esta práctica se hizo uso de una exclusión mutua, que es lo que en codigo vemos como `mutex`, esto lo que hace es proteger a la sección critica para que las instrucciones se completen primero en un solo hilo antes de poder acceder al recurso compartido.