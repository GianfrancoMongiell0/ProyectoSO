package planificacion;

import Clases.Proceso;
import Estructuras.Queue;
import java.util.concurrent.Semaphore;

public class FCFS extends Planificador {
    
    private final Semaphore mutex = new Semaphore(1);

    @Override
    public Proceso siguienteProceso() {
        try {
            mutex.acquire();
            if (colaListos.isEmpty()) {
                return null;
            }
            return colaListos.dequeue(); // Obtiene y elimina el primer proceso
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
            colaListos.enqueue(p); // Agrega el proceso al final de la cola
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
