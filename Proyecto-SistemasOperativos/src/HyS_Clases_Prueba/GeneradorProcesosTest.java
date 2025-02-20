
import Clases.Proceso;
import Estructuras.Queue;

public class GeneradorProcesosTest {

    public static Queue<Proceso> generarProcesosPrueba() {
        Queue<Proceso> cola = new Queue<>();

        // Proceso 1: Corto, CPU-bound, llega al inicio
        Proceso proceso1 = new Proceso("P1", 5); // 5 instrucciones
        proceso1.getPCB().setTiempoLlegada(0); // Asignar tiempo de llegada

        // Proceso 2: Largo, CPU-bound, llega en ciclo 1
        Proceso proceso2 = new Proceso("P2", 15);
        proceso2.getPCB().setTiempoLlegada(1);

        // Proceso 3: I/O-bound, con bloqueos cada 3 ciclos
        Proceso proceso3 = new Proceso(
                "P3",
                8, // Total instrucciones
                3, // Cada cuántos ciclos se bloquea (ciclosExcepcion)
                3 // Ciclos para completar la excepción (ciclosCompletarExcepcion)
        );
        proceso3.getPCB().setTiempoLlegada(2);

        // Proceso 4: Intermedio, llega en ciclo 4
        Proceso proceso4 = new Proceso("P4", 10);
        proceso4.getPCB().setTiempoLlegada(4);

        // Proceso 5: Muy corto, llega en ciclo 5
        Proceso proceso5 = new Proceso("P5", 3);
        proceso5.getPCB().setTiempoLlegada(5);

        // Encolar todos los procesos
        cola.enqueue(proceso1);
        cola.enqueue(proceso2);
        cola.enqueue(proceso3);
        cola.enqueue(proceso4);
        cola.enqueue(proceso5);

        return cola;
    }
}
