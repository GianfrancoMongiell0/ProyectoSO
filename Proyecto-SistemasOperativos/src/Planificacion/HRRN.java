/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package planificacion;

import Clases.Proceso;
import Estructuras.Lista;
import java.util.concurrent.Semaphore;

public class HRRN extends Planificador {

    private int tiempoGlobal = 0;

    @Override
    public Proceso siguienteProceso() {
        try {
            mutex.acquire();
            if (colaListos.isEmpty()) {
                return null;
            }

            // Calcular Response Ratio para todos los procesos
            Proceso seleccionado = null;
            double maxRatio = -1;
            int indexToRemove = -1;

            for (int i = 0; i < colaListos.getLength(); i++) {
                Proceso p = colaListos.get(i);
                double ratio = calcularResponseRatio(p);

                if (ratio > maxRatio) {
                    maxRatio = ratio;
                    seleccionado = p;
                    indexToRemove = i;
                }
            }

            if (seleccionado != null) {
                colaListos.deleteIndex(indexToRemove);
                tiempoGlobal += seleccionado.getInstruccionesRestantes();
            }
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
            p.getPCB().setTiempoLlegada(tiempoGlobal); // Registrar tiempo de llegada
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
    }

    private double calcularResponseRatio(Proceso p) {
        int tiempoEspera = tiempoGlobal - p.getPCB().getTiempoLlegada();
        int tiempoServicio = p.getTotalInstrucciones();
        return (tiempoEspera + tiempoServicio) / (double) tiempoServicio;
    }

    @Override
    public boolean estaVacio() {
        return colaListos.isEmpty();
    }

}
