/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import pkg2dgame.Game;
import utilities.ImageLoader;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joel_
 */
public class Block extends Drawable{
    private Color color;
    private Image image;
    private int type;
    //TYPE
    public final static int WALL = 1;
    public final static int FLOOR = 2;
    public final static int DOOR = 3;
    //TYPE
    
    public Block (double x, double y, double width, double height, int type){
        this.drawable_type = Drawable.BLOCK;
        this.type = type;
        this.setLocation((int)(x*Game.getBlockSize()), (int)(y*Game.getBlockSize()));
        this.setSize((int)(width*Game.getBlockSize()), (int)(height*Game.getBlockSize()));
        if(type == WALL){
            this.color = Color.GRAY;
            try {
                this.image = new ImageLoader("Wall.png").image;
                this.image = image.getScaledInstance((int) this.getWidth(), (int) this.getHeight(), Image.SCALE_AREA_AVERAGING);
            } catch (IOException ex) {
                Logger.getLogger(Block.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(type == DOOR){
            this.color = new Color(0, 255, 80, 200);
        }
    }
    
    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(this.color);
        g2.fillRect((int)this.x, (int)this.y, (int)this.width, (int)this.height);
        if(image != null){
            g2.drawImage(image, x, y, null);
        }
        if(Game.testMode){
            g2.setColor(Color.red);
            g2.drawRect((int)this.getX(), (int)this.getY(), (int)this.getWidth(), (int)this.getHeight());
        }
        
    }

    public int getType() {
        return type;
    }
    
}
