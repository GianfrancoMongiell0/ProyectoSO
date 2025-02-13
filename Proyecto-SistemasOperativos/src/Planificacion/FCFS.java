package planificacion;

import Clases.Proceso;
import Estructuras.Queue;
import java.util.concurrent.Semaphore;

public class FCFS extends Planificador {

    protected Queue<Proceso> colaListos = new Queue<>();
    protected Semaphore mutex = new Semaphore(1);

    public boolean estaVacio() {
        return colaListos.isEmpty();
    }

    @Override
    public Proceso siguienteProceso() {
        try {
            mutex.acquire();
            return colaListos.dequeue();
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

}
