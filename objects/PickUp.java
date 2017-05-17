/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import pkg2dgame.Game;
import utilities.ImageLoader;
import utilities.Sound;

/**
 *
 * @author joel_
 */
public class PickUp extends Drawable{
    private String name;
    private double xCoor, yCoor;
    private Image image;
    private Sound sound;
    private int sound_;
    private int type;
    private int effect;
    private int power;
    private double HOTDOT_BASE = 0.01;
    
    //TYPE
    public final static int CONSUMABLE = 1;
    public final static int MELEE = 2;
    public final static int RANGED = 3;
    public final static int SPELL = 4;
    public final static int MONEY = 5;
    //TYPE
    
    //Item
    public final static int BLOWDARTS = 0;
    public final static int TACO = 1;
    public final static int SWORD = 2;
    public final static int COINS = 3;
    
    //Effect
    public final static int HEAL = 1;
    public final static int HOT = 2;
    public final static int HEAL_AND_HOT = 3;
    public final static int DMG = 4;
    public final static int DOT = 5;
    public final static int DMG_AND_DOT = 6;
    
    private double effect_divisor = 0.5;
    //Effect
    
    protected double HOTDOT_RATE = 0.01;
    
    /**
     * 
     * @param x coordinate
     * @param y coordinate
     * @param item eg. PickUp.BLOWDARTS
     */
    public PickUp(double x, double y, int item){
        this.drawable_type = Drawable.PICKUP;
        int rate = 1;
        switch(item){
            case BLOWDARTS:
                name = "Blowdarts";
                type = RANGED;
                effect = DMG_AND_DOT;
                power = 20;
                rate = 2;
                effect_divisor = 0.8;
                sound_ = Sound.BLOWDARTS;
                loadSound("blowdarts.wav");
                break;
            case TACO:
                name = "Taco";
                type = CONSUMABLE;
                effect = HEAL_AND_HOT;
                rate = 1;
                power = 20;
                sound_ = Sound.EATING_CRUNCHY;
                loadSound("eating_crunchy.wav");
                break;
            case SWORD:
                name = "Sword";
                type = MELEE;
                effect = DMG;
                power = 30;
                sound_ = Sound.SWORD;
                break;
            case COINS:
                name = "Coins";
                type = MONEY;
                power = Game.getRandom(1, 50);
                break;
            default:
                name = "Bork";
                break;
        }
        
        this.color = Color.BLACK;
        this.x = (int) x;
        this.y = (int) y;
        this.xCoor = x;
        this.yCoor = y;
        this.HOTDOT_RATE = HOTDOT_BASE*rate;
        loadImage(name+".png", Game.getUpscaling());
        
    }
    
    private void loadSound(String soundfile){
        if(soundfile != null){
            sound = new Sound(soundfile);
        }
    }
    
    public Sound getSound(){
        return sound;
    }
    
    public void loadImage(String imageName, double scaling){
        try {
            image = new ImageLoader(imageName).image;
        } catch (IOException ex) {
            Logger.getLogger(PickUp.class.getName()).log(Level.SEVERE, null, ex);
        }
        image = image.getScaledInstance((int)(image.getHeight(null)*scaling), (int)(image.getWidth(null)*scaling), Image.SCALE_AREA_AVERAGING);
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
    }
    public double getX(){
        return xCoor;
    }
    public double getY(){
        return yCoor;
    }
    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(this.color);
        
        if(image != null){
            g2.drawImage(image, (int)this.getX(), (int)this.getY(), null);
            if(Game.testMode){
                g2.setColor(Color.RED);
                g2.drawRect((int)this.getX(), (int)this.getY(), width, height);
            }
            
        }else{
            g2.draw(this);
            g2.fill(this);
        }
    }

    void setX(int x) {
        this.xCoor = x;
        this.x = x;
    }

    void setY(int y) {
        this.yCoor = y;
        this.y = y;
    }
    
    public int getEffect(){
        return effect;
    }
    
    public int getPower(){
        return power;
    }
    
    public int getType(){
        return type;
    }
    
    public double HOTDOT_RATE(){
        return HOTDOT_RATE;
    }

    double getEffectDivisor() {
        return effect_divisor;
    }
    
    public void playSound(){
        if(sound != null){
            sound.play();
        }
        /*
        try {
            Game.getSound().playCachedSound(sound_);
        } catch (IOException ex) {
            Logger.getLogger(PickUp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(PickUp.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
    }
}
