package planificacion;

import Clases.Proceso;
import Estructuras.Lista;
import java.util.concurrent.Semaphore;

public class SRT extends Planificador {
    @Override
    public Proceso siguienteProceso() {
        try {
            mutex.acquire();
            if (colaListos.isEmpty()) {
                return null;
            }

            Proceso mejorProceso = colaListos.dequeue();
            int length = colaListos.getLength();
            for (int i = 0; i < length; i++) {
                Proceso p = colaListos.dequeue();
                if (p.getInstruccionesRestantes() < mejorProceso.getInstruccionesRestantes()) {
                    colaListos.enqueue(mejorProceso);
                    mejorProceso = p;
                } else {
                    colaListos.enqueue(p);
                }
            }
            return mejorProceso;

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
