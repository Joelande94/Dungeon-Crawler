/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;

/**
 *
 * @author joel_
 */
public abstract class Drawable extends Rectangle{
    protected Color color;
    public abstract void draw(Graphics g);
    
    protected int drawable_type;
    public static final int PLAYER = 1;
    public static final int ENTITY = 2;
    public static final int BLOCK = 3;
    public static final int PROJECTILE = 4;
    public static final int PICKUP = 5;
    
    public int getDrawableType() {
        return drawable_type;
    }
    
}
