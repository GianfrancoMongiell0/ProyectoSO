package Clases;

import Clases.PCB.Estado;
import Estructuras.Lista;
import Estructuras.Nodo;
import Estructuras.Queue;
import Interfaces.Simulador;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import planificacion.Planificador;

public class SistemaOperativo {

    private final Planificador planificador; // Planificador que gestiona la cola de procesos listos
    private Queue<Proceso> colaBloqueados; // Cola de procesos bloqueados
    private Queue<Proceso> colaTerminados; // Cola de procesos terminados
    private final Lista<CPU> cpus; // Lista dde CPUs disponibles
    private volatile boolean enEjecucion; // Bandera para cpntrolar la ejecucion del sistema
    private int duracionCiclo = 1000; // Duracion de un ciclo de reloj en ms
    private Simulador simulador; // Referecia al simulador (GUI)

    public SistemaOperativo(Planificador planificador, int numCPUs, Queue<Proceso> colaListos, JLabel[] labelsEstadoCPUs) {
        this.planificador = planificador;
        //this.colaBloqueados = new Queue<>();
        //this.colaTerminados = new Queue<>();
        this.cpus = new Lista<>();
        this.enEjecucion = true;

        // Pasar la cola de listos al planificador
        this.planificador.setColaListos(colaListos); // Asigna la cola de listos al planificador 

        for (int i = 0; i < numCPUs; i++) {
            cpus.insertLast(new CPU(this, i + 1, labelsEstadoCPUs[i])); //Crea las CPUs
        }
    }

    public synchronized void setColaTerminados(Queue<Proceso> colaTerminados) {
        this.colaTerminados = colaTerminados;
        actualizarGUI(); // Refrescar interfaz
    }

    public synchronized void setColaBloqueados(Queue<Proceso> colaBloqueados) {
        this.colaBloqueados = colaBloqueados;
        actualizarGUI(); // Refrescar interfaz
    }

    // Métodos sincronizados
    public synchronized void agregarProceso(Proceso proceso) {
        System.out.println("SO: Intentando agregar proceso " + proceso.getPCB().getNombre());

        if (!proceso.estaTerminado()) {
            planificador.agregarProceso(proceso); // Agrega el proceso a la cola de listos
            System.out.println("SO: Proceso agregado correctamente a la cola. Estado actual de la cola:");
            if (simulador != null) {
                simulador.setColaListos(planificador.getColaListos());
                simulador.actualizarTablas();
            }
        } else {
            System.out.println("SO: Proceso rechazado porque está terminado o bloqueado.");
        }
    }

    public synchronized Proceso obtenerSiguienteProceso() {
        Proceso p = planificador.siguienteProceso();
        if (p == null) {
            //  System.out.println("SO: No hay procesos en la cola.");
        } else {
            System.out.println("SO: Asignando proceso " + p.getPCB().getNombre());
            actualizarGUI();
        }
        return p;
    }

    public synchronized void moverAColaBloqueados(Proceso proceso) {
        System.out.println("SO: Proceso " + proceso.getPCB().getNombre() + " movido a Bloqueados");
        proceso.getPCB().setEstado(PCB.Estado.BLOCKED);
        colaBloqueados.enqueue(proceso);
        if (simulador != null) {
            simulador.setColaBloqueados(colaBloqueados);
            simulador.actualizarTablas();
        }
        new Thread(() -> manejarDesbloqueo(proceso)).start(); // Hilo para desbloquear
    }

    public synchronized void moverAColaTerminados(Proceso proceso) {
        System.out.println("SO: Proceso " + proceso.getPCB().getNombre() + " movido a Terminados");
        proceso.getPCB().setEstado(PCB.Estado.TERMINATED);
        colaTerminados.enqueue(proceso);
        if (simulador != null) {
            simulador.setColaTerminados(colaTerminados);
            simulador.actualizarTablas();
        }
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
                // Solo agregar el proceso si no ha terminado
                if (!p.estaTerminado()) {
                    p.getPCB().setEsIOBound(false);
                    agregarProceso(p); // Volver a la cola de listos
                }
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

    public Simulador getSimulador() {
        return simulador;
    }

    public void setSimulador(Simulador simulador) {
        this.simulador = simulador;
    }

    public Lista<CPU> getCpus() {
        return cpus;
    }

    private void actualizarGUI() {
        if (simulador != null) {
            System.out.println("Actualizando GUI...");
            SwingUtilities.invokeLater(() -> {
                simulador.actualizarTablas();
                System.out.println("Tablas actualizadas.");
            });
        }
    }

    public synchronized Proceso obtenerProcesoEnEjecucion(int idCPU) {
        if (idCPU < 1 || idCPU > cpus.getLength()) {
            return null;
        }
        CPU cpu = cpus.get(idCPU - 1); // Ajustar el índice, ya que los índices en la lista son 0-based
        Proceso proceso = cpu.obtenerProcesoEnEjecucion();

        // Asegurar que no devolvemos un proceso terminado
        if (proceso != null && proceso.getPCB().getEstado() == Estado.TERMINATED) {
            return null;
        }

        return proceso;
    }

    public synchronized Proceso obtenerProcesoEnEjecucion() {
        for (int i = 0; i < cpus.getLength(); i++) {
            CPU cpu = cpus.get(i);
            Proceso procesoEnEjecucion = cpu.obtenerProcesoEnEjecucion();
            if (procesoEnEjecucion != null) {
                return procesoEnEjecucion;
            }
        }
        return null; // Si no hay proceso en ejecución
    }

    private int ciclosRelojGlobal = 0;

    public void incrementarCiclosReloj() {
        ciclosRelojGlobal++;

        // Utilizar SwingUtilities.invokeLater para actualizar los componentes en el hilo de eventos
        if (simulador != null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    // Actualiza el JLabel de ciclos de reloj en el simulador
                    simulador.valorCicloReloj.setText("Ciclos de Reloj Global: " + ciclosRelojGlobal);
                    System.out.println("Ciclo de reloj" + ciclosRelojGlobal);
                }
            });
        }
    }

    public void ejecutarSimulacion() {
        while (enEjecucion) {
            if (debeDetenerSimulacion()) {
                System.out.println("Simulación detenida: No hay más procesos en ejecución.");
                enEjecucion = false; // Detiene la ejecucion si no hay procesos
                break;
            }

            incrementarCiclosReloj(); // Incrementa el contador de ciclos de reloj

            if (simulador != null) {
                for (int i = 0; i < cpus.getLength(); i++) {
                    CPU cpu = cpus.get(i);
                    Proceso proceso = cpu.obtenerProcesoEnEjecucion(); // Obtener el proceso de la CPU directamente

                    if (proceso == null || proceso.getPCB().getEstado() == Estado.TERMINATED) {
                        // Si no hay proceso o el proceso ha terminado, limpiar CPU
                        simulador.limpiarEstadoCPU(i + 1);
                    } else {
                        // Si hay un proceso en ejecución, actualizar la interfaz
                        simulador.actualizarEstadoCPU(i + 1, proceso);
                    }
                }
            } else {
                System.out.println("Error: Simulador no inicializado en SistemaOperativo.");
            }

            try {
                Thread.sleep(duracionCiclo);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private boolean debeDetenerSimulacion() {
        return planificador.getColaListos().isEmpty()
                && colaBloqueados.isEmpty()
                && obtenerProcesoEnEjecucion() == null;
    }

    public Planificador getPlanificador() {
        return planificador;
    }

    public int getCiclosRelojGlobal() {
        return this.ciclosRelojGlobal;
    }
}
