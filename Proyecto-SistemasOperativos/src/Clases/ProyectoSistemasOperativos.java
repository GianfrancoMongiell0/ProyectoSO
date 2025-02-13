/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Clases;

import Interfaces.Simulador;
import Utils.LectorEscritorTxt;
import planificacion.FCFS;
import planificacion.Planificador;

/**
 *
 * @author gianf y alejandra
 */
public class ProyectoSistemasOperativos {

    public static void main(String[] args) {

        //String ruta = "C:/Users/gianf/Desktop/PruebaJava.txt";
        //Proceso proceso1 = new Proceso("Proceso 1", 20);
        //LectorEscritorTxt lector = new LectorEscritorTxt();
        //lector.escribirArchivo(ruta, "Hola", false);
        //lector.leerArchivo(ruta);
        Planificador plan = new FCFS();
        Simulador simulador = new Simulador();
        SistemaOperativo so = new SistemaOperativo(plan, 2);

        simulador.setVisible(true);
        /*so.iniciarCPU();
        
        Aqui hay que hacerlo desde la interfaz
         */

    }
}
