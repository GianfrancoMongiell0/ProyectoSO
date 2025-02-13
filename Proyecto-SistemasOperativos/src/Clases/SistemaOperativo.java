package Clases;

import Estructuras.Lista;
import Estructuras.Queue;
import planificacion.Planificador;

public class SistemaOperativo {

    private final Planificador planificador;
    private final Queue<Proceso> colaBloqueados;
    private final Queue<Proceso> colaTerminados;
    private final Lista<CPU> cpus;
    private volatile boolean enEjecucion; // volatile para visibilidad entre hilos
    private int duracionCiclo = 1000;

    public SistemaOperativo(Planificador planificador, int numCPUs) {
        this.planificador = planificador;
        this.colaBloqueados = new Queue<>();
        this.colaTerminados = new Queue<>();
        this.cpus = new Lista<>();
        this.enEjecucion = true;

        for (int i = 0; i < numCPUs; i++) {
            cpus.insertLast(new CPU(this, i + 1));
        }
    }

    // Métodos sincronizados
    public synchronized void agregarProceso(Proceso proceso) {
        if (!proceso.estaTerminado() && !proceso.debeBloquearse()) {
            planificador.agregarProceso(proceso);
        }
    }

    public synchronized Proceso obtenerSiguienteProceso() {
        return planificador.siguienteProceso();
    }

    public synchronized void moverAColaBloqueados(Proceso proceso) {
        proceso.getPCB().setEstado(PCB.Estado.BLOCKED);
        colaBloqueados.enqueue(proceso);
        new Thread(() -> manejarDesbloqueo(proceso)).start(); // Hilo para desbloquear
    }

    public synchronized void moverAColaTerminados(Proceso proceso) {
        proceso.getPCB().setEstado(PCB.Estado.TERMINATED);
        colaTerminados.enqueue(proceso);
    }

    public void iniciarCPUs() {
        for (int i = 0; i < cpus.getLength(); i++) {
            cpus.get(i).start();
        }
    }

    public void detenerCPU() {
        enEjecucion = false;
    }

    private void manejarDesbloqueo(Proceso p) {
        try {
            Thread.sleep(p.getPCB().getCiclosCompletarExcepcion() * duracionCiclo);
            synchronized (this) {
                colaBloqueados.dequeue(); // Eliminar de bloqueados
                p.getPCB().reiniciarContadorBloqueo();
                agregarProceso(p); // Reinsertar en listos
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Getters y Setters
    public boolean isEnEjecucion() {
        return enEjecucion;
    }

    public int getDuracionCiclo() {
        return duracionCiclo;
    }

    public void setDuracionCiclo(int ms) {
        this.duracionCiclo = ms;
    }
}
