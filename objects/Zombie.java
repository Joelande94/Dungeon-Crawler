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
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pkg2dgame.Game;
import utilities.ImageLoader;
import utilities.Sound;

/**
 *
 * @author joel_
 */
public class Zombie extends Entity{
    
    private Sound sound;
    
    private double shrink_box_factor = 0.6;
    
    private double rotation = 90;
    private double targetx;
    private double targety;
    
    public Zombie(int x, int y){
        this.subClass = Entity.ZOMBIE;
        this.setMovementSpeed(0.6);
        //Init(health, maxHealth, damage);
        this.init(x, y, Game.getRandom(50, 100), 100, 10);
        this.color = Color.GREEN;
        this.setSize(Game.getBlockSize(), Game.getBlockSize());
        try {
            if(Game.testMode){
                image = new ImageLoader("Zombie_test.png").image;
            }else{
                image = new ImageLoader("Zombie_fat_v2.png").image;
            }
            
            //TODO SOUND
            int sound = Game.getRandom(1,4);
            switch(sound){
                    case 1:
                        this.sound = new Sound("zombie_idle_1.wav");
                        break;
                    case 2:
                        this.sound = new Sound("zombie_idle_2.wav");
                        break;
                    case 3:
                        this.sound = new Sound("zombie_idle_3.wav");
                        break;
                    default:
                        this.sound = new Sound("zombie_idle_1.wav");
                        break;
            }
            
            //player = new ImageLoader("Bunny.png").image;
            width = (int)(image.getWidth(null)*Game.getUpscaling());
            height = (int)(image.getHeight(null)*Game.getUpscaling());
            image = image.getScaledInstance((int)(width), (int)(height), Image.SCALE_AREA_AVERAGING);
            this.setSize(new Dimension((int)(width*shrink_box_factor), (int)(height*shrink_box_factor)));
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
        alive = true;
        hots = new ArrayList<>(MAX_EFFECTS);
        dots = new ArrayList<>(MAX_EFFECTS);
        
        drop = PickUp.COINS;
    }
    
    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(this.color);
        
        if(!alive){
            g2.drawImage(image, (int)xCoor, (int) yCoor, null);
        }else{
            if(image != null){
                AffineTransform oldTransform = g2.getTransform();
                AffineTransform transform = new AffineTransform();

                //g2.draw(this);
                transform.setToIdentity();
                transform.translate((int)-(shrink_box_factor*this.getWidth()/1.7), -(shrink_box_factor*this.getHeight()/1.8));
                transform.rotate(Math.toRadians(rotation), this.xCoor+image.getWidth(null)/2, this.yCoor+image.getHeight(null)/2);
                g2.setTransform(transform);
                g2.drawImage(image, (int)(this.xCoor), (int)(this.yCoor), null);

                transform.translate((int)(shrink_box_factor*this.width/1.7), (shrink_box_factor*this.height/1.8));
                transform.setToIdentity();
                g2.setTransform(oldTransform);
            }else{
                g2.draw(this);
                g2.fill(this);
            }

            g.setColor(Color.RED);
            g.fillRect((int) this.getX(), (int) this.getY(), width, healthbarHeight);
            g.setColor(Color.GREEN);
            g.fillRect((int) this.getX(), (int) this.getY(), (int)(width*(health/maxHealth)), healthbarHeight);
            g.setColor(Color.white);
            g.drawRect((int) this.getX(), (int) this.getY(), width, healthbarHeight);


            if(Game.testMode){
                g2.setColor(Color.RED);
                g2.drawRect((int)this.xCoor, (int)this.yCoor, (int)this.getWidth(), (int)this.getHeight());
            }
        }
        
        
        
    }
    /**
     * @param targetx (not the final target but the target for the next step)
     * @param targety (not the final target but the target for the next step)
     */
    public void setRotation(double targetx, double targety) {
        //Not final targetx and targety but for the next step
        double xdiff = (targetx-getCenterX());
        double ydiff = (targety-getCenterY());
        
        try{
            rotation = Math.atan2(ydiff, xdiff);
            rotation = Math.toDegrees(rotation) + 90;
        }catch(ArithmeticException e){
            //e.printStackTrace();
        }
        double rot = rotation + 90;
        setUp(false);
        setDown(false);
        setRight(false);
        setLeft(false);
        double slice = 360/16;
        if(rot >= slice*3 && rot <= slice*5){
            this.up = true;
        }
        else if(rot > slice*5 && rot <= slice*7){
            this.right = true;
            this.up = true;
        }
        else if(rot > slice*7 && rot <= slice*9){
            this.right = true;
        }
        else if(rot > slice*9 && rot <= slice*11){
            this.right = true;
            this.down = true;
        }
        else if(rot > slice*11 && rot <= slice*13){
            this.down = true;
        }
        else if(rot > slice*13 && rot <= slice*15){
            this.down = true;
            this.left = true;
        }
        else if(rot > slice*15 && rot <= 360 || rot >= 0 && rot <= slice*1){
            this.left = true;
        }
        else if(rot > slice*1 && rot <= slice*3){
            this.left = true;
            this.up = true;
        }
    }
    
    public void move(double targetx, double targety, double delta){
        setRotation(targetx, targety);
        
        this.setX(this.movementSpeed*Math.cos(rotation)*delta);
        this.setY(this.movementSpeed*Math.sin(rotation)*delta);
    }
    
    public void playSound() {
        sound.play();
    }
}
