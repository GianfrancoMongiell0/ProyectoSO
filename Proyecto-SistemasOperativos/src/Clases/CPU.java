package Clases;

public class CPU extends Thread {

    private final SistemaOperativo so;
    private final int id;

    public CPU(SistemaOperativo so, int id) {
        this.so = so;
        this.id = id;
    }

    @Override
    public void run() {
        while (so.isEnEjecucion()) {
            try {
                Proceso proceso = so.obtenerSiguienteProceso();

                if (proceso == null) {
                    Thread.sleep(100); // Espera no activa
                    continue;
                }

                proceso.ejecutarInstruccion();
                System.out.println("CPU " + id + " ejecutando: " + proceso.getPCB().toString());

                if (proceso.debeBloquearse()) {
                    so.moverAColaBloqueados(proceso);
                } else if (proceso.estaTerminado()) {
                    so.moverAColaTerminados(proceso);
                } else {
                    so.agregarProceso(proceso);
                }

                Thread.sleep(so.getDuracionCiclo());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
