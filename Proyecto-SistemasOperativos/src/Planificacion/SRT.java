package planificacion;

import Clases.Proceso;
import Estructuras.Queue; // Asegúrate de importar tu clase Queue

public class SRT extends Planificador {

    @Override
    public Proceso siguienteProceso() {
        try {
            mutex.acquire();
            if (colaListos.isEmpty()) {
                return null;
            }

            Proceso mejorProceso = null;
            Queue<Proceso> colaAuxiliarSRT = new Queue<>(); // Cola auxiliar temporal para SRT

            // Inicializar mejorProceso con el primer proceso de la cola y removerlo de colaListos
            if (!colaListos.isEmpty()) {
                mejorProceso = colaListos.dequeue();
            } else {
                return null; // La cola estaba vacía al inicio, no hay proceso.
            }

            // Iterar sobre los procesos restantes en la cola de listos:
            int lengthColaListos = colaListos.getLength(); // Obtener la longitud ANTES de empezar a dequeue
            for (int i = 0; i < lengthColaListos; i++) {
                Proceso procesoActual = colaListos.dequeue(); // Sacar el proceso del frente

                // Comparar el tiempo restante con el mejorProceso actual
                if (procesoActual.getInstruccionesRestantes() < mejorProceso.getInstruccionesRestantes()) {
                    colaAuxiliarSRT.enqueue(mejorProceso); // El anterior mejorProceso ahora va a la cola auxiliar
                    mejorProceso = procesoActual; // El proceso actual es ahora el mejor (más corto restante)
                } else {
                    colaAuxiliarSRT.enqueue(procesoActual); // El proceso actual no es más corto, va a la cola auxiliar
                }
            }

            // Re-encolar todos los procesos de la cola auxiliar DE VUELTA a la cola de listos,
            // EXCEPTO el mejorProceso (que es el que se va a retornar para ejecutar).
            int lengthColaAuxiliar = colaAuxiliarSRT.getLength();
            for (int i = 0; i < lengthColaAuxiliar; i++) {
                colaListos.enqueue(colaAuxiliarSRT.dequeue());
            }

            return mejorProceso; // Retorna el proceso con el tiempo de ráfaga restante más corto

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
