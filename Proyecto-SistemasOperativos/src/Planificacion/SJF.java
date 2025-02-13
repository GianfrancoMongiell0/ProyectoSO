/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */package planificacion;

import Clases.Proceso;

public class SJF extends Planificador {

    @Override
    public Proceso siguienteProceso() {
        try {
            mutex.acquire();
            if (colaListos.isEmpty()) {
                return null;
            }

            // Buscar el proceso con menos instrucciones restantes
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
