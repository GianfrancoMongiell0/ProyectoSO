package planificacion;

import Clases.Proceso;
import Estructuras.Lista;
import Estructuras.Queue;
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
    
    @Override
    public void reordenarCola() {
            Queue<Proceso> colaTemporal = new Queue<>(); // Cola temporal para reordenar

        while (!getColaListos().isEmpty()) {
            Proceso procesoMenorRestante = getColaListos().dequeue(); // Saca el primer proceso
            boolean insertado = false;

            // Buscar la posición correcta para insertar el proceso
            while (!getColaListos().isEmpty()) {
                Proceso siguienteProceso = getColaListos().dequeue();
                if (siguienteProceso.getTiempoRestante() < procesoMenorRestante.getTiempoRestante()) {
                    colaTemporal.enqueue(siguienteProceso); // Encolar el siguiente proceso primero
                } else {
                    colaTemporal.enqueue(procesoMenorRestante); // Coloca el proceso que tiene menor tiempo restante
                    procesoMenorRestante = siguienteProceso; // Actualiza el proceso menor restante
                    insertado = true;
                    break; // Salir del bucle
                }
            }

            // Si no se insertó, encolar el proceso menor restante
            if (!insertado) {
                colaTemporal.enqueue(procesoMenorRestante);
            }

            // Reencolar los procesos que se han extraído
            while (!getColaListos().isEmpty()) {
                colaTemporal.enqueue(getColaListos().dequeue());
            }

            // Reemplazar la cola original con la temporal
            while (!colaTemporal.isEmpty()) {
                getColaListos().enqueue(colaTemporal.dequeue());
            }
        }
        simulador.actualizarTablas();
    }
}
