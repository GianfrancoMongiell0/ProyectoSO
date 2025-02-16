package planificacion;

import Clases.Proceso;
import Estructuras.Queue;
import java.util.concurrent.Semaphore;

public abstract class Planificador {
    protected Queue<Proceso> colaListos = new Queue<>();
    protected Semaphore mutex = new Semaphore(1);
    protected int quantum;

    public void setColaListos(Queue<Proceso> colaListos) {
        this.colaListos = colaListos;
    }

    public abstract Proceso siguienteProceso();
    public abstract void agregarProceso(Proceso p);
    public abstract boolean estaVacio();

    public Queue<Proceso> getColaListos() {
        return colaListos;
    }
    
}
