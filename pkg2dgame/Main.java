/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dgame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import objects.UI;

/**
 *
 * @author joel_
 */
public class Main extends JFrame{
    private static int WIDTH = 1920, HEIGHT = WIDTH/16*9;
    private int scale = 2;
    private int blockSize = 20*scale;
    
    public static void main(String[] args) {
        new Main();
    }
    
    public Main(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();
        
        this.setLocation(screens[1].getDefaultConfiguration().getBounds().x, this.getY());
        Dimension d = new Dimension(WIDTH, HEIGHT);
        this.setMinimumSize(d);
        //Fullscreen
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        this.setUndecorated(true);
        //Fullscreen
        this.setVisible(true);
        this.setResizable(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBackground(Color.LIGHT_GRAY);
        
        UI ui = new UI(d, blockSize);
        Screen s = new Screen(d, ui);
        s.setSize(d);
        this.add(s);
        this.pack();
        Game g = new Game(this, s, ui, blockSize);
    }
    
    public static int getGameWidth(){
        return WIDTH;
    }
    public static int getGameHeight(){
        return HEIGHT;
    }
    
}
