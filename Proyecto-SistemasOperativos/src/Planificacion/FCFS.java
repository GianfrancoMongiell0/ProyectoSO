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
            Proceso p = colaListos.isEmpty() ? null : colaListos.dequeue();

            if (p != null) {
                System.out.println("[FCFS] Seleccionando proceso: " + p.getPCB().getNombre() + " (Instrucciones: " + p.getTotalInstrucciones() + ")");
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
    public void agregarProceso(Proceso p) {
        try {
            mutex.acquire();
            colaListos.enqueue(p);
            System.out.println("[FCFS] Nuevo proceso en cola: " + p.getPCB().getNombre() + " | Tiempo llegada: " + p.getPCB().getTiempoLlegada());

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
