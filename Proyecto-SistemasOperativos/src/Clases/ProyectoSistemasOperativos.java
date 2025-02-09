/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Clases;

import Utils.LectorEscritorTxt;

/**
 *
 * @author gianf y alejandra
 */
public class ProyectoSistemasOperativos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String ruta = "C:/Users/gianf/Desktop/PruebaJava.txt";
        Proceso proceso1 = new Proceso("Proceso 1", 20);
        
        
        LectorEscritorTxt lector = new LectorEscritorTxt();
        lector.escribirArchivo(ruta,"Hola", false);
        lector.leerArchivo(ruta);
    }
    
}
