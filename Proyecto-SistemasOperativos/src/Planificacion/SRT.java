package planificacion;

import Clases.Proceso;
import Estructuras.Queue;

public class SRT extends Planificador {

    public SRT() {
        this.quantum = -1; // SRT no usa quantum, se deja en -1 o valor no aplicable
    }

    @Override
    public Proceso siguienteProceso() {
        try {
            mutex.acquire();
            if (colaListos.isEmpty()) {
                return null; // No hay procesos en la cola de listos
            }

            Proceso mejorProcesoSRT = null;
            Queue<Proceso> colaAuxiliarSRT = new Queue<>();

            // Inicializar mejorProcesoSRT con el primer proceso de la cola
            if (!colaListos.isEmpty()) {
                mejorProcesoSRT = colaListos.dequeue();
            } else {
                return null; // La cola estaba vacía
            }

            int lengthColaListos = colaListos.getLength();
            for (int i = 0; i < lengthColaListos; i++) {
                Proceso procesoActual = colaListos.dequeue();

                // Comparar por tiempo restante (SRT - Shortest Remaining Time)
                if (procesoActual.getInstruccionesRestantes() < mejorProcesoSRT.getInstruccionesRestantes()) {
                    colaAuxiliarSRT.enqueue(mejorProcesoSRT); // Re-encolar el anterior 'mejor' proceso
                    mejorProcesoSRT = procesoActual; // El actual es ahora el mejor (menor tiempo restante)
                } else {
                    colaAuxiliarSRT.enqueue(procesoActual); // Re-encolar el proceso actual
                }
            }

            // Re-encolar todos los procesos de la cola auxiliar DE VUELTA a la cola de listos
            int lengthColaAuxiliar = colaAuxiliarSRT.getLength(); // Recalcular longitud por seguridad
            for (int i = 0; i < lengthColaAuxiliar; i++) {
                colaListos.enqueue(colaAuxiliarSRT.dequeue());
            }

            return mejorProcesoSRT; // Retornar el proceso con el menor tiempo restante

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            mutex.release(); // Liberar el mutex
        }
    }

    @Override
    public void agregarProceso(Proceso p) {
        try {
            mutex.acquire();
            colaListos.enqueue(p); // Añadir proceso a la cola de listos
            reordenarCola(); // **IMPORTANTE para SRT: Reordenar la cola después de añadir un nuevo proceso (para preemption y visualización)**
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release(); // Liberar el mutex
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
            Queue<Proceso> colaTemporalSRT = new Queue<>();

            while (!colaListos.isEmpty()) {
                Proceso p = colaListos.dequeue();
                boolean insertado = false;

                while (!colaTemporalSRT.isEmpty()) {
                    Proceso siguiente = colaTemporalSRT.dequeue();
                    if (p.getInstruccionesRestantes() < siguiente.getInstruccionesRestantes()) {
                        colaTemporalSRT.enqueue(p);
                        colaTemporalSRT.enqueue(siguiente);
                        insertado = true;
                        break;
                    }
                    colaTemporalSRT.enqueue(siguiente);
                }
                if (!insertado) {
                    colaTemporalSRT.enqueue(p);
                }
            }
            while (!colaTemporalSRT.isEmpty()) {
                colaListos.enqueue(colaTemporalSRT.dequeue());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
        if (simulador != null) {
            simulador.actualizarTablas(); // Actualizar la GUI después de reordenar
        }
    }
}
