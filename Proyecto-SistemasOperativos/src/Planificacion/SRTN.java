package planificacion;

import Clases.Proceso;
import Estructuras.Lista;
import java.util.concurrent.Semaphore;

public class SRTN extends Planificador {

    @Override
    public Proceso siguienteProceso() {
        try {
            mutex.acquire();
            if (colaListos.isEmpty()) {
                return null;
            }

            // Buscar proceso con menos instrucciones restantes
            int minInstrucciones = Integer.MAX_VALUE;
            int indexToRemove = -1;

            for (int i = 0; i < colaListos.getLength(); i++) {
                Proceso p = colaListos.get(i);
                if (p.getInstruccionesRestantes() < minInstrucciones) {
                    minInstrucciones = p.getInstruccionesRestantes();
                    indexToRemove = i;
                }
            }

            Proceso seleccionado = colaListos.get(indexToRemove);
            colaListos.deleteIndex(indexToRemove);
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
            // Insertar manteniendo orden ascendente de instrucciones restantes
            int index = 0;
            while (index < colaListos.getLength()
                    && colaListos.get(index).getInstruccionesRestantes() < p.getInstruccionesRestantes()) {
                index++;
            }
            colaListos.insertIndex(index, p);
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
