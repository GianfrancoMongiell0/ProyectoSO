package planificacion;

import Clases.Proceso;
import Estructuras.Lista;
import java.util.concurrent.Semaphore;

public class FCFS extends Planificador {

    @Override
    public Proceso siguienteProceso() {
        try {
            mutex.acquire();
            if (colaListos.isEmpty()) {
                return null;
            }

            // Obtener y eliminar el primer proceso
            Proceso p = colaListos.get(0);
            colaListos.deleteFirst();
            return p;

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
            colaListos.insertLast(p);
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
