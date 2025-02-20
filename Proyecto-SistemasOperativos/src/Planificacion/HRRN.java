package planificacion;

import Clases.Proceso;
import Clases.SistemaOperativo;

public class HRRN extends Planificador {

    private SistemaOperativo sistemaOperativo;

    public HRRN() {
        System.out.println("[HRRN] Planificador iniciado con reloj global");
    }

    private double calcularResponseRatio(Proceso p) {
        int tiempoEspera = sistemaOperativo.getCiclosRelojGlobal() - p.getPCB().getTiempoLlegada();
        int tiempoServicio = p.getTotalInstrucciones();
        double ratio = (tiempoEspera + tiempoServicio) / (double) tiempoServicio;

        System.out.println("[HRRN] Ratio para " + p.getPCB().getNombre() + ": Espera=" + tiempoEspera + ", Servicio=" + tiempoServicio + ", Ratio=" + String.format("%.2f", ratio));

        return ratio;
    }

    // Método para establecer SistemaOperativo después de la creación
    public void setSistemaOperativo(SistemaOperativo sistemaOperativo) {
        this.sistemaOperativo = sistemaOperativo;
        System.out.println("[HRRN] Sistema Operativo asignado.");
    }

    @Override
    public Proceso siguienteProceso() {
        try {
            mutex.acquire();
            if (listaListos.isEmpty()) {
                return null;
            }

            // Busca el proceso con el ratio de respuesta mas alto
            Proceso seleccionado = listaListos.get(0);
            double maxRatio = calcularResponseRatio(seleccionado);

            for (int i = 1; i < listaListos.getLength(); i++) {
                Proceso p = listaListos.get(i);
                double ratio = calcularResponseRatio(p);
                if (ratio > maxRatio) {
                    maxRatio = ratio;
                    seleccionado = p;
                }
            }

            // Eliminar el proceso seleccionado
            listaListos.deleteIndex(listaListos.indexOf(seleccionado));
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
            listaListos.insertLast(p);
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
