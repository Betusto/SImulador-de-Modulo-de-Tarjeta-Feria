/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuferia;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.apache.commons.math3.distribution.ExponentialDistribution;

/**
 *
 * @author Alberto Villarreal
 */
public class Ventana extends javax.swing.JFrame {

    private final int WINDOW_WIDTH = 915;
    private final int WINDOW_HEIGHT = 635;
    /*Inicializacion de variables*/
    float RELOJ = 0;   int COLA = 0;    float TET = 0; //Tiempo de espera total
    float TO = 0; //Tiempo de ocio
    int NCLL = 0, NCA = 0; //Número de clientes llegados y atendidos   
    float TLL = generarVairableDistExpo(); //Tiempo de llegada (distribución Normal)
    float TS = 0; //Tiempo de servicio (distribución exponencial)
    float DELTA = 0;         
    float TLIMITE; //Tiempo limite
    float TEM = 0; //Tiempo de espera promedio
    float NCM = 0; //Numero de clientes promedio
    int estadoSimulacion = 0; //0 significa detenido, 1 significa en proceso
    int detente = 0; //0 significa no te detengas, 1 significa detente
    int numeros = 0; //Numeros a generar
    private volatile boolean exit = false;
    int valorVerdaderoCola = 0;
    int detectaErrores=0;


    public Ventana() {
        initComponents();
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("SimuFeria");
        setResizable(false); //Remueve el boton de maximizar pantalla
        // Specify what happens when close button is clicked.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        imagen0.setVisible(false);
        imagen1.setVisible(false);
        imagen2.setVisible(false);
        imagen3.setVisible(false);
        imagen4.setVisible(false);
        jPanel3.setVisible(false);
        //jPanel1.setBackground(new Color(0,0,0,125));
        //jPanel1.setVisible(false);

    }
    
    public void Iterar(){ 
        Hilo hilo = new Hilo();
        exit = false;
        hilo.start(); //inicia la clase hilo en segundo plano
    }
    
    public float generarVariableDistNorm(){
       float NORM; //Valor de la variable aleatoria normal
       float MEDIA = 29.46f; //Valor de la media, se consigue en las estadisticas descriptivas
       float DESV = 3.67123f; //Valor de la desviacion, se consigue en las estadisticas descriptivas
       float R; //Numero aleatorio
       R = generarNumeroAleatorio();
       NORM=DESV*R+MEDIA; //Usamos la formula de la dist. normal
    return NORM;
    }
    
    public float generarVairableDistExpo(){
        float EXPO; //Valor de la variable aleatoria normal
        float MEDIA = 106.52f; //Valor de la media, se consigue en las estadisticas descriptivas
        float LAMBDA; //Valor en base al contexto        
        LAMBDA = 1/MEDIA; //Conseguimos lambda
        ExponentialDistribution ED = new ExponentialDistribution(MEDIA, LAMBDA);
        EXPO = (float)ED.sample();
        //EXPO = (float)(-Math.log10(R)/LAMBDA); //Usamos la formula de la dist. exponencial
    return EXPO;
    }
    
    //Metodo para generar el numeros aleatorios
    public float generarNumeroAleatorio(){
        float  semilla = 59;   float  multiplicador = 67;   float  constante = 65;    
        float  modulo = 97;   float R = 0;
	for (int i=0; i<numeros; i++){
		R = (semilla*multiplicador + constante)%modulo;
		semilla =R;
                R = R/modulo;
        }
        System.out.println("NUMERO ALEATORIO: "+R);
        return R;  
    }
    
    private class Hilo extends Thread{
    public void run() { //Siempre en minusculas
        //Ejecuciones del hilo
        //TODO: PONER UN DELAY
        Random rand = new Random();
        System.out.println("EL HILO");
        while(!exit){ //hilo solo se ejecuta si la variable exit es falsa
        int regresar = 0; //0=No, 1=Si
        int darVuelta = 0;
        int iteraciones = 0; //numero de iteraciones 
        int opt = 5;
        int x=0;
        do{
        iteraciones++;
        numeros++;
        if(detente != 1){
        regresar = 0;
        //Seleccionar Delta
        if(TS > TLL){ //TLL es menor que TS
            if(TLL >0){
            DELTA = TLL;
            }else{
            DELTA = TS;
            }
            System.out.println("TS ES MAYOR A TLL");
        }else{ //TS es menor que TLL
            if(TS >0){
            DELTA = TS;
            }else{
            DELTA = TLL;
            }
            System.out.println("TS ES MENOR O IGUAL A A TLL, TLL VALE: "+TLL);
        }
        if(DELTA > 0){
            System.out.println("Entro al if del delta");
            RELOJ = RELOJ + DELTA; //Actualización del tiempo
            TET = TET + (COLA * DELTA); //Actualizamos tiempo de espera total
            TLL = TLL - DELTA; //Se ajusta tiempo entre llegadas
            do{
                System.out.println("TIEMPOLLEGADA ANTES DEL IF: "+TLL);
            if(TLL == 0){
                System.out.println("Entro a tll=0");
                int n = rand.nextInt(3); //0-2
                n+=1; //1-3
                if(exit==false){
                NCLL = NCLL + 1; //Aumentamos número de clientes llegados
                COLA = COLA + 1; //Aumentamos la cola  
                        valorVerdaderoCola = NCLL - NCA;
               System.out.println("NCLL VALE"+NCLL+"Y NCA VALE"+NCA);
                /*switch(valorVerdaderoCola){
                    case 0:
                        System.out.println("ENTRO A CASE 0");
                        imagen0.setVisible(false);
                        imagen1.setVisible(false);
                        imagen2.setVisible(false);
                        imagen3.setVisible(false);
                        imagen4.setVisible(false);
                        jPanel3.setVisible(false);
                        imagenExtra.setText("");
                        break;
                    case 1:
                        imagen0.setVisible(true);
                        imagen1.setVisible(false);
                        imagen2.setVisible(false);
                        imagen3.setVisible(false);
                        imagen4.setVisible(false);
                        jPanel3.setVisible(false);
                        break;
                    case 2:
                        imagen0.setVisible(true);
                        imagen1.setVisible(true);
                        imagen2.setVisible(false);
                        imagen3.setVisible(false);
                        imagen4.setVisible(false);
                        jPanel3.setVisible(false);
                        break;
                    case 3:
                        imagen0.setVisible(true);
                        imagen1.setVisible(true);
                        imagen2.setVisible(true);
                        imagen3.setVisible(false);
                        imagen4.setVisible(false);
                        jPanel3.setVisible(false);
                        break;
                    case 4:
                        imagen0.setVisible(true);
                        imagen1.setVisible(true);
                        imagen2.setVisible(true);
                        imagen3.setVisible(true);
                        imagen4.setVisible(false);
                        jPanel3.setVisible(false);
                        break;
                    case 5:
                        imagen0.setVisible(true);
                        imagen1.setVisible(true);
                        imagen2.setVisible(true);
                        imagen3.setVisible(true);
                        imagen4.setVisible(true);
                        jPanel3.setVisible(false);
                        break;
                    default:
                        imagen0.setVisible(true);
                        imagen1.setVisible(true);
                        imagen2.setVisible(true);
                        imagen3.setVisible(true);
                        imagen4.setVisible(true);
                        jPanel3.setVisible(true);
                        int colaStr = COLA-5;
                        imagenExtra.setText("+"+colaStr);
                        break;
                }*/
                }
                System.out.println("SE INCREMENTO COLA: "+COLA);
                TLL = generarVairableDistExpo(); //Generar la proxima llegada mediante variable aleatoria
                System.out.println("TIEMPOLLEGADA: "+TLL);
                darVuelta=1;
            }else{
                darVuelta=0;
                System.out.println("Entro a donde debe imprimir");
                TS = TS - DELTA; //Se ajusta el tiempo de servicio
                if(0>TS){
                    //Incrementamos tiempo de ocio
                    TO = TO - TS;
                    TS = 0;
                }else if(TS==0){
                    NCA = NCA +1; //Aumentamos número de clientes atendidos
                }//else if(TS>0){  
                //}
                if(0>=TS){
                    if(COLA > 0){
                        TS = generarVariableDistNorm(); //Generamos  el tiempo der servicio del sig. cliente
                        /*int n= 0;
                        for(int i = 0; i<randomCheck;i++){
                        n = rand.nextInt(3); //0-2
                        n+=1; //1-3
                        if(COLA<n){
                            randomCheck++;
                        }
                        }*/
                        if(exit==false){
                        COLA = COLA - 1;
                        valorVerdaderoCola = NCLL - NCA;
               System.out.println("NCLL VALE"+NCLL+"Y NCA VALE"+NCA);
                  //jPanel1.setVisible(false);
                         }
                        System.out.println("SE DECREMENTO COLA: "+COLA);
                    }
                }
                //Estado del sistema
            System.out.println("Tiempo de reloj: " + RELOJ);
            System.out.println("Valor de cola: " + COLA);
            System.out.println("Valor de delta: " + DELTA);
            System.out.println("Numero de clientes que llegaron: " + NCLL);
            System.out.println("Numero de clientes atendidos: " + NCA);
            System.out.println("Tiempo Llegada: " + TLL);
            System.out.println("Tiempo Servicio: " + TS);
            System.out.println("Tiempo Ocio: " + TO);
            if(RELOJ >= TLIMITE){
                TEM = TET/NCA;
                NCM = TET/TLIMITE;
                //Variables endogénas
                System.out.println("Tiempo de espera promedio: " + TEM);
                temLabel.setText("Tiempo de Espera Promedio: "+TEM);
                System.out.println("Tiempo de ocio total: " + TO);
                totLabel.setText("Tiempo de Ocio Total: "+TO);
                System.out.println("Tiempo de espera total: " + TET);
                retLabel.setText("Tiempo de Espera Total: "+TET);
                totClientesProm.setText("Número de Clientes Promedio: "+NCM);
                regresar=0;
                     opt = JOptionPane.showOptionDialog(null, "Simulación terminada", "Test", JOptionPane.DEFAULT_OPTION,
        JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    if(opt==0){
                    //Reinicio();
                    exit=true;
                    }
            }else{
                regresar=1;
            }
            }                        try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
            }
                  switch(valorVerdaderoCola){
                    case 0:
                        System.out.println("ENTRO A CASE 0");
                        imagen0.setVisible(false);
                        imagen1.setVisible(false);
                        imagen2.setVisible(false);
                        imagen3.setVisible(false);
                        imagen4.setVisible(false);
                        jPanel3.setVisible(false);
                        imagenExtra.setText("");
                        break;
                    case 1:

                        imagen0.setVisible(true);
                        imagen1.setVisible(false);
                        imagen2.setVisible(false);
                        imagen3.setVisible(false);
                        imagen4.setVisible(false);
                        jPanel3.setVisible(false);

                        break;
                    case 2:
                        imagen0.setVisible(true);
                        imagen1.setVisible(true);
                        imagen2.setVisible(false);
                        imagen3.setVisible(false);
                        imagen4.setVisible(false);
                        jPanel3.setVisible(false);
                        break;
                    case 3:
                        imagen0.setVisible(true);
                        imagen1.setVisible(true);
                        imagen2.setVisible(true);
                        imagen3.setVisible(false);
                        imagen4.setVisible(false);
                        jPanel3.setVisible(false);
                        break;
                    case 4:
                        imagen0.setVisible(true);
                        imagen1.setVisible(true);
                        imagen2.setVisible(true);
                        imagen3.setVisible(true);
                        imagen4.setVisible(false);
                        jPanel3.setVisible(false);
                        break;
                    case 5:
                        imagen0.setVisible(true);
                        imagen1.setVisible(true);
                        imagen2.setVisible(true);
                        imagen3.setVisible(true);
                        imagen4.setVisible(true);
                        jPanel3.setVisible(false);
                        break;
                    default:
                        imagen0.setVisible(true);
                        imagen1.setVisible(true);
                        imagen2.setVisible(true);
                        imagen3.setVisible(true);
                        imagen4.setVisible(true);
                        jPanel3.setVisible(true);
                        int colaStr = COLA-5;
                        imagenExtra.setText("+"+colaStr);
                        break;
                }
            }while(TLL != 0 && exit==false && darVuelta == 1); //Si tiempo de llegada vale 0 se regresara
        }else{
            System.out.println("Delta es menor o igual a 0 con un valor de: "+DELTA);
        }
        }else{
        regresar=0;
        }
        //Añadimos a la tabla los valores de la iteración
        if(opt!=0 && exit==false){
        valorVerdaderoCola = NCLL - NCA;
        if(valorVerdaderoCola==0){
            System.out.println("Lo hizo invisible jaja");
            imagen0.setVisible(false);
        }
        Object[] row = {iteraciones, RELOJ+"s", valorVerdaderoCola, DELTA+"s", NCLL, NCA, TLL+"s", TS+"s", TO+"s"};
        DefaultTableModel model = (DefaultTableModel) tablaConsola.getModel();
        x=1;
        model.addRow(row); //Lo añadimos

        }
        
       }while(regresar==1 && exit==false);
                if(valorVerdaderoCola==0){
            System.out.println("Lo hizo invisible jaja");
            imagen0.setVisible(false);
        }}
                if(valorVerdaderoCola==0){
            System.out.println("Lo hizo invisible jaja");
            imagen0.setVisible(false);
        }
    }
    }
    
    //Evitar que el usuario escriba cualquier cosa en vez de un número
    public void Validar(){
        detectaErrores=0;
        String tLimite = tiempoTextField.getText();
        if(tLimite.isEmpty()){
            detectaErrores=1;
        }else{
            try {
            Integer.parseInt(tLimite);
            System.out.println("An integer");
            }
        catch (NumberFormatException e) {
             detectaErrores=1;
            }
        }
    }

    
    public void Reinicio(){
            exit = true;
            detente = 1;
            simulacionButton.setText("Iniciar Simulación");
            estadoSimulacion = 0;
            //Reiniciamos variables
            RELOJ = 0;    COLA = 0;     TET = 0; //Tiempo de espera total
            TO = 0; //Tiempo de ocio
            NCLL = 0; NCA = 0; //Número de clientes llegados y atendidos   
            TLL = generarVairableDistExpo(); //Tiempo de llegada (distribución Normal)
            TS = 0; //Tiempo de servicio (distribución exponencial)
            DELTA = 0;         
            TEM = 0; //Tiempo de espera promedio
            NCM = 0;
            numeros = 0;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel = new javax.swing.JPanel();
        consolaPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaConsola = new javax.swing.JTable();
        simulacionButton = new javax.swing.JButton();
        datosPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        tiempoTextField = new javax.swing.JTextField();
        totClientesProm = new javax.swing.JLabel();
        totLabel = new javax.swing.JLabel();
        retLabel = new javax.swing.JLabel();
        temLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        salirButton = new javax.swing.JButton();
        CréditosButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        imagen4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        imagenExtra = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        imagen0 = new javax.swing.JLabel();
        imagen2 = new javax.swing.JLabel();
        imagen3 = new javax.swing.JLabel();
        imagen1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        setSize(new java.awt.Dimension(909, 605));

        jPanel.setPreferredSize(new java.awt.Dimension(909, 605));
        jPanel.setLayout(null);

        consolaPanel.setBackground(new java.awt.Color(255, 255, 255));
        consolaPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        tablaConsola.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new Object [] {
                "<html><center><font color=blue>Iteración<br> <br> </font></center></html>",
                "<html><center><font color=blue>Reloj<br> <br> </font></center></html>",
                "<html><center><font color=blue>Cola<br> <br> </font></center></html>", "<html><center><font color=blue>Delta<br> <br> </font></center></html>",
                "<html><center><font color=blue>Número de clientes<br>que llegaron<br> </font></center></html>", "<html><center><font color=blue>Número de clientes<br>atendidos<br> </font></center></html>",
                "<html><center><font color=blue>Tiempo de<br>llegada<br> </font></center></html>", "<html><center><font color=blue>Tiempo de<br>servicio<br> </font></center></html>",
                "<html><center><font color=blue>Tiempo de<br>Ocio<br> </font></center></html>"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                ,java.lang.String.class};
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaConsola);

        javax.swing.GroupLayout consolaPanelLayout = new javax.swing.GroupLayout(consolaPanel);
        consolaPanel.setLayout(consolaPanelLayout);
        consolaPanelLayout.setHorizontalGroup(
            consolaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        consolaPanelLayout.setVerticalGroup(
            consolaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, consolaPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel.add(consolaPanel);
        consolaPanel.setBounds(0, 423, 909, 182);

        simulacionButton.setText("Iniciar Simulación");
        simulacionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simulacionButtonActionPerformed(evt);
            }
        });
        jPanel.add(simulacionButton);
        simulacionButton.setBounds(709, 72, 187, 48);

        datosPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        datosPanel.setForeground(new java.awt.Color(204, 204, 204));
        datosPanel.setLayout(null);

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setText("Tiempo a Simular (en segundos):");
        datosPanel.add(jLabel6);
        jLabel6.setBounds(340, 40, 290, 22);

        tiempoTextField.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tiempoTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tiempoTextFieldActionPerformed(evt);
            }
        });
        datosPanel.add(tiempoTextField);
        tiempoTextField.setBounds(430, 80, 91, 23);

        totClientesProm.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        totClientesProm.setText("Número de Clientes Promedio: ");
        datosPanel.add(totClientesProm);
        totClientesProm.setBounds(10, 90, 500, 30);

        totLabel.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        totLabel.setText("Tiempo de Ocio Total: ");
        datosPanel.add(totLabel);
        totLabel.setBounds(10, 0, 490, 30);

        retLabel.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        retLabel.setText("Tiempo de Espera Total: ");
        datosPanel.add(retLabel);
        retLabel.setBounds(10, 30, 520, 30);

        temLabel.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        temLabel.setText("Tiempo de Espera Promedio: ");
        datosPanel.add(temLabel);
        temLabel.setBounds(10, 60, 500, 30);

        jPanel.add(datosPanel);
        datosPanel.setBounds(10, 72, 640, 120);

        jLabel1.setFont(new java.awt.Font("Maiandra GD", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 204, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("SIMULACIÓN DE UN MÓDULO DE TARJETA FERIA");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel.add(jLabel1);
        jLabel1.setBounds(60, 0, 780, 30);

        jLabel2.setFont(new java.awt.Font("Maiandra GD", 0, 24)); // NOI18N
        jLabel2.setText("Representación:");
        jPanel.add(jLabel2);
        jLabel2.setBounds(10, 209, 165, 30);

        salirButton.setText("Salir");
        salirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirButtonActionPerformed(evt);
            }
        });
        jPanel.add(salirButton);
        salirButton.setBounds(709, 175, 67, 23);

        CréditosButton.setText("Créditos");
        CréditosButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CréditosButtonActionPerformed(evt);
            }
        });
        jPanel.add(CréditosButton);
        CréditosButton.setBounds(796, 175, 100, 23);

        jLabel3.setFont(new java.awt.Font("Maiandra GD", 0, 24)); // NOI18N
        jLabel3.setText("Datos:");
        jPanel.add(jLabel3);
        jLabel3.setBounds(10, 36, 67, 30);

        jLabel7.setFont(new java.awt.Font("Maiandra GD", 0, 24)); // NOI18N
        jLabel7.setText("Consola:");
        jPanel.add(jLabel7);
        jLabel7.setBounds(10, 390, 90, 30);

        imagen4.setBackground(new java.awt.Color(153, 255, 255));
        imagen4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/man.png"))); // NOI18N
        imagen4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 153)));
        jPanel.add(imagen4);
        imagen4.setBounds(730, 250, 132, 132);

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 132, Short.MAX_VALUE)
        );

        jPanel.add(jPanel2);
        jPanel2.setBounds(140, 250, 20, 132);

        jPanel3.setBackground(new java.awt.Color(0, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 153)));

        imagenExtra.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        imagenExtra.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imagenExtra.setText("+1");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagenExtra, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagenExtra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel.add(jPanel3);
        jPanel3.setBounds(868, 295, 40, 40);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/customer-service.png"))); // NOI18N
        jLabel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel.add(jLabel10);
        jLabel10.setBounds(10, 250, 132, 132);

        imagen0.setBackground(new java.awt.Color(153, 255, 255));
        imagen0.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/man.png"))); // NOI18N
        imagen0.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 153)));
        jPanel.add(imagen0);
        imagen0.setBounds(170, 250, 132, 132);

        imagen2.setBackground(new java.awt.Color(153, 255, 255));
        imagen2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/man.png"))); // NOI18N
        imagen2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 153)));
        jPanel.add(imagen2);
        imagen2.setBounds(450, 250, 132, 132);

        imagen3.setBackground(new java.awt.Color(153, 255, 255));
        imagen3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/man.png"))); // NOI18N
        imagen3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 153)));
        jPanel.add(imagen3);
        imagen3.setBounds(590, 250, 132, 132);

        imagen1.setBackground(new java.awt.Color(153, 255, 255));
        imagen1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/man.png"))); // NOI18N
        imagen1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 153)));
        jPanel.add(imagen1);
        imagen1.setBounds(310, 250, 132, 132);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 130, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 130, Short.MAX_VALUE)
        );

        jPanel.add(jPanel1);
        jPanel1.setBounds(170, 250, 130, 130);

        getContentPane().add(jPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CréditosButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CréditosButtonActionPerformed
        JOptionPane.showOptionDialog(null, "-Créditos-\nDesarrollo:\n*Alberto Villarreal Canales 1746057\n"
                + "Recopilación de datos e investigación:\n*Celso Antonio Anaya Vázquez 1664022\n*Hiram Velázquez Hernández 1838276", "Créditos", JOptionPane.DEFAULT_OPTION,
        JOptionPane.INFORMATION_MESSAGE, null, null, null);
    }//GEN-LAST:event_CréditosButtonActionPerformed

    private void tiempoTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tiempoTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tiempoTextFieldActionPerformed

    private void salirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_salirButtonActionPerformed

    private void simulacionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simulacionButtonActionPerformed
        Validar();
        if(detectaErrores==0){
        if(estadoSimulacion == 0){
            exit = false;
            detente = 0;
            simulacionButton.setText("Detener Simulación");
            estadoSimulacion = 1;
            temLabel.setText("Tiempo de Espera Promedio: ");
            totLabel.setText("Tiempo de Ocio Total: ");
            retLabel.setText("Tiempo de Espera Total: ");
            totClientesProm.setText("Número de Clientes Promedio: ");
            //TODO: VALIDACION
            TLIMITE = Float.parseFloat(tiempoTextField.getText());
            imagen0.setVisible(false);
            imagen1.setVisible(false);
            imagen2.setVisible(false);
            imagen3.setVisible(false);
            imagen4.setVisible(false);
            jPanel3.setVisible(false);
            //jPanel1.setVisible(false);
            imagenExtra.setText("");
            //Reiniciamos la tabla tambien
            tablaConsola.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new Object [] {
                "<html><center><font color=blue>Iteración<br> <br> </font></center></html>",
                "<html><center><font color=blue>Reloj<br> <br> </font></center></html>",
                "<html><center><font color=blue>Cola<br> <br> </font></center></html>", "<html><center><font color=blue>Delta<br> <br> </font></center></html>",
                "<html><center><font color=blue>Número de clientes<br>que llegaron<br> </font></center></html>", "<html><center><font color=blue>Número de clientes<br>atendidos<br> </font></center></html>",
                "<html><center><font color=blue>Tiempo de<br>llegada<br> </font></center></html>", "<html><center><font color=blue>Tiempo de<br>servicio<br> </font></center></html>",
                "<html><center><font color=blue>Tiempo de<br>Ocio<br> </font></center></html>"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                ,java.lang.String.class};
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
            Iterar();
        }else{
            Reinicio();
        }
    }else{
        JOptionPane.showOptionDialog(null, "Escriba un número valido", "", JOptionPane.DEFAULT_OPTION,
        JOptionPane.INFORMATION_MESSAGE, null, null, null);
        }
    }//GEN-LAST:event_simulacionButtonActionPerformed

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
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventana().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CréditosButton;
    private javax.swing.JPanel consolaPanel;
    private javax.swing.JPanel datosPanel;
    private javax.swing.JLabel imagen0;
    private javax.swing.JLabel imagen1;
    private javax.swing.JLabel imagen2;
    private javax.swing.JLabel imagen3;
    private javax.swing.JLabel imagen4;
    private javax.swing.JLabel imagenExtra;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel retLabel;
    private javax.swing.JButton salirButton;
    private javax.swing.JButton simulacionButton;
    private javax.swing.JTable tablaConsola;
    private javax.swing.JLabel temLabel;
    private javax.swing.JTextField tiempoTextField;
    private javax.swing.JLabel totClientesProm;
    private javax.swing.JLabel totLabel;
    // End of variables declaration//GEN-END:variables
}
