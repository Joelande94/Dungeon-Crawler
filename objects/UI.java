/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pkg2dgame.Game;
import pkg2dgame.Main;
import utilities.ImageLoader;

/**
 *
 * @author joel_
 */
public class UI {
    private Image item_frame_q;
    private Image item_frame_e;
    private Image item_frame_r;
    private Image item_frame_f;
    private Image item_frame_t;
    private Image background;
    private Dimension d;
    private int HEIGHT;
    private int item_frame_width = 32;
    private int item_frame_height = item_frame_width;
    private double item_frame_upscaling = 2.5;
    private int item_frame_spacing = 16;
    private Player player;
    
    //Healthbar values to be multiplied by frame scale
    private double healthbar_x = 0.05;
    private double healthbar_y = 0.86;
    private double healthbar_height = 0.05;
    private double healthbar_width = 0.10;
    
    //Coinstack position to be multiplied by frame scale
    private double coinstack_x = 0.9;
    private double coinstack_y = 0.88;
    private double coinstack_text_x = 0.91;
    private double coinstack_text_y = 0.96;
    private double coinstack_upscaling = 3;
    private Image coinstack;
    
    //Paused UI
    private Image paused_bg;
    
    public UI(Dimension d, int blockSize){
        this.d = d;
        HEIGHT = (int)(7.4*d.height/blockSize);
        
        item_frame_height = (int)(item_frame_height*item_frame_upscaling);
        try {
            item_frame_q = new ImageLoader("Item_frame_q.png").image;
            item_frame_e = new ImageLoader("Item_frame_e.png").image;
            item_frame_r = new ImageLoader("Item_frame_r.png").image;
            item_frame_f = new ImageLoader("Item_frame_f.png").image;
            item_frame_t = new ImageLoader("Item_frame_t.png").image;
            background   = new ImageLoader("UI_background.png").image;
            coinstack    = new ImageLoader("Coinstack.png").image;
        } catch (IOException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
        item_frame_q = item_frame_q.getScaledInstance((int)(item_frame_height), (int)(item_frame_height), Image.SCALE_AREA_AVERAGING);
        item_frame_e = item_frame_e.getScaledInstance((int)(item_frame_height), (int)(item_frame_height), Image.SCALE_AREA_AVERAGING);
        item_frame_f = item_frame_f.getScaledInstance((int)(item_frame_height), (int)(item_frame_height), Image.SCALE_AREA_AVERAGING);
        item_frame_r = item_frame_r.getScaledInstance((int)(item_frame_height), (int)(item_frame_height), Image.SCALE_AREA_AVERAGING);
        item_frame_t = item_frame_t.getScaledInstance((int)(item_frame_height), (int)(item_frame_height), Image.SCALE_AREA_AVERAGING);
        coinstack = coinstack.getScaledInstance((int)(coinstack.getHeight(null)*coinstack_upscaling), (int)(coinstack.getHeight(null)*coinstack_upscaling), Image.SCALE_AREA_AVERAGING);
    }
    public void initPlayer(Player player){
        this.player = player;
    }
    
    public void draw(Graphics g) {
        if(Game.ready()){
            g.setColor(Color.red);
            g.drawImage(background, 0, d.height-HEIGHT, null);
            Font oldFont = g.getFont();

            if(player.q_slot != null){
                player.q_slot.setX((int)(5*d.getWidth()/item_frame_spacing));
                player.q_slot.setY((int)((d.getHeight()-item_frame_height)));
                player.q_slot.draw(g);
            }
            if(player.getqCount() > 1){
                g.setColor(Color.WHITE);
                g.setFont(new Font("MONOSPACE", Font.BOLD, 18));
                g.drawString(player.getqCount()+"", (int)((5*d.getWidth()/item_frame_spacing)+6*item_frame_q.getWidth(null)/10), (int)(((d.getHeight()-item_frame_height)+4*item_frame_q.getWidth(null)/6)));
            }
            if(player.e_slot != null){
                player.e_slot.setX((int)(6*d.getWidth()/item_frame_spacing));
                player.e_slot.setY((int)((d.getHeight()-item_frame_height)));
                player.e_slot.draw(g);
            }
            if(player.geteCount() > 1){
                g.setColor(Color.WHITE);
                g.setFont(new Font("MONOSPACE", Font.BOLD, 18));
                g.drawString(player.getfCount()+"", (int)((6*d.getWidth()/item_frame_spacing)+6*item_frame_f.getWidth(null)/10), (int)(((d.getHeight()-item_frame_height)+4*item_frame_f.getWidth(null)/6)));
            }
            if(player.r_slot != null){
                player.r_slot.setX((int)(7*d.getWidth()/item_frame_spacing));
                player.r_slot.setY((int)((d.getHeight()-item_frame_height)));
                player.r_slot.draw(g);
            }
            if(player.getrCount() > 1){
                g.setColor(Color.WHITE);
                g.setFont(new Font("MONOSPACE", Font.BOLD, 18));
                g.drawString(player.getfCount()+"", (int)((7*d.getWidth()/item_frame_spacing)+6*item_frame_f.getWidth(null)/10), (int)(((d.getHeight()-item_frame_height)+4*item_frame_f.getWidth(null)/6)));
            }
            if(player.f_slot != null){
                player.f_slot.setX((int)(8*d.getWidth()/item_frame_spacing));
                player.f_slot.setY((int)((d.getHeight()-item_frame_height)));
                player.f_slot.draw(g);
            }
            if(player.getfCount() > 1){
                g.setColor(Color.WHITE);
                g.setFont(new Font("MONOSPACE", Font.BOLD, 18));
                g.drawString(player.getfCount()+"", (int)((8*d.getWidth()/item_frame_spacing)+6*item_frame_f.getWidth(null)/10), (int)(((d.getHeight()-item_frame_height)+4*item_frame_f.getWidth(null)/6)));
            }
            if(player.t_slot != null){
                player.t_slot.setX((int)(9*d.getWidth()/item_frame_spacing));
                player.t_slot.setY((int)((d.getHeight()-item_frame_height)));
                player.t_slot.draw(g);
            }
            if(player.gettCount() > 1){
                g.setColor(Color.WHITE);
                g.setFont(new Font("MONOSPACE", Font.BOLD, 18));
                g.drawString(player.getfCount()+"", (int)((9*d.getWidth()/item_frame_spacing)+6*item_frame_f.getWidth(null)/10), (int)(((d.getHeight()-item_frame_height)+4*item_frame_f.getWidth(null)/6)));
            }

            //Action slots
            g.drawImage(item_frame_q, (int)(5*d.getWidth()/item_frame_spacing), (int)((d.getHeight()-item_frame_height)), null);
            g.drawImage(item_frame_e, (int)(6*d.getWidth()/item_frame_spacing), (int)((d.getHeight()-item_frame_height)), null);
            g.drawImage(item_frame_r, (int)(7*d.getWidth()/item_frame_spacing), (int)((d.getHeight()-item_frame_height)), null);
            g.drawImage(item_frame_f, (int)(8*d.getWidth()/item_frame_spacing), (int)((d.getHeight()-item_frame_height)), null);
            g.drawImage(item_frame_t, (int)(9*d.getWidth()/item_frame_spacing), (int)((d.getHeight()-item_frame_height)), null);

            //Healthbar
            g.setColor(Color.RED);
            g.fillRect((int)(healthbar_x*d.width), (int)(healthbar_y*d.height), (int)(healthbar_width*d.width), (int)(healthbar_height*d.height));
            g.setColor(Color.GREEN);
            g.fillRect((int)(healthbar_x*d.width), (int)(healthbar_y*d.height), (int)((healthbar_width*d.width)*(player.getHealth()/player.getMaxHealth())), (int)(healthbar_height*d.height));
            g.setColor(Color.WHITE);
            g.drawRect((int)(healthbar_x*d.width), (int)(healthbar_y*d.height), (int)(healthbar_width*d.width), (int)(healthbar_height*d.height));
            g.setFont(new Font("MONOSPACE", Font.BOLD, 18));
            String healthstring = (int)player.getHealth() + " / " + (int)player.getMaxHealth();
            int healthstring_x = (int)(healthbar_x*d.width + healthbar_width/2*d.width - g.getFont().getSize()*healthstring.length()/4);
            int healthstring_y = (int)(healthbar_y*d.height + healthbar_height/2*d.height + g.getFont().getSize()/2);
            g.drawString(healthstring, healthstring_x, healthstring_y);

            //Coinstack
            g.setFont(new Font("MONOSPACE", Font.BOLD, 18));
            g.setColor(Color.YELLOW);
            g.drawImage(coinstack, (int)(coinstack_x*d.width), (int)(coinstack_y*d.height), null);
            g.drawString((int)player.getMoney()+"", (int)(coinstack_text_x*d.width), (int)(coinstack_text_y*d.height));
            g.setFont(oldFont);
            
            if(Game.getPaused()){
                g.setColor(new Color(255, 255, 255, 125));
                g.fillRect(0,0, Main.getGameWidth(), Main.getGameHeight());
            }
        }
    }

    public int getHeight() {
        return HEIGHT;
    }
    
}
