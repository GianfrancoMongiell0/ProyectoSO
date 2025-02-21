/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

/**
 *
 * @author gianf
 */
import java.io.*;

public class LectorEscritorTxt {

    public static void main(String[] args) {
        String rutaArchivo = "archivo.txt"; // Cambia esto por la ruta de tu archivo

        // Verificar si el archivo existe
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            System.out.println("El archivo no existe. Creando un nuevo archivo...");
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                System.out.println("Error al crear el archivo: " + e.getMessage());
                return;
            }
        }
    }

    // Método para leer el archivo
    public static void leerArchivo(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            System.out.println("Contenido del archivo:");
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    // Método para escribir o modificar el archivo
    public  void escribirArchivo(String rutaArchivo, String texto, boolean append) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo, append))) {
            bw.write(texto);
            bw.newLine();
            System.out.println("Texto escrito correctamente.");
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }
}

