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
                
                proceso.getPCB().setEstado(PCB.Estado.RUNNING); // Establecer estado a RUNNING

                // Asignar el proceso a la CPU
                procesoEnEjecucion = proceso;
             
                // Bucle para ejecutar instrucciones hasta que el proceso termine o se bloquee
             while (!proceso.estaTerminado() && !proceso.debeBloquearse()) {
                 proceso.ejecutarInstruccion();
                 System.out.println("CPU " + id + " ejecutando: " + proceso.getPCB().toString());

                 try {
                     Thread.sleep(so.getDuracionCiclo()); // Simular duración de ciclo
                 } catch (InterruptedException e) {
                     Thread.currentThread().interrupt();
                 }
             }

             // Después de terminar o bloquearse, manejar el proceso
             if (proceso.estaTerminado()) {
                 so.moverAColaTerminados(proceso);
             } else if (proceso.debeBloquearse()) {
                 so.moverAColaBloqueados(proceso);
             } else {
                 so.agregarProceso(proceso); // Si no ha terminado ni bloqueado
             }

             procesoEnEjecucion = null; // La CPU está libre
         } catch (InterruptedException e) {
             Thread.currentThread().interrupt();
         }
}
    }
}
