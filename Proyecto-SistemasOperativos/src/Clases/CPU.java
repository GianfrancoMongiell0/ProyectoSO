package Clases;

public class CPU extends Thread {

    private final SistemaOperativo so;
    public final int id;
    private Proceso procesoEnEjecucion; // Almacenar el proceso en ejecución
    private int ciclosEjecutados;

    public CPU(SistemaOperativo so, int id) {
        this.so = so;
        this.id = id;
        this.procesoEnEjecucion = null;
        this.ciclosEjecutados = 0;
    }
    // Método para obtener el proceso que está siendo ejecutado en la CPU

    public Proceso obtenerProcesoEnEjecucion() {
        return procesoEnEjecucion;
    }

    @Override
    public void run() {
        System.out.println("CPU " + id + " iniciada");
        while (so.isEnEjecucion()) {
            try {
                Proceso proceso = so.obtenerSiguienteProceso();
                if (proceso == null) {
                    Thread.sleep(so.getDuracionCiclo()); // Respeta la duración del ciclo
                    continue;
                }

                procesoEnEjecucion = proceso;
                proceso.getPCB().setEstado(PCB.Estado.RUNNING);
                ciclosEjecutados = 0; // Reiniciar contador de quantum

                int quantum = so.getPlanificador().getQuantum();

                // Ejecutar mientras no termine, no se bloquee o se alcance el quantum (para RR)
                while (!proceso.estaTerminado() && !proceso.debeBloquearse() && (quantum == 0 || ciclosEjecutados < quantum)) {
                    proceso.ejecutarInstruccion();
                    so.incrementarCiclosReloj();
                    ciclosEjecutados++;

                    // Actualizar GUI en tiempo real
                    Thread.sleep(so.getDuracionCiclo());
                }

                if (proceso.estaTerminado()) {
                    so.moverAColaTerminados(proceso);
                } else if (proceso.debeBloquearse()) {
                    so.moverAColaBloqueados(proceso);
                } else {
                    so.agregarProceso(proceso);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("CPU " + id + " detenida");
    }
}
