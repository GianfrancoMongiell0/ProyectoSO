package Clases;

import Clases.PCB.Estado;
import Estructuras.Lista;
import Estructuras.Queue;
import Interfaces.Simulador;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import planificacion.Planificador;

public class SistemaOperativo {

    private Planificador planificador;
    private Queue<Proceso> colaBloqueados;
    private Queue<Proceso> colaTerminados;
    private final Lista<CPU> cpus;
    private volatile boolean enEjecucion; // volatile para visibilidad entre hilos
    private int duracionCiclo = 1000;
    private Simulador simulador;
    private JLabel[] labelsEstadoCPUs;

    public SistemaOperativo(Planificador planificador, int numCPUs, Queue<Proceso> colaListos, JLabel[] labelsEstadoCPUs) {
        this.planificador = planificador;
        this.colaBloqueados = new Queue<>(); // **INICIALIZACIÓN CORRECTA - ¡DESCOMENTADO!**
        this.colaTerminados = new Queue<>(); // **INICIALIZACIÓN CORRECTA - ¡DESCOMENTADO!**
        this.cpus = new Lista<>();
        this.enEjecucion = true;
        this.labelsEstadoCPUs = labelsEstadoCPUs;

        // Pasar la cola de listos al planificador
        this.planificador.setColaListos(colaListos);

        for (int i = 0; i < numCPUs; i++) {
            cpus.insertLast(new CPU(i + 1, this, labelsEstadoCPUs[i]));
        }
    }

    // Métodos sincronizados para acceso seguro multihilo
    public synchronized void setColaTerminados(Queue<Proceso> colaTerminados) {
        this.colaTerminados = colaTerminados;
        actualizarGUI(); // Refrescar interfaz
    }

    public synchronized void setColaBloqueados(Queue<Proceso> colaBloqueados) {
        this.colaBloqueados = colaBloqueados;
        actualizarGUI(); // Refrescar interfaz
    }

    public synchronized void agregarProceso(Proceso proceso) {
        System.out.println("SO: Intentando agregar proceso " + proceso.getNombre()); // Usar proceso.getNombre() si está disponible

        if (!proceso.estaTerminado()) {
            planificador.agregarProceso(proceso);
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
            // System.out.println("SO: No hay procesos en la cola."); // Menos verboso si es normal que no haya procesos
        } else {
            System.out.println("SO: Asignando proceso " + p.getNombre()); // Usar proceso.getNombre() si está disponible
            actualizarGUI();
        }
        return p;
    }

    public synchronized void moverAColaBloqueados(Proceso proceso) {
        System.out.println("SO: Proceso " + proceso.getNombre() + " movido a Bloqueados"); // Usar proceso.getNombre() si está disponible
        proceso.getPCB().setEstado(PCB.Estado.BLOCKED);
        colaBloqueados.enqueue(proceso);
        if (simulador != null) {
            simulador.setColaBloqueados(colaBloqueados);
            simulador.actualizarTablas();
        }
        new Thread(() -> manejarDesbloqueo(proceso)).start(); // Hilo separado para manejar el desbloqueo
    }

    public synchronized void moverAColaTerminados(Proceso proceso) {
        System.out.println("SO: Proceso " + proceso.getNombre() + " movido a Terminados"); // Usar proceso.getNombre() si está disponible
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
                if (!colaBloqueados.isEmpty()) { // Seguridad adicional: verificar que la cola no esté vacía antes de dequeue
                    colaBloqueados.dequeue(); // Eliminar de bloqueados
                    p.getPCB().reiniciarContadorBloqueo();
                    if (!p.estaTerminado()) { // Verificar nuevamente si el proceso no ha terminado (por si acaso)
                        p.getPCB().setEsIOBound(false);
                        p.getPCB().setEstado(PCB.Estado.READY);
                        agregarProceso(p); // Volver a la cola de listos
                    }
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

    public Planificador getPlanificador() {
        return planificador;
    }

    public void setPlanificador(Planificador planificador) {
        this.planificador = planificador;
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
        CPU cpu = cpus.get(idCPU - 1); // Ajustar el índice a 0-based
        Proceso proceso = cpu.obtenerProcesoEnEjecucion();

        if (proceso != null && proceso.getPCB().getEstado() == Estado.TERMINATED) {
            return null; // No retornar procesos terminados
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
        return null; // No hay proceso en ejecución en ninguna CPU
    }

    private int ciclosRelojGlobal = 0;

    public void incrementarCiclosReloj() {
        ciclosRelojGlobal++;

        if (simulador != null) {
            SwingUtilities.invokeLater(() -> {
                simulador.valorCicloReloj.setText("Ciclos de Reloj Global: " + ciclosRelojGlobal);
                System.out.println("Ciclo de reloj" + ciclosRelojGlobal);
            });
        }
    }

    public void ejecutarSimulacion() {
        while (enEjecucion) {
            if (debeDetenerSimulacion()) {
                System.out.println("Simulación detenida: No hay más procesos en ejecución.");
                enEjecucion = false;
                break;
            }

            incrementarCiclosReloj();

            if (simulador != null) {
                for (int i = 0; i < cpus.getLength(); i++) {
                    CPU cpu = cpus.get(i);
                    Proceso proceso = cpu.obtenerProcesoEnEjecucion();

                    if (proceso == null || proceso.getPCB().getEstado() == Estado.TERMINATED) {
                        simulador.limpiarEstadoCPU(i + 1); // Limpiar estado en la GUI si no hay proceso o está terminado
                    } else {
                        simulador.actualizarEstadoCPU(i + 1, proceso); // Actualizar estado de la CPU en la GUI
                    }
                }
            } else {
                System.out.println("Error: Simulador no inicializado en SistemaOperativo.");
            }

            try {
                Thread.sleep(duracionCiclo); // Pausa para simular el ciclo de reloj
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private boolean debeDetenerSimulacion() {
        return planificador.getColaListos().isEmpty()
                && colaBloqueados.isEmpty()
                && obtenerProcesoEnEjecucion() == null; // Simulación termina si no hay procesos pendientes
    }
}
