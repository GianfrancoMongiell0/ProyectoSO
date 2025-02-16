package Clases;

public class CPU extends Thread {

    private final SistemaOperativo so;
    public final int id;
    private Proceso procesoEnEjecucion; // Almacenar el proceso en ejecución
    
    public CPU(SistemaOperativo so, int id) {
        this.so = so;
        this.id = id;
    }
     // Método para obtener el proceso que está siendo ejecutado en la CPU
    public Proceso obtenerProcesoEnEjecucion() {
        return procesoEnEjecucion;
    }
    @Override
    public void run() {
        System.out.println("CPU " + id + " ha iniciado.");
        while (so.isEnEjecucion()) {
            try {
                Proceso proceso = so.obtenerSiguienteProceso();

                if (proceso == null) {
                    Thread.sleep(100); // Espera no activa
                    continue;
                }
                
                // Asignar el proceso a la CPU
                procesoEnEjecucion = proceso;
                proceso.ejecutarInstruccion();
                System.out.println("CPU " + id + " ejecutando: " + proceso.getPCB().toString());

                // Actualiza los ciclos de reloj globales
                so.incrementarCiclosReloj();
                
                System.out.println("CPU " + id + ": Evaluando proceso " + proceso.getPCB().getNombre());
                if (proceso.estaTerminado()) {
                System.out.println("CPU " + id + ": Mover a Terminados " + proceso.getPCB().getNombre());
                so.moverAColaTerminados(proceso);
            } else if (proceso.debeBloquearse()) {
                System.out.println("CPU " + id + ": Mover a Bloqueados " + proceso.getPCB().getNombre());
                so.moverAColaBloqueados(proceso);
            } else {
                System.out.println("CPU " + id + ": Reagregar a la cola de listos " + proceso.getPCB().getNombre());
                so.agregarProceso(proceso);
            }
                
                Thread.sleep(so.getDuracionCiclo());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
   
}

