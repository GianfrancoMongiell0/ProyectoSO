/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import Estructuras.Queue;
import Interfaces.Simulador;
import planificacion.Planificador;

/**
 *
 * @author LENOVO
 */
public class SistemaOperativo {

    private Planificador planificador; // Planificador activo (FCFS, RoundRobin, etc.)
    private Queue<Proceso> colaBloqueados;
    private Queue<Proceso> colaTerminados;
    private boolean enEjecucion;
    private Simulador simulador;

    public SistemaOperativo(Planificador planificador, Simulador simulador) {
        this.planificador = planificador;
        this.colaBloqueados = new Queue<>();
        this.colaTerminados = new Queue<>();
        this.enEjecucion = true;
        this.simulador = simulador;
    }

    // Método para agregar procesos al planificador
    public void agregarProceso(Proceso proceso) {
        planificador.agregarProceso(proceso);
        simulador.actualizarTablas();
    }

    // Método para obtener el próximo proceso (usado por CPUs)
    public synchronized Proceso obtenerSiguienteProceso() {
        return planificador.siguienteProceso();
    }

    // Método para mover procesos a bloqueados
    public void moverAColaBloqueados(Proceso proceso) {
        proceso.getPCB().setEstado("Bloqueado");
        colaBloqueados.enqueue(proceso);
        simulador.actualizarTablas();
    }

    // Método para mover procesos a terminados
    public void moverAColaTerminados(Proceso proceso) {
        proceso.getPCB().setEstado("Terminado");
        colaTerminados.enqueue(proceso);
        simulador.actualizarTablas();
    }

    // Verificar si hay procesos pendientes
    public synchronized boolean hayProcesosPendientes() {
        return !planificador.estaVacio() || !colaBloqueados.isEmpty();
    }

    // Detener la ejecución
    public void detenerCPU() {
        enEjecucion = false;
    }

    // Getters
    public boolean isEnEjecucion() {
        return enEjecucion;
    }

    public Planificador getPlanificador() {
        return planificador;
    }

    public Queue<Proceso> getColaBloqueados() {
        return colaBloqueados;
    }

    public Queue<Proceso> getColaTerminados() {
        return colaTerminados;
    }

}
