package planificacion;

import Clases.Proceso;
import Estructuras.Queue; // Importamos tu clase Queue correctamente
import java.util.concurrent.Semaphore;

public class HRRN extends Planificador {

    @Override
    public Proceso siguienteProceso() {
        try {
            mutex.acquire();
            if (colaListos.isEmpty()) {
                return null;
            }

            double maxRatio = -1;
            Proceso seleccionado = null;
            Queue<Proceso> colaAuxiliar = new Queue<>(); // Cola auxiliar temporal

            // Iterar sobre los procesos en la cola de listos:
            int lengthColaListos = colaListos.getLength(); // Obtener la longitud ANTES de empezar a dequeue
            for (int i = 0; i < lengthColaListos; i++) {
                Proceso procesoActual = colaListos.dequeue(); // Sacar el proceso del frente
                double ratio = calcularResponseRatio(procesoActual);

                if (ratio > maxRatio) {
                    maxRatio = ratio;
                    seleccionado = procesoActual; // Guardar el proceso con el ratio máximo
                }
                colaAuxiliar.enqueue(procesoActual); // Encolar en la cola auxiliar (para re-encolar después)
            }

            // Re-encolar todos los procesos (excepto el seleccionado) de la cola auxiliar de vuelta a la cola de listos,
            // manteniendo el orden original relativo de los procesos no seleccionados.
            lengthColaListos = colaAuxiliar.getLength(); // Obtener la longitud de la cola auxiliar
            for (int i = 0; i < lengthColaListos; i++) {
                Proceso procesoAux = colaAuxiliar.dequeue();
                if (procesoAux != seleccionado) { // No re-encolar el proceso seleccionado (ya se va a ejecutar)
                    colaListos.enqueue(procesoAux);
                }
            }

            return seleccionado; // Retornar el proceso seleccionado para ejecución

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
            p.getPCB().setTiempoLlegada(getTiempoGlobal());
            colaListos.enqueue(p);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
    }

    private double calcularResponseRatio(Proceso p) {
        int tiempoEspera = getTiempoGlobal() - p.getPCB().getTiempoLlegada();
        int tiempoServicio = p.getTotalInstrucciones();
        return (tiempoEspera + tiempoServicio) / (double) tiempoServicio;
    }

    @Override
    public boolean estaVacio() {
        return colaListos.isEmpty();
    }

    private int tiempoGlobal = 0;

    public int getTiempoGlobal() {
        return tiempoGlobal;
    }

    public void setTiempoGlobal(int tiempoGlobal) {
        this.tiempoGlobal = tiempoGlobal;
    }

    public void incrementarTiempoGlobal() {
        this.tiempoGlobal++;
    }
}
