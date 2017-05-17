/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dgame;

import java.awt.AlphaComposite;
import objects.Drawable;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import objects.Menu;
import objects.UI;
import utilities.ImageLoader;

/**
 *
 * @author joel_
 */
public class Screen extends JPanel{
    public int fps = 0;
    public static int WIDTH, HEIGHT;

    private UI ui;
    private ArrayList<Drawable> drawables;
    
    private Image gameover_image;
    private int gameover_alpha = 0;
    
    private Menu menu;
    private Menu pausedMenu;

    private boolean press = false;
    private int mousex = 0;
    private int mousey = 0;
    
    
    private boolean gameOver = false, inMenu = false, inGame = true;
    private int gameover_alpha_counter = 0;
    
    private Image player_hit;
    private float hit_alpha = 2;
    private boolean paused;
    
    public Screen(Dimension d, UI ui){
        WIDTH = d.width;
        HEIGHT = d.height;
        this.ui = ui;
        try {
            this.gameover_image = new ImageLoader("GameOver.png").image;
            this.player_hit = new ImageLoader("Player_hit.png").image;
        } catch (IOException ex) {
            Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, null, ex);
        }
        menu = new Menu(d, Menu.MAIN);
        pausedMenu = new Menu(d, Menu.PAUSED);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g2);
        
        if(gameOver){
            g2.drawImage(gameover_image, 0, 0, null);
            if(gameover_alpha > 255) gameover_alpha = 255;
            else if(gameover_alpha < 0) gameover_alpha = 0;
            g2.setColor(new Color(0, 0, 0, gameover_alpha));
            if(gameover_alpha_counter > 3){
                gameover_alpha++;
                gameover_alpha_counter = 0;
            }
            if(gameover_alpha == 255){
                gameOver = false;
                inMenu = true;
            }
            gameover_alpha_counter++;
            g2.fillRect(0, 0, WIDTH, HEIGHT);
        }
        else if(inMenu){
            menu.draw(g);
        }
        else if(inGame){
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            
            if(drawables != null){
                for(Drawable d: drawables){
                    d.draw(g);
                }
            }
            
            ui.draw(g);

            if(hit_alpha < 2){
                int rule = AlphaComposite.SRC_OVER;
                Composite comp = null;
                if(hit_alpha < 1){
                    comp = AlphaComposite.getInstance(rule, hit_alpha);
                }else{
                    comp = AlphaComposite.getInstance(rule, 1+(1-hit_alpha));
                }
                hit_alpha += 0.1;
                g2.setComposite(comp);
                g2.drawImage(player_hit, 0, 0, null);
            }
            
            if(paused){
                g2.setColor(new Color(0,0,0, 50));
                g2.fillRect(0,0, WIDTH, HEIGHT);
                
                pausedMenu.draw(g);
            }
            
            g.setColor(Color.WHITE);
            g.drawString("FPS: " + fps, 27, 35);
            g2.dispose();
        }
        
    }
    
    public void setFps(int fps){
        this.fps = fps;
    }
    
    public void setDrawables(ArrayList<Drawable> list){
        drawables = list;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
        this.inMenu = gameOver;
        this.inGame = !gameOver;
    }

    void playerHit() {
        this.hit_alpha = 0f;
    }
    
    public boolean gameOverFaded(){
        return gameover_alpha >= 255;
    }

    void setPaused(boolean b) {
        this.paused = b;
    }
    
    void setInMenu(boolean b){
        this.inMenu = b;
    }
    
}
