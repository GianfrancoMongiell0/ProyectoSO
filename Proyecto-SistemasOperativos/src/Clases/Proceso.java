/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author LENOVO
 */
public class Proceso {
    private static int idCounter = 0;  // Contador para asignar IDs únicos
    private int id;
    private String nombre;
    private int instruccionesRestantes;
    private boolean esIOVinculado;  // Indica si el proceso está vinculado a operaciones de I/O
    private int cicloDeSolicitudIO;  // Cada cuántos ciclos necesita I/O
    private int cicloDeResolucionIO;  // Cuántos ciclos de I/O toma resolver
    private String estado;  // "READY", "RUNNING", "BLOCKED"
    private int pC;
    private int mAR; // MAR: Registro de la dirección de memoria

    // Constructor de la clase
 public Proceso(String nombre, int instrucciones, boolean esIOVinculado, int cicloDeSolicitudIO, int cicloDeResolucionIO) {
        this.id = ++idCounter;  // Asigna un ID único
        this.nombre = nombre;
        this.instruccionesRestantes = instrucciones;
        this.esIOVinculado = esIOVinculado;
        this.cicloDeSolicitudIO = cicloDeSolicitudIO;
        this.cicloDeResolucionIO = cicloDeResolucionIO;
        this.estado = "READY";  // Al crearse, el proceso está en estado READY
        this.pC = 0;  // Comienza en el primer ciclo
        this.mAR = 0; // Dirección de memoria inicial
        
    }

// Ejecutar una instrucción, disminuyendo el contador de instrucciones
    public void executeInstruction() {
        if (instruccionesRestantes > 0) {
            instruccionesRestantes--;
            pC++;  // Avanzamos el Program Counter
            mAR = pC;  // El MAR refleja la dirección actual del PC
        }
    }

    // Verificar si el proceso ha finalizado
    public boolean isFinished() {
        return instruccionesRestantes <= 0;
    }

    // Verificar si el proceso necesita realizar operaciones de I/O
    public boolean needsIO() {
        return esIOVinculado && instruccionesRestantes % cicloDeSolicitudIO == 0;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getInstruccionesRestantes() {
        return instruccionesRestantes;
    }

    public boolean isEsIOVinculado() {
        return esIOVinculado;
    }

    public int getCicloDeSolicitudIO() {
        return cicloDeSolicitudIO;
    }

    public int getCicloDeResolucionIO() {
        return cicloDeResolucionIO;
    }

    public String getEstado() {
        return estado;
    }

    public int getpC() {
        return pC;
    }

    public int getmAR() {
        return mAR;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setInstruccionesRestantes(int instruccionesRestantes) {
        this.instruccionesRestantes = instruccionesRestantes;
    }

    public void setEsIOVinculado(boolean esIOVinculado) {
        this.esIOVinculado = esIOVinculado;
    }

    public void setCicloDeSolicitudIO(int cicloDeSolicitudIO) {
        this.cicloDeSolicitudIO = cicloDeSolicitudIO;
    }

    public void setCicloDeResolucionIO(int cicloDeResolucionIO) {
        this.cicloDeResolucionIO = cicloDeResolucionIO;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setpC(int pC) {
        this.pC = pC;
    }

    public void setmAR(int mAR) {
        this.mAR = mAR;
    }

       
}