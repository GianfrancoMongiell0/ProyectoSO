/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package HyS_Clases_Prueba;

import static java.lang.Thread.sleep;
import java.util.concurrent.Semaphore;

/**
 *
 * @author gianf
 */
public class Hilos extends Thread {

    private Semaphore mutex;
    private String mensaje;

    public Hilos(String mensaje, Semaphore mutex) {
        this.mutex = mutex;
        this.mensaje = mensaje;
    }

    @Override
    public void run() {
        while (true) {

            try {
                mutex.acquire();

                System.out.println(mensaje);
                sleep(500);

                mutex.release();
                sleep(1000);

            } catch (InterruptedException e) {

                System.out.println(e);

            }
        }
    }

}