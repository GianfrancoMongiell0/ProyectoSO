package planificacion;

import Clases.Proceso;
import Estructuras.Queue; 

public class SJF extends Planificador {

    @Override
    public Proceso siguienteProceso() {
        try {
            mutex.acquire();
            if (colaListos.isEmpty()) {
                return null;
            }

            Proceso mejorProceso = null;
            Queue<Proceso> colaAuxiliarSJF = new Queue<>(); // Cola auxiliar temporal para SJF

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
                    colaAuxiliarSJF.enqueue(mejorProceso); // El anterior mejorProceso ahora va a la cola auxiliar
                    mejorProceso = procesoActual; // El proceso actual es ahora el mejor (más corto)
                } else {
                    colaAuxiliarSJF.enqueue(procesoActual); // El proceso actual no es más corto, va a la cola auxiliar
                }
            }

            // Re-encolar todos los procesos de la cola auxiliar DE VUELTA a la cola de listos,
            // EXCEPTO el mejorProceso (que es el que se va a retornar para ejecutar).
            int lengthColaAuxiliar = colaAuxiliarSJF.getLength();
            for (int i = 0; i < lengthColaAuxiliar; i++) {
                colaListos.enqueue(colaAuxiliarSJF.dequeue());
            }

            return mejorProceso; // Retorna el proceso con el tiempo de ráfaga más corto

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
    
    @Override
    public void reordenarCola() {
        try {
            mutex.acquire();
            Queue<Proceso> colaTemporal = new Queue<>();

            // Reorganizar la cola según el tiempo de servicio
            while (!colaListos.isEmpty()) {
                Proceso p = colaListos.dequeue();
                boolean insertado = false;

                // Insertar en la cola temporal en el lugar adecuado
                while (!colaTemporal.isEmpty()) {
                    Proceso siguiente = colaTemporal.dequeue();
                    if (p.getTotalInstrucciones() < siguiente.getTotalInstrucciones()) {
                        colaTemporal.enqueue(p); // Insertar el proceso actual
                        colaTemporal.enqueue(siguiente); // Encolar el siguiente
                        insertado = true;
                        break;
                    }
                    colaTemporal.enqueue(siguiente); // Reencolar el siguiente
                }

                // Si no se insertó, agregar al final
                if (!insertado) {
                    colaTemporal.enqueue(p);
                }
            }

            // Volver a encolar los procesos en la cola original
            while (!colaTemporal.isEmpty()) {
                colaListos.enqueue(colaTemporal.dequeue());
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
        simulador.actualizarTablas();
    }
}
