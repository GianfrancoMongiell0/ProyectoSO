/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Estructuras;

/**
 *
 * @author LENOVO
 * @param <T>
 */
public class Queue<T> {
    private Nodo<T> first; 
    private Nodo<T> last;  
    private int length;    

    public Queue() {
        this.first = null;
        this.last = null;
        this.length = 0;
    }

    /**
     * Verifica si la cola está vacía.
     * @return true si está vacía, false en caso contrario.
     */
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * Retorna el tamaño de la cola.
     * @return Número de elementos en la cola.
     */
    public int getLength() {
        return length;
    }

    /**
     * Inserta un elemento al final de la cola 
     * @param data El dato que se va a agregar.
     */
    public void enqueue(T data) {
        Nodo<T> nuevoNodo = new Nodo<>(data);
        if (isEmpty()) {
            first = nuevoNodo;
            last = nuevoNodo;
        } else {
            last.setNext(nuevoNodo);
            last = nuevoNodo;
        }
        length++;
    }

    /**
     * Elimina y retorna el elemento del frente de la cola 
     * @return El dato eliminado o null si la cola está vacía.
     */
    public T dequeue() {
        if (isEmpty()) {
            System.out.println("La cola está vacía.");
            return null;
        }
        T data = first.getData();
        first = first.getNext();
        length--;
        if (length == 0) { // Si la cola quedó vacía, reiniciar last a null
            last = null;
        }
        return data;
    }

    /**
     * Retorna el elemento del frente sin eliminarlo (peek).
     * @return El dato en el frente de la cola o null si la cola está vacía.
     */
    public T peek() {
        if (isEmpty()) {
            System.out.println("La cola está vacía.");
            return null;
        }
        return first.getData();
    }

    /**
     * Imprime los elementos de la cola.
     */
    public void imprimir() {
        if (isEmpty()) {
            System.out.println("La cola está vacía.");
            return;
        }
        Nodo<T> actual = first;
        while (actual != null) {
            System.out.print(actual.getData() + " -> ");
            actual = actual.getNext();
        }
        System.out.println("NULL");
    }
}
