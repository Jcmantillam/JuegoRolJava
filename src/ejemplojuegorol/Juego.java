/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejemplojuegorol;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Camilo
 */
public class Juego extends Canvas implements Runnable {
    private final long serialVersionUID = 1L;
    private static int aps = 0;
    private static int fps = 0;
    
    private static final int ALTO = 600;
    private static final int ANCHO = 800;
    private static JFrame ventana;
    
    private static String NOMBRE = "Juego";
    
    private static Thread hilo;
    
    private static volatile boolean enFuncionamiento=false;
    
    
    private Juego(){
        setPreferredSize(new Dimension(ANCHO,ALTO));
        ventana = new JFrame(NOMBRE);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setResizable(false);
        ventana.setLayout(new BorderLayout());
        ventana.add(this,BorderLayout.CENTER);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }
    
    public static void  main(String[] args){
        Juego juego = new Juego();
        juego.iniciar();
    }  
    
    private synchronized void iniciar(){
        enFuncionamiento = true;
        hilo = new Thread(this, "Graficos");
        hilo.start();
    }
    
    private synchronized void detener(){
        enFuncionamiento = false;
        try {
            hilo.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void actualizar(){
        aps++;
    }
    
    private void mostrar(){
        fps++;
    }
    
    @Override
    public void run() {   
        final int NanoSegundosPorSegundo = 1000000000;
        
        final byte ActualizacionesPorSegundo = 60;
        
        final double NanosegundosPorActualizacion= NanoSegundosPorSegundo/ActualizacionesPorSegundo;
        
        long refActualizacion = System.nanoTime();
        long refContador = System.nanoTime();
        
        
        
        double tiempoTranscurrido;
        double delta = 0;
                
        while(enFuncionamiento){
            final long inicioBucle = System.nanoTime();
            
            tiempoTranscurrido = inicioBucle - refActualizacion;
            refActualizacion = inicioBucle;
            
            delta += tiempoTranscurrido/NanosegundosPorActualizacion;
            
            while(delta >= 1){
                actualizar();
                delta--;
            }            
            mostrar();
            if(System.nanoTime() - refContador > NanoSegundosPorSegundo){
                ventana.setTitle(NOMBRE + " " + "APS: " + aps + " FPS: " + fps);
                aps = 0;
                fps = 0;
                refContador = System.nanoTime();
            } 
            
        }
    }
}
