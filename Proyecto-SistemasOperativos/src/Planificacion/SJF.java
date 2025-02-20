package planificacion;

import Clases.Proceso;

public class SJF extends Planificador {

    @Override
    public Proceso siguienteProceso() {
        try {
            mutex.acquire();
            if (listaListos.isEmpty()) {
                return null;
            }

            // Buscar proceso con menor tiempo
            Proceso mejorProceso = listaListos.get(0);
            int minInstrucciones = mejorProceso.getTotalInstrucciones();

            for (int i = 1; i < listaListos.getLength(); i++) {
                Proceso p = listaListos.get(i);
                if (p.getTotalInstrucciones() < minInstrucciones) {
                    mejorProceso = p;
                    minInstrucciones = p.getTotalInstrucciones();
                }
            }

            listaListos.deleteIndex(listaListos.indexOf(mejorProceso));
            System.out.println("[SJF] Proceso seleccionado: " + mejorProceso.getPCB().getNombre() + " | Duración: " + mejorProceso.getTotalInstrucciones() + " ciclos");

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
            listaListos.insertLast(p);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
    }

    @Override
    public boolean estaVacio() {
        return listaListos.isEmpty();
    }
}
