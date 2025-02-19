package Clases;

import java.util.concurrent.atomic.AtomicInteger;

public class PCB {

    public enum Estado {
        READY, RUNNING, BLOCKED, TERMINATED,
    }

    private static final AtomicInteger contadorID = new AtomicInteger(0);
    private final int id;
    private Estado estado;
    private final String nombre;
    private int pc;
    private int mar;
    private final boolean esIOBound;
    private int ciclosExcepcion;
    private int ciclosCompletarExcepcion;
    private int ciclosEjecutadosDesdeUltimoBloqueo = 0;
    private int tiempoLlegada;
    private int tiempoEspera;

    // Constructor para CPU-bound
    public PCB(String nombre) {
        this.id = contadorID.getAndIncrement();
        this.nombre = nombre;
        this.estado = Estado.READY;
        this.pc = 0;
        this.mar = 0;
        this.esIOBound = false;
    }

    // Constructor para I/O bound
    public PCB(String nombre, int ciclosExcepcion, int ciclosCompletarExcepcion) {
        this.id = contadorID.getAndIncrement();
        this.nombre = nombre;
        this.estado = Estado.READY;
        this.pc = 0;
        this.mar = 0;
        this.esIOBound = true;
        this.ciclosExcepcion = ciclosExcepcion;
        this.ciclosCompletarExcepcion = ciclosCompletarExcepcion;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public int getPc() {
        return pc;
    }

    public void incrementarPc() {
        pc++;
    }

    public int getMar() {
        return mar;
    }

    public void incrementarMar() {
        mar++;
    }

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

    public int getTiempoLlegada() {
        return tiempoLlegada;
    }

    public void setTiempoLlegada(int tiempoLlegada) {
        this.tiempoLlegada = tiempoLlegada;
    }

    public int getCiclosEjecutadosDesdeUltimoBloqueo() {
        return ciclosEjecutadosDesdeUltimoBloqueo;
    }

    public void setCiclosEjecutadosDesdeUltimoBloqueo(int ciclosEjecutadosDesdeUltimoBloqueo) {
        this.ciclosEjecutadosDesdeUltimoBloqueo = ciclosEjecutadosDesdeUltimoBloqueo;
    }

    public int getTiempoEspera() {
        return tiempoEspera;
    }

    public void setTiempoEspera(int tiempoEspera) {
        this.tiempoEspera = tiempoEspera;
    }

    // Métodos para manejo de bloqueo
    public boolean debeBloquearse() {
        if (esIOBound) {
            ciclosEjecutadosDesdeUltimoBloqueo++;
            // Mostrar el número de ciclos ejecutados
            System.out.println("Ciclos ejecutados desde último bloqueo: " + ciclosEjecutadosDesdeUltimoBloqueo);
            if (ciclosEjecutadosDesdeUltimoBloqueo >= ciclosExcepcion) {
                reiniciarContadorBloqueo(); // Reiniciar el contador
                return true; // Indica que el proceso debe bloquearse
            }
        }
        return false; // Indica que el proceso no debe bloquearse
    }

    public void incrementarTiempoEspera() {
        tiempoEspera++;
    }

    public void reiniciarContadorBloqueo() {
        ciclosEjecutadosDesdeUltimoBloqueo = 0;
    }

    @Override
    public String toString() {
        return String.format(
                "PCB[ID=%d, Nombre=%s, Estado=%s, PC=%d, MAR=%d]",
                id, nombre, estado.name(), pc, mar
        );
    }
}
