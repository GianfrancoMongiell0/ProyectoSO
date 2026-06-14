<div align="center">

# 🖥️ Simulador de Planificación de Procesos

### Sistemas Operativos I — Universidad Metropolitana (UNIMET)

[![Java](https://img.shields.io/badge/Java-SE-ED8B00?style=flat-square&logo=openjdk)](https://java.com)
[![Swing](https://img.shields.io/badge/GUI-Java%20Swing-ED8B00?style=flat-square)](https://docs.oracle.com/javase/tutorial/uiswing)
[![Concurrencia](https://img.shields.io/badge/Threads-Semaphores-5A0FC8?style=flat-square)](https://docs.oracle.com/javase/tutorial/essential/concurrency)
[![Universidad](https://img.shields.io/badge/UNIMET-Ing.%20Sistemas-004A8F?style=flat-square)](https://www.unimet.edu.ve)

**Simulador de escritorio que implementa 5 algoritmos de planificación de CPU con concurrencia real, manejo de procesos I/O-bound y visualización en tiempo real.**

</div>

---

## ¿Qué es?

Este proyecto simula el comportamiento interno de un sistema operativo en lo que respecta a la **planificación de CPU**. No es una simulación estática — cada CPU es un hilo real (`Thread`) de Java que compite por procesos usando semáforos (`Semaphore`) para sincronización, exactamente como lo haría un SO real.

Permite cargar procesos, elegir el algoritmo de planificación y ver en tiempo real cómo el SO asigna, bloquea, desbloquea y termina cada proceso.

---

## Algoritmos implementados

| Algoritmo | Tipo | Descripción |
|-----------|------|-------------|
| **FCFS** | No expulsivo | First Come First Served — ejecuta en orden de llegada hasta terminar |
| **SJF** | No expulsivo | Shortest Job First — prioriza el proceso con menos instrucciones totales |
| **SRT** | **Expulsivo** | Shortest Remaining Time — versión preemptiva de SJF; interrumpe al proceso actual si llega uno más corto |
| **Round Robin** | **Expulsivo** | Quantum configurable (default: 5 ciclos); reencola el proceso al agotar su tiempo |
| **HRRN** | No expulsivo | Highest Response Ratio Next — calcula `(espera + servicio) / servicio` para evitar inanición |

---

## Arquitectura del sistema

El diseño sigue la estructura real de un SO con separación clara de responsabilidades:

```
src/
│
├── Clases/
│   ├── Proceso.java          # Entidad proceso con instrucciones ejecutables
│   ├── PCB.java              # Process Control Block — estado, PC, MAR, tipo I/O
│   ├── CPU.java              # Hilo independiente; ejecuta procesos según política activa
│   └── SistemaOperativo.java # Orquestador: gestiona colas, CPUs y GUI
│
├── Planificacion/
│   ├── Planificador.java     # Clase abstracta con mutex compartido y cola de listos
│   ├── FCFS.java
│   ├── SJF.java
│   ├── SRT.java              # Lógica de preemption con Semaphore
│   ├── RoundRobin.java       # Quantum y reencola
│   └── HRRN.java             # Cálculo de response ratio + reordenamiento de cola
│
├── Estructuras/
│   ├── Lista.java            # Lista enlazada genérica propia
│   ├── Nodo.java
│   └── Queue.java            # Cola FIFO genérica propia (no usa java.util)
│
├── Interfaces/
│   └── Simulador.java        # GUI Swing — tablas, estado CPUs, ciclo de reloj
│
└── Utils/
    └── LectorEscritorTxt.java
```

> **Nota:** Las estructuras de datos (`Lista`, `Queue`) fueron implementadas desde cero sin usar `java.util.LinkedList` ni `java.util.Queue`.

---

## Concurrencia — cómo funciona realmente

Este es el núcleo técnico del proyecto. Cada CPU es un `Thread` independiente corriendo en paralelo:

```java
// CPU.java — cada CPU es un hilo real
public class CPU extends Thread {
    private java.util.concurrent.Semaphore semaforo;

    @Override
    public void run() {
        while (so.isEnEjecucion()) {
            Proceso proceso = so.obtenerSiguienteProceso(); // sincronizado
            // ...ejecuta instrucciones ciclo a ciclo
        }
    }
}
```

### Sincronización
- **`Semaphore mutex`** en `Planificador` — protege la cola de listos de condiciones de carrera cuando múltiples CPUs compiten por el siguiente proceso
- **`Semaphore semaforo`** en `CPU` — controla la preemption en SRT: antes de seguir ejecutando, la CPU verifica si llegó un proceso con menos tiempo restante
- **`synchronized`** en `SistemaOperativo` — garantiza acceso atómico a las colas de bloqueados y terminados
- **`volatile boolean enEjecucion`** — visibilidad inmediata entre hilos al detener la simulación
- **`SwingUtilities.invokeLater()`** — toda actualización de la GUI ocurre en el Event Dispatch Thread

### Flujo de estados de un proceso

```
           llegada
              │
           [READY] ◄──────────────────────────┐
              │                               │
        CPU disponible                   quantum agotado /
              │                          preemption (SRT)
           [RUNNING]                          │
           /      \                           │
     termina    I/O bound                     │
        │       (bloqueo)                     │
  [TERMINATED]  [BLOCKED]                     │
                    │                         │
              ciclosCompletarExcepcion        │
               (hilo separado)               │
                    └────────────────────────►┘
```

### Preemption en SRT (detalle)

```java
// Dentro del bucle de ejecución de CPU.java para SRT:
semaforo.acquire();
if (!colaListos.isEmpty()) {
    Proceso enCola = colaListos.peek();
    if (enCola.getInstruccionesRestantes() < proceso.getInstruccionesRestantes()) {
        // Preemption: el proceso actual cede la CPU
        procesoListoParaReencolar(proceso);
        semaforo.release();
        break;
    }
}
semaforo.release();
```

---

## Procesos CPU-bound vs I/O-bound

El simulador distingue dos tipos de proceso a través del `PCB`:

| Tipo | Comportamiento |
|------|---------------|
| **CPU-bound** | Ejecuta todas sus instrucciones sin interrupciones |
| **I/O-bound** | Se bloquea después de `ciclosExcepcion` ciclos; vuelve a READY tras `ciclosCompletarExcepcion` ciclos (manejado por un hilo separado) |

```java
// PCB.java — lógica de bloqueo I/O
public boolean debeBloquearse() {
    if (esIOBound) {
        ciclosEjecutadosDesdeUltimoBloqueo++;
        if (ciclosEjecutadosDesdeUltimoBloqueo > ciclosExcepcion) {
            ciclosEjecutadosDesdeUltimoBloqueo = 0;
            return true; // debe bloquearse
        }
    }
    return false;
}
```

---

## Cómo ejecutar

**Requisitos:** JDK 8+ · NetBeans IDE (recomendado)

```bash
git clone https://github.com/GianfrancoMongiell0/ProyectoSO
```

1. Abre **NetBeans → Open Project** y selecciona `Proyecto-SistemasOperativos/`
2. Run → `ProyectoSistemasOperativos.java`
3. En la GUI: carga procesos, selecciona el algoritmo y presiona **Iniciar**

---

## Autores

**Gianfranco Mongiello** · **Alejandra Carrera**  
Materia: Sistemas Operativos — Universidad Metropolitana (UNIMET), Caracas 🇻🇪
