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

            int minIndex = 0;
            for (int i = 1; i < colaListos.getLength(); i++) {
                if (colaListos.get(i).getInstruccionesRestantes()
                        < colaListos.get(minIndex).getInstruccionesRestantes()) {
                    minIndex = i;
                }
            }

            Proceso seleccionado = colaListos.get(minIndex);
            colaListos.deleteIndex(minIndex);
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
            colaListos.insertLast(p);
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
