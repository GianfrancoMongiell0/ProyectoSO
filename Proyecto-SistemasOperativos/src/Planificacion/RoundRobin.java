package planificacion;

import Clases.Proceso;
import Estructuras.Queue;
import java.util.concurrent.Semaphore;

public class RoundRobin extends Planificador {

    private int contadorQuantum = 0;

    public RoundRobin() {
        this.quantum = 5; // Quantum definido en el PDF
    }

    @Override
    public Proceso siguienteProceso() {
        try {
            mutex.acquire();
            if (colaListos.isEmpty()) {
                return null;
            }

            if (contadorQuantum >= quantum) {
                Proceso p = colaListos.dequeue();
                colaListos.enqueue(p); // Reencolar si no terminó en el quantum
                contadorQuantum = 0;
            }

            contadorQuantum++;
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

    @Override
    public boolean estaVacio() {
        return colaListos.isEmpty();
    }
}
