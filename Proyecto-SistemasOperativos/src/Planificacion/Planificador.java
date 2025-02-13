package planificacion;

import Estructuras.Lista;
import Clases.Proceso;
import java.util.concurrent.Semaphore;

public abstract class Planificador {

    protected Lista<Proceso> colaListos = new Lista<>();
    protected Semaphore mutex = new Semaphore(1);
    protected int quantum;

    public abstract Proceso siguienteProceso();

    public abstract void agregarProceso(Proceso p);

    public abstract boolean estaVacio();

}
