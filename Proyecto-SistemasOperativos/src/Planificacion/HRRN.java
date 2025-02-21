package planificacion;

import Clases.Proceso;
import Estructuras.Queue;

public class HRRN extends Planificador {

    public HRRN() {
        // No se necesita inicialización específica para HRRN en este momento, se usa el constructor de Planificador
    }

    @Override
    public Proceso siguienteProceso() {
        try {
            mutex.acquire();
            if (colaListos.isEmpty()) {
                return null; // No hay procesos en la cola de listos
            }

            double maxRatio = -1;
            Proceso seleccionado = null;
            Queue<Proceso> colaAuxiliar = new Queue<>(); // Cola auxiliar temporal

            int lengthColaListos = colaListos.getLength();
            for (int i = 0; i < lengthColaListos; i++) {
                Proceso procesoActual = colaListos.dequeue(); // Sacar el proceso del frente de la cola
                double ratio = calcularResponseRatio(procesoActual); // Calcular el ratio de respuesta

                if (ratio > maxRatio) {
                    maxRatio = ratio;
                    seleccionado = procesoActual; // Actualizar el proceso seleccionado si tiene un ratio mayor
                }
                colaAuxiliar.enqueue(procesoActual); // Re-encolar en la cola auxiliar
            }

            // Re-encolar de vuelta a la cola de listos, excepto el proceso seleccionado
            lengthColaListos = colaAuxiliar.getLength(); // Recalcular longitud por seguridad
            for (int i = 0; i < lengthColaListos; i++) {
                Proceso procesoAux = colaAuxiliar.dequeue();
                if (procesoAux != seleccionado) {
                    colaListos.enqueue(procesoAux); // Re-encolar solo si no es el proceso seleccionado
                }
            }
            return seleccionado; // Retornar el proceso con el response ratio más alto

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            mutex.release(); // Liberar el mutex al finalizar (en caso de excepción o no)
        }
    }

    @Override
    public void agregarProceso(Proceso p) {
        try {
            mutex.acquire();
            p.getPCB().setTiempoLlegada(getTiempoGlobal()); // Registrar tiempo de llegada para cálculo de ratio
            colaListos.enqueue(p); // Añadir proceso a la cola de listos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release(); // Liberar el mutex
        }
    }

    private double calcularResponseRatio(Proceso p) {
        int tiempoEspera = getTiempoGlobal() - p.getPCB().getTiempoLlegada();
        int tiempoServicio = p.getTotalInstrucciones(); // Usar getTotalInstrucciones para tiempo de servicio
        // Evitar división por cero si tiempoServicio es 0 (aunque no debería pasar en este contexto)
        if (tiempoServicio <= 0) {
            return 1.0; // Ratio mínimo si el tiempo de servicio es cero o negativo (valor por defecto razonable)
        }
        return (double) (tiempoEspera + tiempoServicio) / tiempoServicio; // Fórmula del Response Ratio
    }

    @Override
    public boolean estaVacio() {
        return colaListos.isEmpty();
    }

    // Ya no se necesita tiempoGlobal en HRRN, se usa el tiempoGlobal centralizado en Planificador/SistemaOperativo (opcional refactorización)
    // Los métodos getTiempoGlobal, setTiempoGlobal e incrementarTiempoGlobal se heredan de Planificador y pueden ser usados directamente.
    // reordenarCola() se hereda de Planificador y tiene una implementación por defecto vacía,
    // HRRN NO necesita una implementación específica de reordenarCola() en este diseño.
}
