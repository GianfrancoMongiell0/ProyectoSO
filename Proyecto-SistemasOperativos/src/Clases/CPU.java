package Clases;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class CPU extends Thread {
    private int id;
    private SistemaOperativo so;
    private Proceso procesoEnEjecucion;
    private JLabel labelEstado;
    private int quantumRestante;

    public CPU(int id, SistemaOperativo so, JLabel labelEstado) {
        this.id = id;
        this.so = so;
        this.labelEstado = labelEstado;
        this.procesoEnEjecucion = null;
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
                          //  this.setProceso(proceso);

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
}
