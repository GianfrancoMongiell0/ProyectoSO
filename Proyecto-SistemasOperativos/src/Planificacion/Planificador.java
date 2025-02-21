package planificacion;

import Clases.Proceso;
import Estructuras.Queue;
import Interfaces.Simulador;
import java.util.concurrent.Semaphore;

public abstract class Planificador {

    protected int tiempoGlobal;
    protected Queue<Proceso> colaListos = new Queue<>();
    protected Semaphore mutex = new Semaphore(1); // Mutex para proteger la cola de listos
    protected int quantum; // Quantum para políticas como Round Robin
    protected Simulador simulador; // Referencia al simulador 

    public Planificador() {
        this.tiempoGlobal = 0; // Inicializar tiempoGlobal a 0
        this.quantum = -1; // Valor por defecto para quantum 
    }

    public void setColaListos(Queue<Proceso> colaListos) {
        this.colaListos = colaListos;
    }

    // Métodos abstractos 
    public abstract Proceso siguienteProceso();

    public abstract void agregarProceso(Proceso p);

    public abstract boolean estaVacio();

    // Métodos comunes (getters y setters)
    public int getQuantum() {
        return quantum;
    }

    public Queue<Proceso> getColaListos() {
        return colaListos;
    }

    // Gestión del tiempo global 
    public int getTiempoGlobal() {
        return tiempoGlobal;
    }

    public void setTiempoGlobal(int tiempoGlobal) {
        this.tiempoGlobal = tiempoGlobal;
    }

    public void incrementarTiempoGlobal() {
        tiempoGlobal++;
    }

    // Método para reordenar la cola 
    public void reordenarCola() {
    }
}
