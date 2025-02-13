package Clases;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author gianf
 */
public class PCB {

    // Atributos del PCB
    private static int contadorID = 0; // Generador de IDs únicos
    private final int id;
    private String estado; // Running, Blocked, Ready
    private final String nombre;
    private int pc; // Program Counter
    private int mar; // Memory Address Register
    private final boolean esIOBound;
    private int ciclosExcepcion; // Ciclos para generar excepción (solo I/O bound)
    private int ciclosCompletarExcepcion; // Ciclos para resolver excepción (solo I/O bound)
    private int ciclosEjecutadosDesdeUltimoBloqueo = 0; // Contador de ciclos ejecutados
    private int tiempoLlegada;

    // Constructor (para CPU-bound)
    public PCB(String nombre) {
        this.id = contadorID++;
        this.nombre = nombre;
        this.estado = "Ready";
        this.pc = 0;
        this.mar = 0;
        this.esIOBound = false;
    }

    // Constructor (para I/O bound)
    public PCB(String nombre, int ciclosExcepcion, int ciclosCompletarExcepcion) {
        this.id = contadorID++;
        this.nombre = nombre;
        this.estado = "Ready";
        this.pc = 0;
        this.mar = 0;
        this.esIOBound = true;
        this.ciclosExcepcion = ciclosExcepcion;
        this.ciclosCompletarExcepcion = ciclosCompletarExcepcion;
    }

    // Getters y setters (solo los críticos)
    public int getId() {
        return id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getPc() {
        return pc;
    }

    public void incrementarPc() {
        pc++;
    } // Avanza el PC en cada ciclo

    public int getMar() {
        return mar;
    }

    public void incrementarMar() {
        mar++;
    } // Avanza el MAR en cada ciclo

    public boolean esIOBound() {
        return esIOBound;
    }

    public int getCiclosExcepcion() {
        return ciclosExcepcion;
    }

    public int getCiclosCompletarExcepcion() {
        return ciclosCompletarExcepcion;
    }

    public String getNombre() {
        return nombre;
    }

    public static int getContadorID() {
        return contadorID;
    }

    public static void setContadorID(int contadorID) {
        PCB.contadorID = contadorID;
    }

    public int getCiclosEjecutadosDesdeUltimoBloqueo() {
        return ciclosEjecutadosDesdeUltimoBloqueo;
    }

    public void setCiclosEjecutadosDesdeUltimoBloqueo(int ciclosEjecutadosDesdeUltimoBloqueo) {
        this.ciclosEjecutadosDesdeUltimoBloqueo = ciclosEjecutadosDesdeUltimoBloqueo;
    }

    public int getTiempoLlegada() {
        return tiempoLlegada;
    }

    public void setTiempoLlegada(int tiempoLlegada) {
        this.tiempoLlegada = tiempoLlegada;
    }

    // Método para verificar si el proceso debe bloquearse
    public boolean debeBloquearse() {
        if (esIOBound) {
            ciclosEjecutadosDesdeUltimoBloqueo++;
            if (ciclosEjecutadosDesdeUltimoBloqueo >= ciclosExcepcion) {
                ciclosEjecutadosDesdeUltimoBloqueo = 0; // Reiniciar contador
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return id + "," + nombre + "," + estado + "," + pc + "," + mar;
    }
}
