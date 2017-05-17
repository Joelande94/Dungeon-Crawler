/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pkg2dgame.Main;
import utilities.ImageLoader;

/**
 *
 * @author joel_
 */
public class Menu{
    private int WIDTH, HEIGHT;
    
    //Menu type
    public final static int MAIN = 0;
    public final static int PAUSED = 1;
    private int menutype;
    
    private Image menu_background;
    private Image mainmenu_play, mainmenu_settings, mainmenu_highscore, mainmenu_exit;
    private Image paused_resume;
    
    private ArrayList<Button> menu_buttons;
    
    public Menu(Dimension d, int type){
        this.WIDTH = d.width;
        this.HEIGHT = d.height;
        menutype = type;
        
        menu_buttons = new ArrayList<>();
        if(menutype == MAIN){
            try {
                menu_background = new ImageLoader("Menu_background.png").image;
                mainmenu_play = new ImageLoader("Mainmenu_play.png").image;
                mainmenu_settings = new ImageLoader("Mainmenu_settings.png").image;
                mainmenu_highscore = new ImageLoader("Mainmenu_highscore.png").image;
                mainmenu_exit = new ImageLoader("Button_exit.png").image;
            } catch (IOException ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }

            Button play_button = new Button((int)(WIDTH/2-mainmenu_play.getWidth(null)/2), (int)(4*HEIGHT/16), mainmenu_play, Button.PLAY);
            Button settings_button = new Button((int)(WIDTH/2-mainmenu_play.getWidth(null)/2), (int)(7.2*HEIGHT/16), mainmenu_settings, Button.SETTINGS);
            Button highscore_button = new Button((int)(WIDTH/2-mainmenu_play.getWidth(null)/2), (int)(10*HEIGHT/16), mainmenu_highscore, Button.HIGHSCORE);
            Button exit_button     = new Button((int)(WIDTH/2-mainmenu_exit.getWidth(null)/2), (int)(13*HEIGHT/16), mainmenu_exit, Button.EXIT);

            menu_buttons.add(play_button);
            menu_buttons.add(settings_button);
            menu_buttons.add(highscore_button);
            menu_buttons.add(exit_button);
            
        }else if (menutype == PAUSED) {
            try {
                paused_resume = new ImageLoader("Button_resume.png").image;
                mainmenu_settings = new ImageLoader("Mainmenu_settings.png").image;
                mainmenu_exit = new ImageLoader("Button_exit.png").image;
            } catch (IOException ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Button resume_button   = new Button((int)(WIDTH/2-paused_resume.getWidth(null)/2), (int)(4*HEIGHT/16), paused_resume, Button.RESUME);
            Button settings_button = new Button((int)(WIDTH/2-mainmenu_settings.getWidth(null)/2), (int)(7.2*HEIGHT/16), mainmenu_settings, Button.SETTINGS);
            Button exit_button     = new Button((int)(WIDTH/2-mainmenu_exit.getWidth(null)/2), (int)(10*HEIGHT/16), mainmenu_exit, Button.EXIT);
            
            menu_buttons.add(resume_button);
            menu_buttons.add(settings_button);
            menu_buttons.add(exit_button);
        }
        
    }
    
    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(100, 100, 255, 125));
        g2.fillRect(0, 0, Main.getGameWidth(), Main.getGameHeight());
        if(menutype == MAIN) g2.drawImage(menu_background, 0, 0, null);
        
        for(Button b: menu_buttons){
            b.draw(g);
        }
        
    }

    public Iterable<Button> getButtons() {
        return menu_buttons;
    }
}
