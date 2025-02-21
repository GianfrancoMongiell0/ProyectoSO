package planificacion;

import Clases.Proceso;
import Estructuras.Queue;
import Interfaces.Simulador;
import java.util.concurrent.Semaphore;

public abstract class Planificador {
    protected Queue<Proceso> colaListos = new Queue<>();
    protected Semaphore mutex = new Semaphore(1);
    protected int quantum;
    protected Simulador simulador;

    public void setColaListos(Queue<Proceso> colaListos) {
        this.colaListos = colaListos;
    }

    public abstract Proceso siguienteProceso();
    public abstract void agregarProceso(Proceso p);
    public abstract boolean estaVacio();

    public int getQuantum() {
        return quantum;
    }
       
    public Queue<Proceso> getColaListos() {
        return colaListos;
    }
   
    public void reordenarCola() {
     
    }
}
