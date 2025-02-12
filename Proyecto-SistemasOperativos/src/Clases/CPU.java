/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author LENOVO
 */
import Estructuras.Queue;
import java.util.concurrent.Semaphore;

public class CPU extends Thread {
    private SistemaOperativo so;
    private int quantum;

    public CPU(SistemaOperativo so, int quantum) {
        this.so = so;
        this.quantum = quantum;
    }

    @Override
    public void run() {
    while (so.isEnEjecucion()) {
        Proceso proceso = so.obtenerSiguienteProceso();

        if (proceso == null) {
            System.out.println("No hay más procesos en cola. Apagando el sistema...");
            so.detenerCPU();
            break;
        }

        System.out.println("Ejecutando: " + proceso.getPCB().toString());

        int ciclos = 0;
        while (ciclos < quantum && !proceso.estaTerminado()) {
            proceso.ejecutarInstruccion();
            ciclos++;

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (proceso.debeBloquearse()) {
                System.out.println("Proceso " + proceso.getPCB().getId() + " se ha bloqueado.");
                so.moverAColaBloqueados(proceso);
                break;
            }
        }

        if (!proceso.estaTerminado() && !proceso.debeBloquearse()) {
            System.out.println("Proceso " + proceso.getPCB().getId() + " no terminó, regresando a cola de listos.");
            so.agregarProceso(proceso);
        } else if (proceso.estaTerminado()) {
            System.out.println("Proceso " + proceso.getPCB().getId() + " ha finalizado.");
            so.moverAColaTerminados(proceso);
        }
    }
    }
}