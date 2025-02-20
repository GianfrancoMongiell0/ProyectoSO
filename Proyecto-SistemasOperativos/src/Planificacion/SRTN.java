package planificacion;

import Clases.Proceso;

public class SRTN extends Planificador {

    private Proceso procesoActual; // Proceso en ejecución actualmente

    @Override
    public Proceso siguienteProceso() {
        try {
            mutex.acquire();
            if (listaListos.isEmpty()) {
                return null;
            }

            // Buscar proceso con menor instrucciones restantes
            Proceso mejorProceso = listaListos.get(0);
            int minInstrucciones = mejorProceso.getInstruccionesRestantes();

            for (int i = 1; i < listaListos.getLength(); i++) {
                Proceso p = listaListos.get(i);
                if (p.getInstruccionesRestantes() < minInstrucciones) {
                    mejorProceso = p;
                    minInstrucciones = p.getInstruccionesRestantes();
                }
            }

            // Eliminar de la lista
            listaListos.deleteIndex(listaListos.indexOf(mejorProceso));
            procesoActual = mejorProceso; // Asignar como proceso en ejecución
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

            if (procesoActual != null && p.getInstruccionesRestantes() < procesoActual.getInstruccionesRestantes()) {
                System.out.println("[SRTN] ¡Preempción! Nuevo proceso más corto: " + p.getPCB().getNombre() + " | Inst. restantes: " + p.getInstruccionesRestantes() + " vs actual: "
                        + procesoActual.getInstruccionesRestantes());

                listaListos.insertLast(procesoActual);
                procesoActual = null;
            }

            listaListos.insertLast(p);
            System.out.println("[SRTN] Proceso agregado: " + p.getPCB().getNombre() + " | Inst. restantes: " + p.getInstruccionesRestantes());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
    }

    @Override
    public boolean estaVacio() {
        return listaListos.isEmpty() && procesoActual == null;
    }

    // Método nuevo para limpiar procesoActual cuando termina
    public void procesoTerminado() {
        procesoActual = null;
    }
}
