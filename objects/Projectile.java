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
import pkg2dgame.Game;

/**
 *
 * @author joel_
 */
public class Projectile extends Drawable{
    private Image image;
    private double angle;
    private int speed;
    private int dmg;
    private int dmgType;
    private Entity origin;
    private double xCoor, yCoor;
    private double HOTDOT_RATE = 0.01;
    private int inaccuracy;
    private double effect_divisor;
    
    /**
     * 
     * @param x
     * @param y
     * @param speed
     * @param dmg
     * @param HOTDOT_RATE
     * @param dmgType
     * @param effect_divisor
     * @param mousex
     * @param mousey
     * @param inaccuracy
     * @param image
     * @param origin 
     */
    public Projectile(int x, int y, int speed, int dmg, int dmgType, double HOTDOT_RATE, double effect_divisor, int mousex, int mousey, int inaccuracy, Image image, Entity origin){
        this.drawable_type = Drawable.PROJECTILE;
        this.x = x;
        this.y = y;
        this.image = image;
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
        this.speed = speed;
        this.dmg = dmg;
        this.dmgType = dmgType;
        this.HOTDOT_RATE = HOTDOT_RATE;
        this.effect_divisor = effect_divisor;
        this.origin = origin;
        this.inaccuracy = inaccuracy;
        double xDiff = mousex-this.getCenterX();
        double yDiff = mousey-this.getCenterY();
        this.angle = (Game.getRandom(0, inaccuracy) - inaccuracy/2) + ((float) (Math.atan2(yDiff, xDiff) * 180 / Math.PI));
    }
    
    /**
     * 
     * @param x
     * @param y
     * @param speed
     * @param dmg
     * @param dmgType
     * @param HOTDOT_RATE
     * @param effect_divisor
     * @param mousex
     * @param mousey
     * @param inaccuracy
     * @param origin 
     */
    public Projectile(int x, int y, int speed, int dmgType, int dmg, double HOTDOT_RATE, double effect_divisor, int mousex, int mousey, int inaccuracy, Entity origin){
        this.drawable_type = Drawable.PROJECTILE;
        this.x = x;
        this.y = y;
        this.xCoor = x;
        this.yCoor = y;
        this.width = 3;
        this.height = 3;
        this.dmg = dmg;
        this.dmgType = dmgType;
        this.HOTDOT_RATE = HOTDOT_RATE;
        this.effect_divisor = effect_divisor;
        this.origin = origin;
        this.speed = speed;
        this.inaccuracy = inaccuracy;
        double xDiff = mousex-this.getCenterX();
        double yDiff = mousey-this.getCenterY();
        this.angle = (Game.getRandom(0, inaccuracy) - inaccuracy/2) + ((float) (Math.atan2(yDiff, xDiff) * 180 / Math.PI));
    }
    
    public void move(double delta){
        double dx = delta*speed*Math.cos(angle * Math.PI/180);
        double dy = delta*speed*Math.sin(angle * Math.PI/180);
        this.xCoor += dx;
        this.yCoor += dy;
        this.x = (int)xCoor;
        this.y = (int)yCoor;
    }
            
    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        if(image != null){
            
        }else{
            g2.setColor(Color.GREEN);
            g2.fillOval(x, y, width, height);
        }
    }
    
    public int getDmg(){
        return dmg;
    }
    
    public int getDmgType(){
        return dmgType;
    }
    
    public Entity getOrigin(){
        return origin;
    }
    
    public double HOTDOT_RATE(){
        return HOTDOT_RATE;
    }

    public double getEffectDivisor() {
        return effect_divisor;
    }
            
}
