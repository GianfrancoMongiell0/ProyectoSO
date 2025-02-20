package planificacion;

import Clases.Proceso;
import Estructuras.Lista;
import Estructuras.Queue;
import java.util.concurrent.Semaphore;

/**
 * Clase abstracta que define la estructura común para todos los planificadores.
 */
public abstract class Planificador {

    protected Queue<Proceso> colaListos = new Queue<>();
    protected Lista<Proceso> listaListos = new Lista<>();
    protected Semaphore mutex = new Semaphore(1);
    protected int quantum;

    // Métodos getters y setters
    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public int getQuantum() {
        return quantum;
    }

    public Semaphore getMutex() {
        return mutex;
    }

    public void setMutex(Semaphore mutex) {
        this.mutex = mutex;
    }

    public Queue<Proceso> getColaListos() {
        return colaListos;
    }

    public Lista<Proceso> getListaListos() {
        return listaListos;
    }

    public void setColaListos(Queue<Proceso> colaListos) {
        this.colaListos = colaListos;
    }

    public void setListaListos(Lista<Proceso> listaListos) {
        this.listaListos = listaListos;
    }

    // Métodos abstractos
    public abstract Proceso siguienteProceso();

    public abstract void agregarProceso(Proceso p);

    public abstract boolean estaVacio();
}
