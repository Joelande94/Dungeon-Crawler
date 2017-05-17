/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
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
public class Entity extends Drawable{
    protected Game g;
    protected Image image;
    
    protected boolean up = false, down = false, left = false, right = false;
    double xCoor, yCoor;
    protected double movementSpeed;
    protected boolean alive = false;
    protected double health = 80, maxHealth = 100;
    protected double attackSpeed = 1;
    private double attackRange = 25;
    protected int healthbarHeight = 2;
    private double attackPower;
    
    //The time of the last attack
    private long lastAttack = 0;
    
    protected int subClass;
    public static final int PLAYER = 1;
    public static final int ZOMBIE = 2;
    
    //10 effects with four values: Effect type, effect left, effect rate, effect counter
    //protected double hots[][];
    //protected double dots[][];
    protected ArrayList<double[]> hots;
    protected ArrayList<double[]> dots;
    protected int MAX_EFFECTS = 5;
    
    protected double HOT_LEFT = 0;
    protected double HOT_RATE = 0.01;
    protected int HOT_COUNTER = 0;
    
    protected double DOT_LEFT = 0;
    protected double DOT_RATE = 0.01;
    protected int DOT_COUNTER = 0;
    
    //Drop
    protected int drop = -1;
    public final static int DROP_COINS = 0;
    public final static int DROP_TACO = 1;
    
    private Sound hurt_sound;
    
    /**
     * @param xCoor
     * @param yCoor
     * @param health
     * @param maxHealth
     * @param damage 
     */
    public void init(int xCoor, int yCoor, int health, int maxHealth, int attackPower){
        this.g = Game.getInstance();
        this.drawable_type = Drawable.ENTITY;
        this.x = xCoor;
        this.y = yCoor;
        this.xCoor = xCoor;
        this.yCoor = yCoor;
        this.health = health;
        this.maxHealth = maxHealth;
        this.attackPower = attackPower;
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect((int)this.getX(), (int)this.getY(), (int)this.getWidth(), (int)this.getHeight());
        
        g.setColor(Color.RED);
        g.fillRect((int) this.getX(), (int) this.getY(), width, healthbarHeight);
        g.setColor(Color.GREEN);
        g.fillRect((int) this.getX(), (int) this.getY(), (int)(width*(health/maxHealth)), healthbarHeight);
        g.setColor(Color.white);
        g.drawRect((int) this.getX(), (int) this.getY(), width, healthbarHeight);
    }
    
    public double getMovementSpeed(){
        return movementSpeed;
    }
    public void setMovementSpeed(double movementSpeed){
        this.movementSpeed = movementSpeed;
    }
    public void setUp(boolean up){
        this.up = up;
    }
    public void setDown(boolean down){
        this.down = down;
    }
    public void setLeft(boolean left){
        this.left = left;
    }
    public void setRight(boolean right){
        this.right = right;
    }
    public boolean getUp(){
        return up;
    }
    public boolean getDown(){
        return down;
    }
    public boolean getLeft(){
        return left;
    }
    public boolean getRight(){
        return right;
    }
    public void setX(double x){
        if(x+getWidth() < Game.getWidth() && x > 0){
            double temp = xCoor;
            this.xCoor = x;
            this.x = (int)x;
            if(g.collides(this) != null){
                //Unsticks entity if collision
                this.xCoor = temp;
                this.x = (int)temp;
            }
            this.setBounds(this.x, this.y, (int)this.getWidth(), (int)this.getHeight());
        }
    }
    public void setY(double y){
        if(y+getHeight() < Game.getHeight() && y > 0){
            double temp = yCoor;
            this.yCoor = y;
            this.y = (int)y;
            if(g.collides(this) != null){
                //Unsticks entity if collision
                this.yCoor = temp;
                this.y = (int)temp;
            }
            this.setBounds(this.x, this.y, (int)this.getWidth(), (int)this.getHeight());
        }
    }
    public double getX(){
        return xCoor;
    }
    public double getY(){
        return yCoor;
    }
    public void setGame(Game g){
        this.g = g;
    }

    public int getSubClass() {
        return this.subClass;
    }
    /**
     * @param power is the strength of the effect (0->)
     * 
     * @param effect is one of the following: i.e. PickUp.HEAL
     * public final static int HEAL = 1;
     * public final static int HOT = 2;
     * public final static int HEAL_AND_HOT = 3;
     * public final static int DMG = 4;
     * public final static int DOT = 5;
     * public final static int DMG_AND_DOT = 6;
     * 
     * @param rate is the rate at which HOTs and DOTs are activated. 1.0 is standard, 2.0 is two times standard. 
     * @param divisor double between 0 and 1. HEAL_AND_HOT with divisor 0.3 means 0.3*power is instant heal and 0.7*power is HOT
    */
    public void effect(int effect, int power, double rate, double divisor){
        switch(effect){
            case PickUp.HEAL: 
                heal(power);
                break;
            case PickUp.HOT: 
                addEffect(effect, power, rate);
                break;
            case PickUp.HEAL_AND_HOT:
                heal(power*(1-divisor));
                addEffect(effect, (int)(power*divisor), rate);
                break;
            case PickUp.DMG:
                damage(power);
                break;
            case PickUp.DOT: 
                addEffect(effect, power, rate);
                break;
            case PickUp.DMG_AND_DOT: 
                damage(power*(1-divisor));
                addEffect(effect, (int)(power*divisor), rate);
                break;
            default: break;
        }
        if(health <= 0) kill();
    }
    /**
     * addEffect(int effect_type, double effect_left, double effect_rate)
     * @param effect_type PickUp.TYPE
     * @param effect_left 
     * @param effect_rate 
     */
    private void addEffect(int effect_type, double effect_left, double effect_rate){
        if(effect_type <= PickUp.HEAL_AND_HOT){
            addEffect_helper(effect_type, effect_left, effect_rate, hots);
        }else{//Else effect_type is a dot
            addEffect_helper(effect_type, effect_left, effect_rate, dots);
        }
    }
    
    private void addEffect_helper(int effect_type, double effect_left, double effect_rate, ArrayList<double[]> list){
        boolean found_slot = false;
        int least_left = 0;
        if(list.size()<MAX_EFFECTS){
            double[] effect = new double[4];
            effect[0] = effect_type;
            effect[1] = effect_left;
            effect[2] = effect_rate;
            list.add(effect);
            found_slot = true;
        }else{
            double least = 80085;
            for(double[] effect: list){
                if(effect[0] == effect_type && least < effect[1]) least_left = list.indexOf(effect);
            }
        }
        if(!found_slot){
            double[] effect = new double[4];
            effect[0] = effect_type;
            effect[1] = effect_left;
            effect[2] = effect_rate;
            effect[3] = 0;
            list.set(least_left, effect);
        }
    }
    private double[] EOT(double[] eots, int sign){
        double left = eots[1];
        double rate = eots[2];
        double counter = eots[3];
        if(left > 0){
            counter++;
            if(counter >= 1){
                if(left >= rate){
                    if(sign==1)heal(rate);
                    else damage(rate);
                    left -= rate;
                }
                else{
                    if(sign==1)heal(rate);
                    else damage(rate);
                    left -= left;
                }
            }
        }else{
            return null;
        }
        
        eots[1] = left;
        eots[2] = rate;
        eots[3] = counter;
        return eots;
    }
    
    public void trigger_effects(){
        for(int i=0; i<MAX_EFFECTS; i++){
            trigger_effects(hots, i, 1);
            trigger_effects(dots, i, -1);
        }
    }
    private void trigger_effects(ArrayList<double[]> list, int i, int sign){
        if(list.size() >= i+1){
            if(list.get(i) != null){
                double[] temp = EOT(list.get(i), sign);
                if (temp != null) { //There's still power left in the EOT
                    list.set(i, temp);
                }else{//EOT has done its job
                    list.remove(i);
                }
            }
        }
    }
    
    public void damage(double dmg){
        if(subClass == PLAYER) System.out.println("Ow");
        if(Game.godMode) this.health -= 1000*dmg;
        this.health -= dmg;
        //GetSound.getHurt(this.subClass).play();
        if(health <= 0) kill();
        
    }
    
    public void heal(double heal){
        this.health += heal;
        if(health > maxHealth) health = maxHealth;
    }
    
    private void kill(){
        if(alive){
            alive = false;
            if(subClass == PLAYER){
                g.gameOver();
                return;
            }else if(subClass == ZOMBIE){
                try {
                    image = new ImageLoader("zombie_splat.png").image;
                } catch (IOException ex) {
                    Logger.getLogger(Entity.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(this.drop != -1){
                Game.addDrop(getDrop());
            }
        }
    }
    
    public Image getImage(){
        return image;
    }
    
    public boolean isAlive(){
        return alive;
    }

    public double getAttackRange() {
        return attackRange;
    }
    
    public double getAttackPower(){
        return attackPower;
    }

    public long getLastAttack() {
        return lastAttack;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public void setLastAttack(long time) {
        this.lastAttack = time;
    }
    
    public PickUp getDrop(){
        return new PickUp(this.getX(), this.getY(), drop);
    }
    
}
