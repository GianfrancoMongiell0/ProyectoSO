/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Clases;

import Interfaces.Simulador;
import Utils.LectorEscritorTxt;

/**
 *
 * @author gianf y alejandra
 */
public class ProyectoSistemasOperativos {
    public static void main(String[] args) {
    
    
        String ruta = "C://Users/LENOVO/Desktop/Prueba.txt";
        Proceso proceso1 = new Proceso("Proceso 1", 20);
        
        
        LectorEscritorTxt lector = new LectorEscritorTxt();
        lector.escribirArchivo(ruta,"Hola", false);
        lector.leerArchivo(ruta);
        
            
        Simulador simulador = new Simulador();
        SistemaOperativo so = new SistemaOperativo(3, simulador);
    
        simulador.setVisible(true);
        so.iniciarCPU();
    
}
}
