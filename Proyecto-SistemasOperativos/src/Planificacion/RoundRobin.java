package planificacion;

import Clases.Proceso;
import Estructuras.Queue;
import java.util.concurrent.Semaphore;

public class RoundRobin extends Planificador {

    private Queue<Proceso> colaListos = new Queue<>();
    private int quantum;

    public RoundRobin(int quantum) {
        this.quantum = quantum;
    }

    @Override
    public Proceso siguienteProceso() {
        try {
            mutex.acquire();
            return colaListos.isEmpty() ? null : colaListos.dequeue();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            mutex.release();
        }
    }

    @Override
    public void agregarProceso(Proceso p) {
        try {
            mutex.acquire();
            colaListos.enqueue(p);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
    }

    @Override
    public boolean estaVacio() {
        return colaListos.isEmpty();
    }
}
