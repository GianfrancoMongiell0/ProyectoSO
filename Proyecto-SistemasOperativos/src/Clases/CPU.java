package Clases;

import planificacion.Planificador;
import planificacion.HRRN; // Asegúrate de importar HRRN si vas a usar tiempoGlobal de ahí.
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class CPU extends Thread {

    private int id;
    private SistemaOperativo so;
    private Proceso procesoEnEjecucion;
    private JLabel labelEstado;
    private int quantumRestante;
    private java.util.concurrent.Semaphore semaforo;

    public CPU(int id, SistemaOperativo so, JLabel labelEstado) {
        this.id = id;
        this.so = so;
        this.labelEstado = labelEstado;
        this.procesoEnEjecucion = null;
        this.semaforo = new java.util.concurrent.Semaphore(1);
    }

    public Proceso obtenerProcesoEnEjecucion() {
        return procesoEnEjecucion;
    }

    @Override
    public void run() {
        System.out.println("CPU " + id + " ha iniciado.");
        while (so.isEnEjecucion()) {
            actualizarEstado("Ejecutando: Sistema Operativo");
            try {
                Proceso proceso = so.obtenerSiguienteProceso();

                if (proceso == null) {
                    Thread.sleep(100); // Espera no activa
                    continue;
                }

                proceso.getPCB().setEstado(PCB.Estado.RUNNING);
                procesoEnEjecucion = proceso;
                String politica = so.getPlanificador().getClass().getSimpleName();

                switch (politica) {
                    case "RoundRobin":
                        quantumRestante = so.getPlanificador().getQuantum();
                        while (proceso.getInstruccionesRestantes() > 0) {
                            Thread.sleep(so.getDuracionCiclo());

                            proceso.ejecutarInstruccion();
                            // **Incrementar tiempoGlobal en cada ciclo de CPU para Round Robin**
                            incrementarTiempoGlobalSimulacion();

                            if (proceso.debeBloquearse()) {
                                proceso.getPCB().setEstado(PCB.Estado.BLOCKED);
                                so.moverAColaBloqueados(proceso);
                                this.liberarProceso();
                                break;
                            }

                            quantumRestante--;

                            if (proceso.estaTerminado()) {
                                proceso.getPCB().setEstado(PCB.Estado.TERMINATED);
                                so.moverAColaTerminados(proceso);
                                this.liberarProceso();
                                break;
                            }

                            if (quantumRestante <= 0) {
                                proceso.getPCB().setEstado(PCB.Estado.READY);
                                so.agregarProceso(proceso);
                                this.liberarProceso();
                                break;
                            }
                        }
                        break;
                    
                   
                    case "HRRN":
                        while (proceso.getInstruccionesRestantes() > 0) {
                            Thread.sleep(so.getDuracionCiclo()); // Simula la ejecución de una instrucción
                            proceso.ejecutarInstruccion(); // Ejecuta una instrucción del proceso
                            // **Incrementar tiempoGlobal en cada ciclo de CPU para HRRN**
                            incrementarTiempoGlobalSimulacion();

                            // Verifica si el proceso debe bloquearse
                            if (proceso.debeBloquearse()) {
                                proceso.getPCB().setEstado(PCB.Estado.BLOCKED);
                                so.moverAColaBloqueados(proceso);
                                this.liberarProceso();
                                break;
                            }

                            // Verifica si el proceso ha terminado
                            if (proceso.estaTerminado()) {
                                proceso.getPCB().setEstado(PCB.Estado.TERMINATED);
                                so.moverAColaTerminados(proceso);
                                this.liberarProceso();
                                break;
                            }
                        }
                        break;

                    case "SFJ":

                        while (proceso.getInstruccionesRestantes() > 0) {
                            Thread.sleep(so.getDuracionCiclo()); // Simula la ejecución de una instrucción
                            proceso.ejecutarInstruccion(); // Ejecuta una instrucción del proceso
                            // **Incrementar tiempoGlobal en cada ciclo de CPU para SFJ - Aunque SJF no lo use directamente, se mantiene por consistencia si otros planificadores en tu sistema lo usan.**
                            incrementarTiempoGlobalSimulacion();

                            // Verifica si el proceso debe bloquearse
                            if (proceso.debeBloquearse()) {
                                proceso.getPCB().setEstado(PCB.Estado.BLOCKED);
                                so.moverAColaBloqueados(proceso);
                                this.liberarProceso();
                                break;
                            }

                            // Verifica si el proceso ha terminado
                            if (proceso.estaTerminado()) {
                                proceso.getPCB().setEstado(PCB.Estado.TERMINATED);
                                so.moverAColaTerminados(proceso);
                                this.liberarProceso();
                                break;
                            }
                        }
                        break;

                    case "SRT":
                        while (proceso.getInstruccionesRestantes() > 0) {
                            try { // **Añadido bloque try-catch dentro del bucle para Thread.sleep()**
                                Thread.sleep(so.getDuracionCiclo());
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break; // Importante: salir del bucle si el thread es interrumpido
                            }

                            proceso.ejecutarInstruccion();
                            // **Incrementar tiempoGlobal en cada ciclo de CPU para SRT**
                            incrementarTiempoGlobalSimulacion();

                            if (proceso.debeBloquearse()) {
                                proceso.getPCB().setEstado(PCB.Estado.BLOCKED);
                                so.moverAColaBloqueados(proceso);
                                this.liberarProceso();
                                break;
                            }

                            if (proceso.estaTerminado()) {
                                proceso.getPCB().setEstado(PCB.Estado.TERMINATED);
                                so.moverAColaTerminados(proceso);
                                this.liberarProceso();
                                break;
                            }

                            // **Lógica de Preemption de SRT:**
                            try { // **Bloque try-finally para asegurar liberación del semáforo**
                                semaforo.acquire();
                                if (!so.getPlanificador().getColaListos().isEmpty()) {
                                    Proceso procesoEnColaListos = so.getPlanificador().getColaListos().peek();
                                    if (proceso.getInstruccionesRestantes() > procesoEnColaListos.getInstruccionesRestantes()) {
                                        proceso.getPCB().setEstado(PCB.Estado.READY);
                                        so.agregarProceso(proceso);
                                        this.liberarProceso(); // Importante liberar el proceso ANTES de obtener el siguiente
                                        semaforo.release(); // Liberar el semáforo ANTES de retornar
                                        break; // **Salida *inmediata* del bucle debido a preemption**
                                    }
                                }
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break; // Salir si el thread es interrumpido durante acquire()
                            } finally {
                                semaforo.release(); // Asegurar que el semáforo se libere siempre
                            }

                            // **No hacer 'continue' aquí dentro del 'if' de preemption. Si no hubo preemption, el bucle sigue normalmente.**
                        }
                        break;

                    default:
                        ejecutarProceso(proceso);
                        break;
                }
                procesoEnEjecucion = null;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void ejecutarProceso(Proceso proceso) throws InterruptedException {
        while (!proceso.estaTerminado() && !proceso.debeBloquearse()) {
            actualizarEstado("Ejecutando: Programa de Usuario");
            proceso.ejecutarInstruccion();
            Thread.sleep(so.getDuracionCiclo());
            // **Incrementar tiempoGlobal en cada ciclo de CPU para políticas no RR, HRRN, SRT, SFJ**
            incrementarTiempoGlobalSimulacion();
        }
        if (proceso.estaTerminado()) {
            so.moverAColaTerminados(proceso);
        } else if (proceso.debeBloquearse()) {
            so.moverAColaBloqueados(proceso);
        } else {
            so.agregarProceso(proceso);
        }
    }

    private void liberarProceso() {
        procesoEnEjecucion = null;
        actualizarEstado("Ejecutando: Sistema Operativo");
    }

    private void actualizarEstado(String mensaje) {
        SwingUtilities.invokeLater(() -> labelEstado.setText(mensaje));
    }

    // **Método para incrementar el tiempo global correctamente en la CPU**
    private void incrementarTiempoGlobalSimulacion() {
        Planificador planificador = so.getPlanificador();
        if (planificador instanceof HRRN) { // Si el planificador es HRRN, incrementa su tiempo global.
            HRRN hrrnPlanificador = (HRRN) planificador;
            hrrnPlanificador.incrementarTiempoGlobal();
        }
        // Si tuvieras otros planificadores que necesitan tiempo global, podrías gestionarlo aquí.
        // O, mejor aún, gestiona tiempoGlobal de forma centralizada en SistemaOperativo si es posible.
    }
}
