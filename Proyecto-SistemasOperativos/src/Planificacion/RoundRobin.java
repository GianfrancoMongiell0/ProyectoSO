/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package planificacion;

import Clases.Proceso;
import Estructuras.Queue;
import Estructuras.Lista;
import java.util.concurrent.Semaphore;

/**
 *
 * @author gianf
 */
public class RoundRobin extends FCFS {

    private Lista<ParProcesoCiclos> contadorQuantum = new Lista<>();
    private int quantum;

    // Clase interna para asociar proceso con sus ciclos ejecutados
    private static class ParProcesoCiclos {

        Proceso proceso;
        int ciclos;

        public ParProcesoCiclos(Proceso proceso, int ciclos) {
            this.proceso = proceso;
            this.ciclos = ciclos;
        }
    }

    public RoundRobin(int quantum) {
        this.quantum = quantum;
    }

    @Override
    public Proceso siguienteProceso() {
        try {
            mutex.acquire();
            if (colaListos.isEmpty()) {
                return null;
            }

            // Usar dequeue() en lugar de deleteFirst()
            Proceso p = colaListos.dequeue();
            resetContador(p);
            return p;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            mutex.release();
        }
    }

    public boolean checkQuantum(Proceso p) {
        try {
            mutex.acquire();
            int ciclos = getCiclos(p) + 1;
            actualizarContador(p, ciclos);
            return ciclos >= quantum;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            mutex.release();
        }
    }

    public void recolocarProceso(Proceso p) {
        try {
            mutex.acquire();
            if (!p.estaTerminado()) {
                // Usar enqueue() en lugar de insertLast()
                colaListos.enqueue(p);
                actualizarContador(p, 0);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
    }

    // Métodos auxiliares (sin cambios, siguen usando Lista<T>)
    private int getCiclos(Proceso p) {
        for (int i = 0; i < contadorQuantum.getLength(); i++) {
            ParProcesoCiclos par = contadorQuantum.get(i);
            if (par.proceso.equals(p)) {
                return par.ciclos;
            }
        }
        return 0;
    }

    private void actualizarContador(Proceso p, int nuevosCiclos) {
        for (int i = 0; i < contadorQuantum.getLength(); i++) {
            ParProcesoCiclos par = contadorQuantum.get(i);
            if (par.proceso.equals(p)) {
                par.ciclos = nuevosCiclos;
                return;
            }
        }
        contadorQuantum.insertLast(new ParProcesoCiclos(p, nuevosCiclos));
    }

    private void resetContador(Proceso p) {
        for (int i = 0; i < contadorQuantum.getLength(); i++) {
            ParProcesoCiclos par = contadorQuantum.get(i);
            if (par.proceso.equals(p)) {
                contadorQuantum.deleteIndex(i);
                return;
            }
        }
    }
}
