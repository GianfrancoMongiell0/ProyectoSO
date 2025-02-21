package Clases;

import planificacion.HRRN; 
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import planificacion.Planificador;

public class CPU extends Thread {

    private int id;
    private SistemaOperativo so;
    private Proceso procesoEnEjecucion;
    private JLabel labelEstado;
    private int quantumRestante;
    private java.util.concurrent.Semaphore semaforo; // Semáforo para control de preemption en SRT

    public CPU(int id, SistemaOperativo so, JLabel labelEstado) {
        this.id = id;
        this.so = so;
        this.labelEstado = labelEstado;
        this.procesoEnEjecucion = null;
        this.quantumRestante = 0; // Inicializar quantumRestante
        this.semaforo = new java.util.concurrent.Semaphore(1); // Inicializar semáforo
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
                    Thread.sleep(100); // Espera no activa si no hay procesos listos
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
                            incrementarTiempoGlobalSimulacion(); // Incremento de tiempo global para Round Robin

                            if (proceso.debeBloquearse()) {
                                procesoABloqueado(proceso);
                                break;
                            }
                            if (proceso.estaTerminado()) {
                                procesoATerminado(proceso);
                                break;
                            }
                            quantumRestante--;
                            if (quantumRestante <= 0) {
                                procesoListoParaReencolar(proceso);
                                break;
                            }
                        }
                        break;

                    case "HRRN":
                        while (proceso.getInstruccionesRestantes() > 0) {
                            Thread.sleep(so.getDuracionCiclo());
                            proceso.ejecutarInstruccion();
                            incrementarTiempoGlobalSimulacion(); // Incremento de tiempo global para HRRN

                            if (proceso.debeBloquearse()) {
                                procesoABloqueado(proceso);
                                break;
                            }
                            if (proceso.estaTerminado()) {
                                procesoATerminado(proceso);
                                break;
                            }
                        }
                        break;

                    case "SJF": // SFJ - No preemptivo
                        while (proceso.getInstruccionesRestantes() > 0) {
                            Thread.sleep(so.getDuracionCiclo());
                            proceso.ejecutarInstruccion();
                            incrementarTiempoGlobalSimulacion(); // Incremento de tiempo global para SJF

                            if (proceso.debeBloquearse()) {
                                procesoABloqueado(proceso);
                                break;
                            }
                            if (proceso.estaTerminado()) {
                                procesoATerminado(proceso);
                                break;
                            }
                        }
                        break;

                    case "SRT": // SRT - Preemptivo
                        while (proceso.getInstruccionesRestantes() > 0) {
                            try {
                                Thread.sleep(so.getDuracionCiclo());
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }

                            proceso.ejecutarInstruccion();
                            incrementarTiempoGlobalSimulacion(); // Incremento de tiempo global para SRT

                            // Lógica de Preemption SRT:
                            try {
                                semaforo.acquire();
                                if (!so.getPlanificador().getColaListos().isEmpty()) {
                                    Proceso procesoEnColaListos = so.getPlanificador().getColaListos().peek();
                                    if (procesoEnColaListos != null && proceso.getInstruccionesRestantes() > procesoEnColaListos.getInstruccionesRestantes()) {
                                        System.out.println("CPU " + id + ": Preemption! Proceso " + proceso.getNombre() + " preempted por " + procesoEnColaListos.getNombre()); // Mensaje de preemption
                                        procesoListoParaReencolar(proceso);
                                        semaforo.release();
                                        break; // Salida inmediata del bucle por preemption
                                    }
                                }
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            } finally {
                                semaforo.release();
                            }

                            if (proceso.debeBloquearse()) {
                                procesoABloqueado(proceso);
                                break;
                            }
                            if (proceso.estaTerminado()) {
                                procesoATerminado(proceso);
                                break;
                            }
                            // No 'continue' aquí dentro del 'if' de preemption. Si no hubo preemption, el bucle sigue normal.
                        }
                        break;

                    default: // FCFS y otras políticas no especificadas explícitamente
                        ejecutarProceso(proceso); // Lógica por defecto para FCFS (no preemptivo)
                        break;
                }
                liberarProceso(); // CPU libre después de ejecutar el proceso (o ser preemptado)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("CPU " + id + " ha terminado."); // Mensaje al terminar el hilo de CPU
    }

    private void ejecutarProceso(Proceso proceso) throws InterruptedException { // Para políticas como FCFS
        while (!proceso.estaTerminado() && !proceso.debeBloquearse()) {
            actualizarEstado("Ejecutando: Programa de Usuario");
            proceso.ejecutarInstruccion();
            Thread.sleep(so.getDuracionCiclo());
            incrementarTiempoGlobalSimulacion(); // Incremento de tiempo global para FCFS
        }
        if (proceso.estaTerminado()) {
            procesoATerminado(proceso);
        } else if (proceso.debeBloquearse()) {
            procesoABloqueado(proceso);
        } else {
            procesoListoParaReencolar(proceso); // Reencolar si no terminó ni se bloqueó (FCFS no preemptivo)
        }
    }


    private void procesoListoParaReencolar(Proceso proceso) {
        proceso.getPCB().setEstado(PCB.Estado.READY);
        so.agregarProceso(proceso);
        liberarProceso();
    }

    private void procesoATerminado(Proceso proceso) {
        proceso.getPCB().setEstado(PCB.Estado.TERMINATED);
        so.moverAColaTerminados(proceso);
        liberarProceso();
    }

    private void procesoABloqueado(Proceso proceso) {
        proceso.getPCB().setEstado(PCB.Estado.BLOCKED);
        so.moverAColaBloqueados(proceso);
        liberarProceso();
    }


    private void liberarProceso() {
        procesoEnEjecucion = null;
        actualizarEstado("Ejecutando: Sistema Operativo");
    }

    private void actualizarEstado(String mensaje) {
        SwingUtilities.invokeLater(() -> labelEstado.setText(mensaje));
    }

    private void incrementarTiempoGlobalSimulacion() {
        Planificador planificador = so.getPlanificador();
        if (planificador instanceof HRRN) {
            HRRN hrrnPlanificador = (HRRN) planificador;
            hrrnPlanificador.incrementarTiempoGlobal();
        }
        
    }
}