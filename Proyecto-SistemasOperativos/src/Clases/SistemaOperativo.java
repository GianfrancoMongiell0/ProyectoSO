/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import Estructuras.Queue;
import Interfaces.Simulador;

/**
 *
 * @author LENOVO
 */
public class SistemaOperativo {
    private Queue<Proceso> colaListos;
    private Queue<Proceso> colaBloqueados;
    private Queue<Proceso> colaTerminados;
    private CPU cpu;
    private boolean enEjecucion;
    private Simulador simulador;

    public SistemaOperativo(int quantum, Simulador simulador) {
        this.colaListos = new Queue<>();
        this.colaBloqueados = new Queue<>();
        this.colaTerminados = new Queue<>();
        this.cpu = new CPU(this, quantum);
        this.enEjecucion = true;
        this.simulador = simulador;
    }

    public void agregarProceso(Proceso proceso) {
        colaListos.enqueue(proceso);
        simulador.actualizarTablas();  // Actualizar la interfaz
    }

    public void iniciarCPU() {
        cpu.start(); // Inicia el hilo de la CPU
    }

    public synchronized Proceso obtenerSiguienteProceso() {
        if (!colaListos.isEmpty()) {
            return colaListos.dequeue();
        }
        return null;
    }

    public void moverAColaBloqueados(Proceso proceso) {
        proceso.getPCB().setEstado("Bloqueado");
        colaBloqueados.enqueue(proceso);
        simulador.actualizarTablas();
    }

    public void moverAColaTerminados(Proceso proceso) {
        proceso.getPCB().setEstado("Terminado");
        colaTerminados.enqueue(proceso);
        simulador.actualizarTablas();
    }

    public synchronized boolean hayProcesosPendientes() {
        return !colaListos.isEmpty();
    }

    public void detenerCPU() {
        enEjecucion = false;
    }

    public boolean isEnEjecucion() {
        return enEjecucion;
    }
}
