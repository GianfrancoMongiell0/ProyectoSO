package planificacion;

import Clases.Proceso;
import Estructuras.Lista;
import java.util.concurrent.Semaphore;

public class HRRN extends Planificador {

    private int tiempoGlobal = 0;

    @Override
    public Proceso siguienteProceso() {
        try {
            mutex.acquire();
            if (colaListos.isEmpty()) {
                return null;
            }

            double maxRatio = -1;
            Proceso seleccionado = null;
            for (int i = 0; i < colaListos.getLength(); i++) {
                Proceso p = colaListos.dequeue();
                double ratio = calcularResponseRatio(p);
                if (ratio > maxRatio) {
                    maxRatio = ratio;
                    seleccionado = p;
                }
                colaListos.enqueue(p);
            }

            colaListos.dequeue();
            tiempoGlobal += seleccionado.getInstruccionesRestantes();
            return seleccionado;


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
            p.getPCB().setTiempoLlegada(tiempoGlobal);
            colaListos.enqueue(p);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
    }

    private double calcularResponseRatio(Proceso p) {
        int tiempoEspera = tiempoGlobal - p.getPCB().getTiempoLlegada();
        int tiempoServicio = p.getTotalInstrucciones();
        return (tiempoEspera + tiempoServicio) / (double) tiempoServicio;
    }

    @Override
    public boolean estaVacio() {
        return colaListos.isEmpty();
    }
}
