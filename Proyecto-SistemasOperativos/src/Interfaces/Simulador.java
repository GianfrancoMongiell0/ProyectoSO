/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Interfaces;

import Clases.CPU;
import Clases.Proceso;
import Clases.SistemaOperativo;
import Estructuras.Lista;
import Estructuras.Nodo;
import Estructuras.Queue;
import Utils.LectorEscritorTxt;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import planificacion.*;

/**
 *
 * @author LENOVO
 */
public class Simulador extends javax.swing.JFrame {

    String rutaArchivo = "PruebaJava.txt";
    LectorEscritorTxt lector = new LectorEscritorTxt();
    private Queue<Proceso> colaListos = new Queue<>();
    private Queue<Proceso> colaBloqueados = new Queue<>();
    private Queue<Proceso> colaTerminados = new Queue<>();
    private SistemaOperativo sistemaOperativo;
    private JLabel[] estadosCPU;

    private DefaultTableModel modeloTablaListos = new DefaultTableModel(
            new Object[][]{}, new String[]{"Id", "Nombre", "Estado", "PC", "MAR"}
    );

    private DefaultTableModel modeloTablaBloqueados = new DefaultTableModel(
            new Object[][]{}, new String[]{"Id", "Nombre", "Estado", "PC", "MAR"}
    );

    private DefaultTableModel modeloTablaTerminados = new DefaultTableModel(
            new Object[][]{}, new String[]{"Id", "Nombre", "Estado", "PC", "MAR"}
    );

    public Simulador() {
        initComponents();
        setTitle("Simulador de planificacion de procesos");
        ColaListos.setModel(modeloTablaListos);
        ColaBloqueados.setModel(modeloTablaBloqueados);
        ColaTerminados.setModel(modeloTablaTerminados);
    }

    public void agregarProceso(Proceso proceso) {
        if (sistemaOperativo != null) {
            sistemaOperativo.agregarProceso(proceso);
        }
        actualizarTablas();
    }

    public void setColaBloqueados(Queue<Proceso> colaBloqueados) {
        this.colaBloqueados = colaBloqueados;
    }

    public void setColaTerminados(Queue<Proceso> colaTerminados) {
        this.colaTerminados = colaTerminados;
    }

    public void setColaListos(Queue<Proceso> colaListos) {
        this.colaListos = colaListos;
    }

    public void actualizarTablas() {
        actualizarTabla(colaListos, modeloTablaListos);
        actualizarTabla(colaBloqueados, modeloTablaBloqueados);
        actualizarTabla(colaTerminados, modeloTablaTerminados);
    }

    private void actualizarTabla(Queue<Proceso> cola, DefaultTableModel modelo) {
        modelo.setRowCount(0);

        if (cola.isEmpty()) {
            return;
        }
        Queue<Proceso> copiaCola = new Queue<>();

        // Recorremos la cola sin modificarla
        Nodo<Proceso> actual = cola.getFirst(); // Obtener el primer nodo
        while (actual != null) {
            Proceso proceso = actual.getData(); // Obtener el proceso del nodo

            Object[] fila = {
                proceso.getPCB().getId(),
                proceso.getPCB().getNombre(),
                proceso.getPCB().getEstado(),
                proceso.getPCB().getPc(),
                proceso.getPCB().getMar()
            };
            modelo.addRow(fila);

            // Guardamos en la copia para no perder los elementos
            copiaCola.enqueue(proceso);

            actual = actual.getNext(); // Avanzar al siguiente nodo
        }

        // Restaurar la cola original
        cola.clear();
        while (!copiaCola.isEmpty()) {
            cola.enqueue(copiaCola.dequeue());
        }
    }

    public void actualizarEstadoCPU(int idCPU, Proceso proceso) {
        SwingUtilities.invokeLater(() -> { // Asegurar actualización en el hilo de la UI
            if (proceso == null) {
                limpiarEstadoCPU(idCPU);
                return;
            }

            if (idCPU == 1) {
                estCPU1.setText("Ejecutando");
                idP1.setText("ID: " + String.valueOf(proceso.getPCB().getId()));
                nombreP1.setText("Nombre: " + proceso.getPCB().getNombre());
                statusP1.setText("Estatus: " + proceso.getPCB().getEstado().toString());
                pcP1.setText("PC: " + String.valueOf(proceso.getPCB().getPc()));
                marP1.setText("MAR: " + String.valueOf(proceso.getPCB().getMar()));
            } else if (idCPU == 2) {
                estCPU2.setText("Ejecutando");
                idP2.setText("ID: " + String.valueOf(proceso.getPCB().getId()));
                nombreP2.setText("Nombre: " + proceso.getPCB().getNombre());
                statusP2.setText("Estatus: " + proceso.getPCB().getEstado().toString());
                pcP2.setText("PC: " + String.valueOf(proceso.getPCB().getPc()));
                marP2.setText("MAR: " + String.valueOf(proceso.getPCB().getMar()));
            } else if (idCPU == 3) {
                estCPU3.setText("Ejecutando");
                idP3.setText("ID: " + String.valueOf(proceso.getPCB().getId()));
                nombreP3.setText("Nombre: " + proceso.getPCB().getNombre());
                statusP3.setText("Estatus: " + proceso.getPCB().getEstado().toString());
                pcP3.setText("PC: " + String.valueOf(proceso.getPCB().getPc()));
                marP3.setText("MAR: " + String.valueOf(proceso.getPCB().getMar()));
            }
        });
    }

    // Método para limpiar los labels cuando un CPU queda sin proceso
    public void limpiarEstadoCPU(int idCPU) {
        SwingUtilities.invokeLater(() -> {
            if (idCPU == 1) {
                estCPU1.setText("CPU Libre");
                idP1.setText("ID: -");
                nombreP1.setText("Nombre: -");
                statusP1.setText("Estatus: -");
                pcP1.setText("PC: -");
                marP1.setText("MAR: -");
            } else if (idCPU == 2) {
                estCPU2.setText("CPU Libre");
                idP2.setText("ID: -");
                nombreP2.setText("Nombre: -");
                statusP2.setText("Estatus: -");
                pcP2.setText("PC: -");
                marP2.setText("MAR: -");
            } else if (idCPU == 3) {
                estCPU3.setText("CPU Libre");
                idP3.setText("ID: -");
                nombreP3.setText("Nombre: -");
                statusP3.setText("Estatus: -");
                pcP3.setText("PC: -");
                marP3.setText("MAR: -");
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        nombreProceso = new javax.swing.JTextField();
        cantInstrucciones = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        tipo = new javax.swing.JComboBox<>();
        cantCiclosSE = new javax.swing.JSpinner();
        cantCiclosGE = new javax.swing.JSpinner();
        crearProceso = new javax.swing.JButton();
        cargarProceso = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        cargarProceso1 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        algoritmoPlanificacion = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cpus2 = new javax.swing.JRadioButton();
        cpus3 = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        botonSaveconfig = new javax.swing.JButton();
        duracionCE = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cpussoo = new javax.swing.JLabel();
        ciclosdereloj = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        ColaListos = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        ColaBloqueados = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        ColaTerminados = new javax.swing.JTable();
        algoritmoPlan = new javax.swing.JLabel();
        cpusAct = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        estCPU1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        idP1 = new javax.swing.JLabel();
        nombreP1 = new javax.swing.JLabel();
        statusP1 = new javax.swing.JLabel();
        pcP1 = new javax.swing.JLabel();
        marP1 = new javax.swing.JLabel();
        enEjP1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        estCPU2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        idP2 = new javax.swing.JLabel();
        nombreP2 = new javax.swing.JLabel();
        statusP2 = new javax.swing.JLabel();
        pcP2 = new javax.swing.JLabel();
        marP2 = new javax.swing.JLabel();
        enEjP2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        estCPU3 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        idP3 = new javax.swing.JLabel();
        nombreP3 = new javax.swing.JLabel();
        statusP3 = new javax.swing.JLabel();
        pcP3 = new javax.swing.JLabel();
        marP3 = new javax.swing.JLabel();
        enEjP3 = new javax.swing.JLabel();
        cpusAct1 = new javax.swing.JLabel();
        valorCicloReloj = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 153, 102));

        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane1.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N

        jPanel4.setBackground(new java.awt.Color(0, 153, 102));

        jPanel7.setBackground(new java.awt.Color(122, 186, 143));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Crear Proceso", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel7.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Nombre:");

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Cantidad de instrucciones:");

        jLabel13.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Tipo:");

        jLabel15.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Cant. ciclos para generar ");

        nombreProceso.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        nombreProceso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombreProcesoActionPerformed(evt);
            }
        });

        cantInstrucciones.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cantInstrucciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cantInstruccionesActionPerformed(evt);
            }
        });
        cantInstrucciones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cantInstruccionesKeyTyped(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("excepción:");

        jLabel17.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Cant. ciclos para satisfacer");

        jLabel18.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("excepción:");

        tipo.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        tipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CPU bound", "I/O bound" }));
        tipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoActionPerformed(evt);
            }
        });

        cantCiclosSE.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cantCiclosSE.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        cantCiclosGE.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cantCiclosGE.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        crearProceso.setBackground(new java.awt.Color(0, 153, 102));
        crearProceso.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        crearProceso.setForeground(new java.awt.Color(255, 255, 255));
        crearProceso.setText("Crear proceso");
        crearProceso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crearProcesoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(39, 39, 39)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tipo, javax.swing.GroupLayout.Alignment.TRAILING, 0, 108, Short.MAX_VALUE)
                    .addComponent(cantCiclosGE)
                    .addComponent(cantInstrucciones)
                    .addComponent(nombreProceso)
                    .addComponent(cantCiclosSE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(crearProceso)
                .addGap(87, 87, 87))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(nombreProceso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(cantInstrucciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addGap(12, 12, 12)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(cantCiclosSE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(cantCiclosGE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(crearProceso)
                .addGap(18, 18, 18))
        );

        cargarProceso.setBackground(new java.awt.Color(122, 186, 143));
        cargarProceso.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        cargarProceso.setForeground(new java.awt.Color(255, 255, 255));
        cargarProceso.setText("Cargar procesos escritos en txt");
        cargarProceso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cargarProcesoActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Se cargaran procesos que estaban guardados en un txt ");

        jLabel19.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("y la última configuración del simulador");

        cargarProceso1.setBackground(new java.awt.Color(122, 186, 143));
        cargarProceso1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        cargarProceso1.setForeground(new java.awt.Color(255, 255, 255));
        cargarProceso1.setText("Cargar procesos aleatorios");
        cargarProceso1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cargarProceso1ActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Se cargaran 20 procesos que se generan aleatoriamente");

        jLabel21.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("y la última configuración del simulador");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(249, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(250, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(307, 307, 307)
                .addComponent(cargarProceso1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(292, 292, 292)
                .addComponent(cargarProceso, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cargarProceso)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addGap(18, 18, 18)
                .addComponent(cargarProceso1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21)
                .addContainerGap(155, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Proceso", jPanel4);

        jPanel1.setBackground(new java.awt.Color(0, 153, 102));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.setPreferredSize(new java.awt.Dimension(900, 900));

        jPanel2.setBackground(new java.awt.Color(122, 186, 143));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Configuración de simulación", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Algoritmo de planificación:");

        algoritmoPlanificacion.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        algoritmoPlanificacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "FCFS", "Round Robin", "SJF", "SRT", "HRRN" }));
        algoritmoPlanificacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                algoritmoPlanificacionActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("CPU´s Activos");

        buttonGroup1.add(cpus2);
        cpus2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cpus2.setText("2");
        cpus2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cpus2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(cpus3);
        cpus3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cpus3.setText("3");
        cpus3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cpus3ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jLabel3.setText("Duración del ciclo de ejecución:");

        jComboBox1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ms", "s" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        botonSaveconfig.setBackground(new java.awt.Color(0, 153, 102));
        botonSaveconfig.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        botonSaveconfig.setForeground(new java.awt.Color(255, 255, 255));
        botonSaveconfig.setText("Guardar");
        botonSaveconfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSaveconfigActionPerformed(evt);
            }
        });

        duracionCE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                duracionCEActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(cpus2, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(cpus3, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(algoritmoPlanificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1))
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(duracionCE, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(botonSaveconfig)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(algoritmoPlanificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cpus2)
                    .addComponent(cpus3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(duracionCE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(botonSaveconfig)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        cpussoo.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cpussoo.setForeground(new java.awt.Color(255, 255, 255));
        cpussoo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        ciclosdereloj.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        ciclosdereloj.setForeground(new java.awt.Color(255, 255, 255));
        ciclosdereloj.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ciclosdereloj.setText("Ciclos de reloj globales");

        ColaListos.setBackground(new java.awt.Color(122, 186, 143));
        ColaListos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {"", null, null, null, null},
                {null, null, null, null, null},
                {"", null, null, null, null}
            },
            new String [] {
                "Id", "Nombre", "Status", "PC", "MAR"
            }
        ));
        jScrollPane2.setViewportView(ColaListos);

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Cola listos:");

        ColaBloqueados.setBackground(new java.awt.Color(122, 186, 143));
        ColaBloqueados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id", "Nombre", "Status", "PC", "MAR"
            }
        ));
        jScrollPane4.setViewportView(ColaBloqueados);

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Cola bloqueados:");

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Procesos terminados");

        ColaTerminados.setBackground(new java.awt.Color(122, 186, 143));
        ColaTerminados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id", "Nombre", "Status", "PC", "MAR"
            }
        ));
        jScrollPane5.setViewportView(ColaTerminados);

        algoritmoPlan.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        algoritmoPlan.setForeground(new java.awt.Color(255, 255, 255));
        algoritmoPlan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        algoritmoPlan.setText("Algoritmo de planificación");

        cpusAct.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        cpusAct.setForeground(new java.awt.Color(255, 255, 255));
        cpusAct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cpusAct.setText("CPU´s activos");

        jPanel3.setBackground(new java.awt.Color(122, 186, 143));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CPU 1", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        estCPU1.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        estCPU1.setText("Inactivo");

        idP1.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        idP1.setText("ID proceso");

        nombreP1.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        nombreP1.setText("Nombre proceso");

        statusP1.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        statusP1.setText("Status");

        pcP1.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        pcP1.setText("PC");

        marP1.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        marP1.setText("MAR");

        enEjP1.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        enEjP1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        enEjP1.setText("En ejecución: SO/PU");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(estCPU1)
                .addGap(6, 6, 6))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(statusP1)
                            .addComponent(nombreP1))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pcP1)
                            .addComponent(marP1)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(idP1))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(enEjP1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(estCPU1)
                .addGap(2, 2, 2)
                .addComponent(idP1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombreP1)
                    .addComponent(pcP1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusP1)
                    .addComponent(marP1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(enEjP1)
                .addGap(28, 28, 28)
                .addComponent(jLabel9)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(122, 186, 143));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CPU 2", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        estCPU2.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        estCPU2.setText("Inactivo");

        idP2.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        idP2.setText("ID proceso");

        nombreP2.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        nombreP2.setText("Nombre proceso");

        statusP2.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        statusP2.setText("Status");

        pcP2.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        pcP2.setText("PC");

        marP2.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        marP2.setText("MAR");

        enEjP2.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        enEjP2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        enEjP2.setText("En ejecución: SO/PU");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(estCPU2)
                .addGap(6, 6, 6))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(statusP2)
                            .addComponent(nombreP2))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pcP2)
                            .addComponent(marP2)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(idP2))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(enEjP2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(estCPU2)
                .addGap(2, 2, 2)
                .addComponent(idP2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombreP2)
                    .addComponent(pcP2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusP2)
                    .addComponent(marP2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(enEjP2)
                .addGap(28, 28, 28)
                .addComponent(jLabel11))
        );

        jPanel6.setBackground(new java.awt.Color(122, 186, 143));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CPU 3", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        estCPU3.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        estCPU3.setText("Inactivo");

        idP3.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        idP3.setText("ID proceso");

        nombreP3.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        nombreP3.setText("Nombre proceso");

        statusP3.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        statusP3.setText("Status");

        pcP3.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        pcP3.setText("PC");

        marP3.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        marP3.setText("MAR");

        enEjP3.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        enEjP3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        enEjP3.setText("En ejecución: SO/PU");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(estCPU3)
                .addGap(6, 6, 6))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(statusP3)
                            .addComponent(nombreP3))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pcP3)
                            .addComponent(marP3)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(idP3))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(enEjP3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(estCPU3)
                .addGap(2, 2, 2)
                .addComponent(idP3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombreP3)
                    .addComponent(pcP3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusP3)
                    .addComponent(marP3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enEjP3)
                .addGap(34, 34, 34)
                .addComponent(jLabel12))
        );

        cpusAct1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        cpusAct1.setForeground(new java.awt.Color(255, 255, 255));
        cpusAct1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        valorCicloReloj.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        valorCicloReloj.setForeground(new java.awt.Color(255, 255, 255));
        valorCicloReloj.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        valorCicloReloj.setText("Ciclo de reloj global: 0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 216, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(ciclosdereloj))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                    .addComponent(jScrollPane4)
                    .addComponent(jScrollPane5)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(cpusAct1)
                                    .addComponent(cpusAct))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cpussoo, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(algoritmoPlan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(valorCicloReloj, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(34, 34, 34))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(algoritmoPlan, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(valorCicloReloj, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cpusAct, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cpusAct1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cpussoo, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ciclosdereloj, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(317, 317, 317))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jTabbedPane1.addTab("Simulación", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 670, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void cpus3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cpus3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cpus3ActionPerformed

    private void cpus2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cpus2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cpus2ActionPerformed

    private void algoritmoPlanificacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_algoritmoPlanificacionActionPerformed

        // Obtener el algoritmo seleccionado
        String algoritmo = (String) algoritmoPlanificacion.getSelectedItem();

        // Crear el nuevo planificador según el algoritmo seleccionado
        Planificador nuevoPlanificador;
        switch (algoritmo) {
            case "FCFS":
                nuevoPlanificador = new FCFS();
                break;
            case "SJF":
                nuevoPlanificador = new SJF();
                break;
            case "Round Robin":
                nuevoPlanificador = new RoundRobin();
                break;
            case "HRRN":
                nuevoPlanificador = new HRRN();
                break;
            case "SRT":
                nuevoPlanificador = new SRT();
                break;
            default:
                JOptionPane.showMessageDialog(Simulador.this, "Algoritmo no soportado."); // Usar Simulador.this para referenciar la ventana principal
                return;
        }

        // Cambiar el planificador en el sistema operativo
        if (sistemaOperativo != null) {
            sistemaOperativo.setPlanificador(nuevoPlanificador);
        }

        // Reordenar la cola de procesos según el nuevo planificador
        if (sistemaOperativo != null && sistemaOperativo.getPlanificador().getColaListos() != null) {
            
            sistemaOperativo.getPlanificador().reordenarCola(); // Asegúrate de tener este método implementado en cada planificador
            sistemaOperativo.getSimulador().setColaListos(sistemaOperativo.getPlanificador().getColaListos()); // Asumiendo que 'setColaListos' existe en Simulador
        }

        // Actualizar el estado de la interfaz gráfica (opcional)
        algoritmoPlan.setText("Algoritmo Actual: " + algoritmo);
    

    }//GEN-LAST:event_algoritmoPlanificacionActionPerformed

    private void crearProcesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_crearProcesoActionPerformed
        String nombre = nombreProceso.getText();

        int instrucciones = Integer.parseInt(cantInstrucciones.getText());
        String tipoProceso = (String) tipo.getSelectedItem();

        if (tipoProceso.equals("CPU bound")) {
            Proceso proceso = new Proceso(nombre, instrucciones);
            System.out.println("Proceso creado y agregado a la cola de listos y es CPU bound.");
            colaListos.enqueue(proceso);
            agregarProceso(proceso);
            colaListos.imprimir();
            lector.escribirArchivo(rutaArchivo, proceso.getPCB().getNombre() + "," + proceso.getTotalInstrucciones()
                    + "," + proceso.getPCB().esIOBound() + "," + proceso.getPCB().getCiclosExcepcion() + "," + proceso.getPCB().getCiclosCompletarExcepcion(), true);
        } else {
            int ciclosExcepcion = (int) cantCiclosGE.getValue();
            int ciclosResolucion = (int) cantCiclosSE.getValue();
            Proceso proceso = new Proceso(nombre, instrucciones, ciclosExcepcion, ciclosResolucion);
            colaListos.enqueue(proceso);
            agregarProceso(proceso);
            System.out.println("Proceso creado y agregado a la cola de listos y es IO bound." + colaListos);
            lector.escribirArchivo(rutaArchivo, proceso.getPCB().getNombre() + "," + proceso.getTotalInstrucciones()
                    + "," + proceso.getPCB().esIOBound() + "," + proceso.getPCB().getCiclosExcepcion() + "," + proceso.getPCB().getCiclosCompletarExcepcion(), true);
        }
    }//GEN-LAST:event_crearProcesoActionPerformed

    private void cantInstruccionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cantInstruccionesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cantInstruccionesActionPerformed

    private void cargarProcesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cargarProcesoActionPerformed

        // Leer la configuración del simulador desde configuracion.txt
        try (BufferedReader br = new BufferedReader(new FileReader("configuracion.txt"))) {
            String linea = br.readLine();
            if (linea != null) {
                String[] config = linea.split(",");
                algoritmoPlanificacion.setSelectedItem(config[0]); // Algoritmo
                if (config[1].equals("2")) {
                    cpus2.setSelected(true);
                } else {
                    cpus3.setSelected(true);
                }
                duracionCE.setText(config[2]); // Duración
                jComboBox1.setSelectedItem(config[3].equals("true") ? "ms" : "s"); // Unidad
            }
        } catch (IOException ex) {
            System.out.println("No se encontró el archivo de configuración.");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                String nombre = datos[0];
                int instrucciones = Integer.parseInt(datos[1]);
                String tipo = datos[2];

                if (tipo.equals(false)) {
                    Proceso proceso = new Proceso(nombre, instrucciones);
                    colaListos.enqueue(proceso);
                    agregarProceso(proceso);
                    System.out.println("Procesos cargados desde el archivo." + proceso);
                } else {
                    int ciclosExcepcion = Integer.parseInt(datos[3]);
                    int ciclosResolucion = Integer.parseInt(datos[4]);
                    Proceso proceso = new Proceso(nombre, instrucciones, ciclosExcepcion, ciclosResolucion);
                    colaListos.enqueue(proceso);
                    agregarProceso(proceso);

                    System.out.println("Procesos cargados desde el archivo." + proceso);
                }
            }
            System.out.println("Procesos cargados desde el archivo.");
        } catch (IOException ex) {
            System.out.println("Error al leer el archivo: " + ex.getMessage());
        }

    }//GEN-LAST:event_cargarProcesoActionPerformed

    private void tipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tipoActionPerformed

    private void nombreProcesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombreProcesoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nombreProcesoActionPerformed

    private void cantInstruccionesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cantInstruccionesKeyTyped
        int key = evt.getKeyChar();
        boolean numero = key >= 48 && key <= 57;
        if (!numero) {
            evt.consume();
        }
    }//GEN-LAST:event_cantInstruccionesKeyTyped

    private void botonSaveconfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSaveconfigActionPerformed
        String algoritmo = (String) algoritmoPlanificacion.getSelectedItem();
        int numeroCPUs = cpus2.isSelected() ? 2 : 3;
        int duracion = Integer.valueOf(duracionCE.getText());
        boolean enMilisegundos = jComboBox1.getSelectedItem().equals("ms");
        int duracionMs = enMilisegundos ? duracion : duracion * 1000;

        algoritmoPlan.setText("Algoritmo Actual: " + algoritmo);
        cpusAct.setText("CPUs Activos: " + numeroCPUs);

        // Guardar la configuración en el archivo usando la clase LectorEscritorTxt
        String rutaConfig = "configuracion.txt";
        String configuracion = algoritmo + "," + numeroCPUs + "," + duracion + "," + enMilisegundos;

        lector.escribirArchivo(rutaConfig, configuracion, false);

        // Crear el planificador basado en el algoritmo seleccionado
        Planificador planificador;
        switch (algoritmo) {
            case "FCFS":
                planificador = new FCFS();
                break;
            case "SJF":
                planificador = new SJF();
                break;
            case "Round Robin":
                planificador = new RoundRobin();
                break;
            case "HRRN":
                planificador = new HRRN();
                break;
            case "SRT":
                planificador = new SRT();
                break;
            default:
                JOptionPane.showMessageDialog(this, "Algoritmo no soportado.");
                return;
        }
        JLabel[] labelsEstado = {enEjP1, enEjP2, enEjP3};
        // Crear el sistema operativo y configurar CPUs
        sistemaOperativo = new SistemaOperativo(planificador, numeroCPUs, colaListos, labelsEstado);
        sistemaOperativo.setDuracionCiclo(duracionMs);
        sistemaOperativo.setSimulador(this);
        sistemaOperativo.setColaBloqueados(colaBloqueados);
        sistemaOperativo.setColaTerminados(colaTerminados);

        if (!colaListos.isEmpty()) {
            new Thread(() -> { // Ejecutar en un hilo separado
                sistemaOperativo.iniciarCPUs();
                sistemaOperativo.ejecutarSimulacion();
            }).start();
        } else {
            JOptionPane.showMessageDialog(this, "No hay procesos en la cola de listos. Agrega procesos antes de iniciar.");
        }

    }//GEN-LAST:event_botonSaveconfigActionPerformed

    private void cargarProceso1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cargarProceso1ActionPerformed
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            // Generar valores aleatorios para cada atributo del proceso
            String nombre = "PROCESO-" + i;
            int instrucciones = random.nextInt(10) + 5;
            boolean esCPUBound = random.nextBoolean(); // CPU-bound o I/O-bound

            Proceso proceso;
            if (esCPUBound) {
                // Si es CPU-bound, se usa el constructor sin ciclos de excepción/resolución
                proceso = new Proceso(nombre, instrucciones);
                System.out.println("Proceso generado (CPU-bound): " + nombre);
            } else {
                // Si es I/O-bound, se generan ciclos de excepción y resolución
                int ciclosExcepcion = random.nextInt(5) + 1; // Entre 1 y 5 ciclos de excepción
                int ciclosResolucion = random.nextInt(5) + 1; // Entre 1 y 5 ciclos de resolución
                proceso = new Proceso(nombre, instrucciones, ciclosExcepcion, ciclosResolucion);
                System.out.println("Proceso generado (I/O-bound): " + nombre);
            }

            // Agregar el proceso a la cola de listos y actualizar la tabla
            colaListos.enqueue(proceso);
            agregarProceso(proceso);
        }
        // Leer la configuración del simulador desde configuracion.txt
        try (BufferedReader br = new BufferedReader(new FileReader("configuracion.txt"))) {
            String linea = br.readLine();
            if (linea != null) {
                String[] config = linea.split(",");
                algoritmoPlanificacion.setSelectedItem(config[0]); // Algoritmo
                if (config[1].equals("2")) {
                    cpus2.setSelected(true);
                } else {
                    cpus3.setSelected(true);
                }
                String valorDuracion = config[2]; // Obtén el valor como String
                int duracion = Integer.parseInt(valorDuracion); // Convierte a int
                duracionCE.setText(Integer.toString(duracion));
                jComboBox1.setSelectedItem(config[3].equals("true") ? "ms" : "s"); // Unidad
            }
        } catch (IOException ex) {
            System.out.println("No se encontró el archivo de configuración.");
        }

    }//GEN-LAST:event_cargarProceso1ActionPerformed

    private void duracionCEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_duracionCEActionPerformed
        try {
            // Obtener el valor del JTextField
            int duracion = Integer.parseInt(duracionCE.getText().trim());

            // Comprobar la unidad seleccionada
            boolean enMilisegundos = jComboBox1.getSelectedItem().equals("ms");
            int duracionMs = enMilisegundos ? duracion : duracion * 1000;

            // Establecer la duración en el sistema operativo
            sistemaOperativo.setDuracionCiclo(duracionMs);

            // Opcional: mostrar un mensaje de confirmación
            JOptionPane.showMessageDialog(this, "Duración del ciclo actualizada a: " + duracionMs + " ms");
        } catch (NumberFormatException e) {
            // Manejo de errores si la entrada no es un número válido
            JOptionPane.showMessageDialog(this, "Por favor, ingresa un número válido para la duración.");
        }

    }//GEN-LAST:event_duracionCEActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Simulador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Simulador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Simulador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Simulador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Simulador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable ColaBloqueados;
    private javax.swing.JTable ColaListos;
    private javax.swing.JTable ColaTerminados;
    private javax.swing.JLabel algoritmoPlan;
    private javax.swing.JComboBox<String> algoritmoPlanificacion;
    private javax.swing.JButton botonSaveconfig;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JSpinner cantCiclosGE;
    private javax.swing.JSpinner cantCiclosSE;
    private javax.swing.JTextField cantInstrucciones;
    private javax.swing.JButton cargarProceso;
    private javax.swing.JButton cargarProceso1;
    private javax.swing.JLabel ciclosdereloj;
    private javax.swing.JRadioButton cpus2;
    private javax.swing.JRadioButton cpus3;
    private javax.swing.JLabel cpusAct;
    private javax.swing.JLabel cpusAct1;
    private javax.swing.JLabel cpussoo;
    private javax.swing.JButton crearProceso;
    private javax.swing.JTextField duracionCE;
    private javax.swing.JLabel enEjP1;
    private javax.swing.JLabel enEjP2;
    private javax.swing.JLabel enEjP3;
    private javax.swing.JLabel estCPU1;
    private javax.swing.JLabel estCPU2;
    private javax.swing.JLabel estCPU3;
    private javax.swing.JLabel idP1;
    private javax.swing.JLabel idP2;
    private javax.swing.JLabel idP3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel marP1;
    private javax.swing.JLabel marP2;
    private javax.swing.JLabel marP3;
    private javax.swing.JLabel nombreP1;
    private javax.swing.JLabel nombreP2;
    private javax.swing.JLabel nombreP3;
    private javax.swing.JTextField nombreProceso;
    private javax.swing.JLabel pcP1;
    private javax.swing.JLabel pcP2;
    private javax.swing.JLabel pcP3;
    private javax.swing.JLabel statusP1;
    private javax.swing.JLabel statusP2;
    private javax.swing.JLabel statusP3;
    private javax.swing.JComboBox<String> tipo;
    public javax.swing.JLabel valorCicloReloj;
    // End of variables declaration//GEN-END:variables
}
