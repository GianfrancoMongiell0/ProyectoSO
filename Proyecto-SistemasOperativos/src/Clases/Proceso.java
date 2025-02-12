/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author gianf
 */
public class Proceso {

    private final PCB pcb;
    private final int totalInstrucciones;
    private int instruccionesRestantes;
    private boolean terminado = false;

    // Constructor para CPU-bound
    public Proceso(String nombre, int totalInstrucciones) {
        this.pcb = new PCB(nombre);
        this.totalInstrucciones = totalInstrucciones;
        this.instruccionesRestantes = totalInstrucciones;
    }

    // Constructor para I/O bound
    public Proceso(String nombre, int totalInstrucciones, int ciclosExcepcion, int ciclosCompletarExcepcion) {
        this.pcb = new PCB(nombre, ciclosExcepcion, ciclosCompletarExcepcion);
        this.totalInstrucciones = totalInstrucciones;
        this.instruccionesRestantes = totalInstrucciones;
    }

 
    // Método para ejecutar una instrucción (simulación)
    public void ejecutarInstruccion() {
        if (!terminado) {
            pcb.incrementarPc();
            pcb.incrementarMar();
            instruccionesRestantes--;

            if (instruccionesRestantes == 0) {
                pcb.setEstado("Terminado");
                terminado = true;

            }
            System.out.println(instruccionesRestantes);
        }
    }

     // Método para verificar si el proceso debe bloquearse
    public boolean debeBloquearse() {
        return pcb.debeBloquearse();
    }
    
    // Getters
    public PCB getPCB() {
        return pcb;
    }

    public int getTotalInstrucciones() {
        return totalInstrucciones;
    }
    
    public int getInstruccionesRestantes() {
        return instruccionesRestantes;
    }

    public boolean estaTerminado() {
        return terminado;
    }
    
    

    @Override
    public String toString() {
        return ( pcb.toString() +"," +instruccionesRestantes);
    }

}
