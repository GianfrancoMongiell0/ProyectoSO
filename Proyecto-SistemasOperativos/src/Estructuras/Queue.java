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
public class Queue <T>{
    private Lista<T> list;
    public Queue() {
        this.list = new Lista<>();
    }

    /**
     * Agrega un elemento al final de la cola (enqueue)
     * @param data el elemento a agregar
     */
    public void enqueue(T data) {
        list.insertLast(data);
    }

    /**
     * Elimina y retorna el primer elemento de la cola (dequeue)
     * @return el primer elemento de la cola
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("La cola está vacía.");
        }
        T data = list.get(0);
        list.deleteFirst();
        return data;
    }

    /**
     * Retorna el primer elemento de la cola sin eliminarlo (peek)
     * @return el primer elemento de la cola
     */
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("La cola está vacía.");
        }
        return list.get(0);
    }

    /**
     * Verifica si la cola está vacía
     * @return true si está vacía, false en caso contrario
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Retorna el tamaño de la cola
     * @return número de elementos en la cola
     */
    public int size() {
        return list.getLength();
    }
}

