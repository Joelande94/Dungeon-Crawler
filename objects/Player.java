/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import utilities.ImageLoader;
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

/**
 *
 * @author joel_
 */
public class Player extends Entity{

    public double rotation;
    private int width = 32, height = 32;
    public PickUp q_slot = null, e_slot = null, r_slot = null, f_slot = null, t_slot = null;
    private int q_count = 0, e_count = 0, r_count = 0, f_count = 0, t_count = 0;
    private int MAX_ITEM_COUNT = 9;
    private double shrink_box_factor = 0.6;
    
    public float money = 0;
    
    public Player(){
        this.subClass = Entity.PLAYER;
        this.setMovementSpeed(1);
        this.color = Color.GREEN;
        try {
            if(Game.testMode){
                image = new ImageLoader("Player_hooded_test.png").image;
            }else{
                image = new ImageLoader("Player_hooded.png").image;
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
    }
    
    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(this.color);
        
        if(image != null){
            AffineTransform oldTransform = g2.getTransform();
            AffineTransform transform = new AffineTransform();
            
            //g2.draw(this);
            transform.setToIdentity();
            transform.translate((int)-(shrink_box_factor*this.width/2.82), -(shrink_box_factor*this.height/2.85));
            transform.rotate(Math.toRadians(rotation), this.xCoor+image.getWidth(null)/2, this.yCoor+image.getHeight(null)/2);
            g2.setTransform(transform);
            g2.drawImage(image, (int)(this.xCoor), (int)(this.yCoor), null);
            
            transform.translate((int)(shrink_box_factor*this.width/2.82), (shrink_box_factor*this.height/2.85));
            transform.setToIdentity();
            g2.setTransform(oldTransform);
            //THIS CAN ROTATE PROPERLY BUT COLLISION IS WEIRD
            /*
            double oldx = this.xCoor;
            double oldy = this.yCoor;
            setX(oldx-shrink_box_factor*this.width/6); //This is to fit the player texture in the middle of collider box
            setY(oldy-shrink_box_factor*this.height/6);//This is to fit the player texture in the wmiddle of collider box
            AffineTransform oldTransform = g2.getTransform();
            AffineTransform transform = new AffineTransform();
            
            Area a = new Area(this);
            a.transform(transform);
            g2.draw(a.getBounds2D());
            
            transform.setToIdentity();
            transform.rotate(Math.toRadians(rotation), this.xCoor+player.getWidth(null)/2, this.yCoor+player.getHeight(null)/2);
            g2.setTransform(transform);
            g2.drawImage(player, (int)(this.xCoor), (int)(this.yCoor), null);
            
            transform.setToIdentity();
            g2.setTransform(oldTransform);
            setX(oldx);
            setY(oldy);
            */
            //THIS CAN ROTATE PROPERLY BUT COLLISION IS WEIRD
        }else{
            g2.draw(this);
            g2.fill(this);
        }
        
        if(Game.testMode){
            g2.setColor(Color.RED);
            g2.drawRect((int)this.xCoor, (int)this.yCoor, (int)this.getWidth(), (int)this.getHeight());
        }
        
    }

    public void setRotation(int mousex, int mousey) {
        int xdiff = (int) (mousex-getCenterX());
        int ydiff = (int) (mousey-getCenterY());
        
        try{
            rotation = Math.atan2(ydiff, xdiff);
            rotation = Math.toDegrees(rotation) + 90;
            //rotation = Math.toDegrees(rotation); //FOR BUNNY
        }catch(ArithmeticException e){
            //e.printStackTrace();
        }
    }
    
    /**
     * @param i
     * @param p 
     */
    public void addToSlot(int i, PickUp p) {
        switch(i){
            case 0:
                q_slot = p; if(q_count < MAX_ITEM_COUNT) q_count++; break;
            case 1:
                e_slot = p; if(e_count < MAX_ITEM_COUNT) e_count++; break;
            case 2:
                r_slot = p; if(r_count < MAX_ITEM_COUNT) r_count++; break;
            case 3:
                f_slot = p; if(f_count < MAX_ITEM_COUNT) f_count++; break;
            case 4:
                t_slot = p; if(t_count < MAX_ITEM_COUNT) t_count++; break;
            default:
                break;
        }
    }
    
    public int getqCount(){
        return q_count;
    }
    public int geteCount(){
        return e_count;
    }
    public int getrCount(){
        return r_count;
    }
    public int getfCount(){
        return f_count;
    }
    public int gettCount(){
        return t_count;
    }

    public void useItem(int i) {
        switch(i){
            case 0:
                if(q_slot != null){
                    useWeapon(i);
                    q_slot.playSound();
                }
                break;
            case 1: 
                if(e_slot != null){
                    useWeapon(i);
                }
                break;
            case 2:
                if(r_slot != null){
                    useSpell(i);
                }
                break;
            case 3:
                if(f_slot != null){
                    useConsumable(i);
                }
                break;
            case 4:
                if(t_slot != null){
                    
                }
                break;
            default:
                break;
        }
    }
    private void useWeapon(int slot){
        switch(slot){
            case 0:
                if(q_slot.getType() == PickUp.RANGED){
                    Projectile p = null;
                    p = new Projectile((int)getCenterX(), (int)getCenterY(), 3, q_slot.getEffect(), q_slot.getPower(), q_slot.HOTDOT_RATE(), q_slot.getEffectDivisor(), g.getMousex(), g.getMousey(), 6, this);
                    if(p != null){
                        g.addProjectile(p);
                    }
                }
                break;
            case 1:
                
                break;
            default:
                
                break;
        }
    }
    private void useConsumable(int slot){
        switch(slot){
            case 3:
                int eff = f_slot.getEffect();
                int power = f_slot.getPower();
                double rate = f_slot.HOTDOT_RATE();
                double divisor = f_slot.getEffectDivisor();
                effect(eff, power, rate, divisor);
                
                f_slot.playSound();
                
                f_count--;
                if(f_count == 0){
                    f_slot = null;
                }
                break;
            default:
                break;
        }
    }
    
    private void useSpell(int slot){
        
    }
    

    double getHealth() {
        return health;
    }

    double getMaxHealth() {
        return maxHealth;
    }
    
    @Override
    public void setX(double x){
        if(x+getWidth() < Game.getWidth() && x > 0){
            double temp = xCoor;
            this.xCoor = x;
            this.x = (int)x;
            
            Block door = (Block) g.collides(this, 2);
            if(door != null){
                if(x < Game.getWidth()/2){
                    System.out.println("LEFT DOOR");
                }else{
                    System.out.println("RIGHT DOOR");
                }
            }
            
            if(g.collides(this, 0) != null){
                //Unsticks entity if collision
                this.xCoor = temp;
                this.x = (int)temp;
            }
            this.setBounds(this.x, this.y, (int)this.getWidth(), (int)this.getHeight());
        }
    }
    
    @Override
    public void setY(double y){
        if(y+getHeight() < Game.getHeight() && y > 0){
            double temp = yCoor;
            this.yCoor = y;
            this.y = (int)y;
            
            Block door = (Block)g.collides(this, 2);
            if(door != null){
                if(y < Game.getHeight()/2){
                    System.out.println("TOP DOOR");
                }else{
                    System.out.println("BOTTOM DOOR");
                }
            }
            
            if(g.collides(this, 0) != null){
                //Unsticks entity if collision
                this.yCoor = temp;
                this.y = (int)temp;
            }
            this.setBounds(this.x, this.y, (int)this.getWidth(), (int)this.getHeight());
        }
    }
    
    public float getMoney() {
        return money;
    }

    public void addMoney(int money) {
        this.money += money;
    }
}
