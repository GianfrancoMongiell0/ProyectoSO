package planificacion;

import Clases.Proceso;
import Estructuras.Queue;
import java.util.concurrent.Semaphore;

public class RoundRobin extends Planificador {

    public RoundRobin() {
        this.quantum = 5;
    }

    @Override
    public Proceso siguienteProceso() {
        try {
            mutex.acquire();
            if (colaListos.isEmpty()) {
                return null;
            }

            Proceso p = colaListos.dequeue();

            if (p.getInstruccionesRestantes() > 0) {
                colaListos.enqueue(p);
                System.out.println("[RoundRobin] Reencolando proceso: " + p.getPCB().getNombre() + " | Instrucciones restantes: " + p.getInstruccionesRestantes());
            } else {
                System.out.println("[RoundRobin] Proceso completado: " + p.getPCB().getNombre());
            }

            return p;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            mutex.release();
        }
    }

    @Override
    public boolean estaVacio() {
        return colaListos.isEmpty();
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
