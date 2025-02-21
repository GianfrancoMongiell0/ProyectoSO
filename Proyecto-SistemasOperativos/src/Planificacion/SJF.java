package planificacion;

import Clases.Proceso;
import Estructuras.Lista;
import Estructuras.Queue;
import java.util.concurrent.Semaphore;

public class SJF extends Planificador {
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