/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

/**
 *
 * @author joel_
 */
public class Button extends Rectangle{
    private Image image;
    private int action;
    
    public static final int CONTINUE = 1;
    public static final int PLAY = 2;
    public static final int SETTINGS = 3;
    public static final int HIGHSCORE = 4;
    public static final int RESUME = 5;
    public static final int EXIT = 6;
    
    public Button(int x, int y, Image i, int action){
        this.setBounds(x, y, (int)i.getWidth(null), (int)i.getHeight(null));
        this.image = i;
        this.action = action;
    }
    
    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        
        g2.drawImage(image, x, y, null);
    }

    public int getAction() {
        return action;
    }
    
}
