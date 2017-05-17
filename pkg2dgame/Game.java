/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dgame;

import objects.Block;
import objects.Drawable;
import objects.Entity;
import objects.Player;
import objects.UI;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.event.MouseInputListener;
import objects.Button;
import objects.Menu;
import objects.PickUp;
import objects.Projectile;
import objects.Splat;
import objects.Zombie;

/**
 *
 * @author joel_
 */
public class Game {

    public final static boolean testMode = false;
    public final static boolean godMode = true;
    
    public static Game instance;
    
    public static Game getInstance() {
        return instance;
    }

    public static boolean ready() {
        return ready;
    }
    /*
    private static Sound sound = new Sound();
    public static Sound getSound(){
        return sound;
    }
    */

    public static boolean getPaused() {
        return paused;
    }

    
    
    
    private Screen s;
    private Main main;
    private UI ui;
    private static int PLAYGROUND_WIDTH, PLAYGROUND_HEIGHT;
    
    private boolean running;
    private boolean gameOver = false;
    private boolean inMenu = false;
    private static boolean paused = false;
    private static boolean ready = false;
    
    //Game loop stuff
    private long time;
    private long lastLoopTime = System.nanoTime();
    private int TARGET_FPS = 144;
    private final long OPTIMAL_TIME = 1000000000/TARGET_FPS;
    private long lastFpsTime;
    private int fps;
    private long updateLength;
    private double delta;
    //Game loop stuff
    
    //Game constants
    private static int blockSize;
    private final double gravity = 10;
    private final static double upscaling = 2;
    
    //Game constants
    
    //Player control
    private boolean mouse_click = false;
    private Key key;
    private boolean up = false, down = false, left = false, right = false;//Movement
    
    private MouseListener mouseListener;
    private int mousex, mousey;
    private boolean holding_click = false, attack = false, strong_attack = false;
    private long hold_attack_time;
    private long attack_strength;
    
    //Player control
    
    
    //Game objects
    private Menu menu;
    private Menu paused_menu;
    
    private Block[][] map;
    private PickUp[][] pickup_map;
    //private Entity[][] entity_map;
    private Player player;
    
    private ArrayList<Drawable> drawables;
    private ArrayList<Block> blocks;
    private ArrayList<Entity> entities;
    private static ArrayList<PickUp> pickups;
    private ArrayList<Projectile> projectiles;
    private ArrayList<Splat> splats;
    
    private Projectile[] projectiles_to_remove;
    private Entity[] entities_to_remove;
    //Game objects
    
    public Game(Main main, Screen s, UI ui, int blockSize){
        this.instance = this;
        this.main = main;
        this.s = s;
        this.ui = ui;
        this.blockSize = blockSize;
        Dimension d = s.getSize();
        PLAYGROUND_WIDTH = d.width;
        PLAYGROUND_HEIGHT = d.height-ui.getHeight();
        
        key = new Key();
        main.addKeyListener(key);
        mouseListener = new MouseListener();
        main.addMouseListener(mouseListener);
        main.addMouseMotionListener(mouseListener);
        
        menu = new Menu(d, Menu.MAIN);
        paused_menu = new Menu(d, Menu.PAUSED);
        
        start();
    }
    
    
    public void start(){
        running = true;
        inMenu = true;
        gameOver = false;
        paused = false;
        s.setInMenu(true);
        ready = true;
        run();
    }
    public void init_game(){
        entities = new ArrayList<>();
        drawables = new ArrayList<>();
        projectiles = new ArrayList<>();
        splats = new ArrayList<>();
        projectiles_to_remove = new Projectile[100];
        entities_to_remove = new Entity[100];
        loadPlayer();
        generateLevel();
        s.setDrawables(drawables);
        ui.initPlayer(player);
        s.setGameOver(false);
        s.setPaused(false);
        paused = false;
        inMenu = false;
        gameOver = false;
        time = System.nanoTime();
        ready = true;
    }
    public void run(){
        while(running){
            time = System.nanoTime();
            updateLength = time - lastLoopTime;
            lastLoopTime = time;
            delta = updateLength/((double)OPTIMAL_TIME);
            
            lastFpsTime += updateLength;
            fps++;
            
            //Do game updates
            if(inMenu){
            }
            else if(!gameOver && !paused){
                updateGame(delta);
            }
            else if(gameOver){
                if(s.gameOverFaded()){
                    gameOver = false;
                    inMenu = true;
                }
            }
            else if(paused){
                
            }
            
            //draw everything
            s.repaint();
            
            if(lastFpsTime >= 1000000000){
                System.out.println("FPS: " + fps);
                s.setFps(fps);
                lastFpsTime = 0;
                fps = 0;
            }
            
            try{
                Thread.sleep((lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000);
            }catch(Exception e){
                
            }
        }
    }
    public void stop() throws InterruptedException{
        running = false;
    }
    
    public void updateGame(double delta){
        //Every change * delta;
        for(Entity e: entities){
            //Movement
            //0.7 is just so that pressing for example 'W' and 'D' won't make the player move insanely fast.
            //Calculated with pythagoras theorem.
            if(e.getUp() && !e.getDown() && collides(e) == null){
                if(e.getLeft() ^ e.getRight()){
                    e.setY(e.getY() - 0.7*e.getMovementSpeed()*delta);
                }else{
                    e.setY(e.getY() - e.getMovementSpeed()*delta);
                }
            }
            if(e.getDown() && !e.getUp() && collides(e) == null){
                if(e.getLeft() ^ e.getRight()){
                    e.setY(e.getY() + 0.7*e.getMovementSpeed()*delta);
                }else{
                    e.setY(e.getY() + e.getMovementSpeed()*delta);
                }
            }
            if(e.getLeft() && !e.getRight() && collides(e) == null){
                if(e.getUp() ^ e.getDown()){
                    e.setX(e.getX() - 0.7*e.getMovementSpeed()*delta);
                }else{
                    e.setX(e.getX() - e.getMovementSpeed()*delta);
                }
            }
            if(e.getRight() && !e.getLeft() && collides(e) == null){
                if(e.getUp() ^ e.getDown()){
                    e.setX(e.getX() + 0.7*e.getMovementSpeed()*delta);
                }else{
                    e.setX(e.getX() + e.getMovementSpeed()*delta);
                }
            }
            //Movement
        }
        player.setRotation(mousex, mousey);

        PickUp p = pickup_collides(player);
        if(p != null){
            pickups.remove(p);
            if(p.getType() == PickUp.MONEY){
                player.addMoney(p.getPower());
            }else{
                player.addToSlot(3, p);
            }
        }

        int e_counter = 0;
        for(Entity e: entities){
            //Trigger entity HOT and DOT effects
            e.trigger_effects();
            
            if(e.getSubClass() == Entity.ZOMBIE){
                if(!e.isAlive()){
                    Splat s = new Splat(e.getImage(), (int)e.getX(), (int)e.getY(), (int)e.getWidth(), (int) e.getHeight());
                    splats.add(s);
                    entities_to_remove[e_counter++] = e;
                }else{
                    //Zombie growl
                    if(getRandom(1,10000) == 5000){
                        ((Zombie)e).playSound();
                    }

                    //Move zombies
                    ((Zombie)e).setRotation(player.getX(), player.getY());

                    if(getDistance(e, player) < e.getAttackRange() && !e.equals(player)){
                        if(System.nanoTime() - e.getLastAttack() > e.getAttackSpeed()*1000000000){
                            e.setLastAttack(System.nanoTime());
                            if(!godMode){
                                player.damage(e.getAttackPower());
                            }
                            System.out.println("ATTACK");
                            s.playerHit();
                        }
                    }
                }

            }

        }

        //Remove dead entities
        for(int i=0; i<e_counter; i++){
            Entity en = entities_to_remove[i];
            if(en != null){
                entities.remove(en);
            }
            entities_to_remove[i] = null;
        }
        //Remove dead entities

        //Projectiles
        int p_counter = 0;
        //Move projectiles and check for collision
        int i = 0;
        while(i < projectiles.size()){
            Projectile pr = projectiles.get(i);
            pr.move(delta);
            Drawable collision = collides(pr);
            if(collision != null && !collision.equals(pr.getOrigin())){
                projectiles_to_remove[p_counter++] = pr;
                if(collision.getDrawableType() == Drawable.ENTITY){
                    ((Entity) collision).effect(pr.getDmgType(), pr.getDmg(), pr.HOTDOT_RATE(), pr.getEffectDivisor());
                }
            }
            i++;
        }

        //Remove old projectiles
        for(int j=0; j<p_counter; j++){
            Projectile pro = projectiles_to_remove[j];
            if(pro != null){
                projectiles.remove(pro);
            }
            projectiles_to_remove[j] = null;
        }
        //Projectiles

        drawables = new ArrayList<>();
        drawables.addAll((ArrayList<Splat>)((Object)splats));
        drawables.addAll((ArrayList<Drawable>)((Object)pickups));
        drawables.addAll((ArrayList<Drawable>)((Object)projectiles));
        drawables.addAll((ArrayList<Drawable>)((Object)entities));
        drawables.addAll((ArrayList<Drawable>)((Object)blocks));

        s.setDrawables(drawables);
    }
    
    //Static functions
    public static int getBlockSize() {
        return blockSize;
    }
    public static int getHeight(){
        return PLAYGROUND_HEIGHT;
    }
    public static int getWidth(){
        return PLAYGROUND_WIDTH;
    }
    public static int getRandom(int min, int max){
        Random rand = new Random();
        return rand.nextInt(max-min)+min;
    }
    public static double getUpscaling() {
        return upscaling;
    }
    public static void addDrop(PickUp drop) {
        pickups.add(drop);
    }
    //Static functions
    
    //Returns the distance between two rectangles
    public double getDistance(Rectangle r, Rectangle r2){
        double xdiff = Math.abs(r.getCenterX() - r2.getCenterX()) - (r.getWidth()/2 + r2.getWidth()/2);
        double ydiff = Math.abs(r.getCenterY() - r2.getCenterY()) - (r.getHeight()/2 + r2.getHeight()/2);
        
        double distance = Math.sqrt(Math.pow(xdiff, 2) + Math.pow(ydiff, 2));
        return distance;
    }
    
    //Does this object collide with a pickup
    public PickUp pickup_collides(Rectangle r){
        for(PickUp p: pickups){
            if(r.intersects(p)){
                return p;
            }
        }
        return null;
    }
    
    //Does this object collide with a door
    public Block door_collides(Rectangle r){
        for(Block b: blocks){
            if(b.getType() != Block.DOOR) continue;
            if(r.intersects(b)){
                return b;
            }
        }
        return null;
    }
    
    //Does this object collide with anything
    public Drawable collides(Rectangle r){
        for(Entity en: entities){
            if(r.intersects(en) && !r.equals(en)){
                return en;
            }
        }
        for(Block b: blocks){
            if(r.intersects(b)){
                return b;
            }
        }
        return null;
    }
    
    private void loadPlayer(){
        player = new Player();
        player.setGame(this);
        player.init(5*blockSize, 5*blockSize, 50, 100, 10);
        System.out.println("playercoords: " + player.getX()+", " + player.getY());
        entities.add(player);
        PickUp p = new PickUp(player.getX(), player.getY(), PickUp.BLOWDARTS);
        player.q_slot = p;
    }
    
    private void generateLevel(){
        int w = PLAYGROUND_WIDTH/blockSize; //Width as coordinates according to squares
        int h = PLAYGROUND_HEIGHT/blockSize;//Height as coordinates according to squares
        map = new Block[w][h];
        pickup_map = new PickUp[w][h];
        blocks = new ArrayList<>();
        Block block = new Block(10, 10, 1, 1, Block.WALL);
        blocks.add(block);
        for(int i=0; i<w; i++){
            //Left
            if(i <= h-1) {
                Block l = null;
                if(i == h/2 || i == (h/2)-1 || i == (h/2)+1){
                    l = new Block(0, i, 0.5, 1, Block.DOOR);
                }else{
                    l = new Block(0, i, 1, 1, Block.WALL);
                }
                map[0][i] = l;
                blocks.add(l);
            }
            //Left
            //Top
            if(i <= w-1) {
                Block t = null;
                if(i == (w/2)-1 || i == w/2 || i == (w/2)+1){
                    t = new Block(i, 0, 1, 0.5, Block.DOOR);
                }else{
                    t = new Block(i, 0, 1, 1, Block.WALL);
                }
                map[i][0] = t;
                blocks.add(t);
            }
            //Top
            //Right
            if(i <= h-1) {
                Block r = null;
                if(i == (h/2)-1 || i == h/2 || i == (h/2)+1){
                    r = new Block(w-0.5, i, 0.5, 1, Block.DOOR);
                }else{
                    r = new Block(w-1, i, 1, 1, Block.WALL);
                }
                map[w-1][i] = r;
                blocks.add(r);
            }
            //Right
            //Bottom
            if(i <= w-1){
                Block b = null;
                if(i == w/2 || i == (w/2)-1 || i == (w/2)+1){
                    b = new Block(i, h-0.5, 1, 0.5, Block.DOOR);
                }else{
                    b = new Block(i, h-1, 1, 1, Block.WALL);
                }
                map[i][h-1] = b;
                blocks.add(b);
            }
            //Bottom
            
        }
        pickups = new ArrayList<>();
        
        //Generate pickups
        while(pickups.size() < 10){
            int x = getRandom(1*blockSize, (int)((w-2)*blockSize));
            int y = getRandom(1*blockSize, (int)((h-2)*blockSize));
            if(pickup_map[x/blockSize][y/blockSize] == null){
                PickUp p = new PickUp(x, y, PickUp.TACO);
                pickups.add(p);
            }else{
                System.out.println("TACO CLASH!\nTACO TACO TACO BURRITO");
            }
        }
        
        //Generate zombies
        while(entities.size() < 10){
            int x = getRandom(1*blockSize, (int)((w-2)*blockSize));
            int y = getRandom(1*blockSize, (int)((h-2)*blockSize));
            if(collides(new Rectangle(x, y, blockSize, blockSize)) == null){
                Zombie z = new Zombie(x, y);
                entities.add(z);
            }else{
                System.out.println("ZOMBIE CLASH!");
            }
        }
        
    }
    
    public void addProjectile(Projectile p){
        projectiles.add(p);
    }
    
    
    public int getMousex(){
        return mousex;
    }
    public int getMousey(){
        return mousey;
    }

    public void addDrawable(Splat s) {
        drawables.add(s);
    }

    public void gameOver() {
        gameOver = true;
        s.setGameOver(true);
    }
    public void goToMenu() {
        inMenu = true;
        s.setInMenu(true);
    }
    private void setPaused(boolean b) {
        s.setPaused(b);
        paused = b;
    }
    
    class Key implements KeyListener{
        @Override
        public void keyPressed(KeyEvent pressed) {
            if(!paused){
                if(pressed.getKeyCode() == KeyEvent.VK_W){
                    player.setUp(true);
                }
                if(pressed.getKeyCode() == KeyEvent.VK_A){
                    player.setLeft(true);
                }
                if(pressed.getKeyCode() == KeyEvent.VK_S){
                    player.setDown(true);
                }
                if(pressed.getKeyCode() == KeyEvent.VK_D){
                    player.setRight(true);
                }
                if(pressed.getKeyCode() == KeyEvent.VK_Q){
                    player.useItem(0);
                }
                if(pressed.getKeyCode() == KeyEvent.VK_E){
                    player.useItem(1);
                }
                if(pressed.getKeyCode() == KeyEvent.VK_R){
                    player.useItem(2);
                }
                if(pressed.getKeyCode() == KeyEvent.VK_F){
                    player.useItem(3);
                }
                if(pressed.getKeyCode() == KeyEvent.VK_T){
                    player.useItem(4);
                }
            }
            
            if(pressed.getKeyCode() == KeyEvent.VK_ESCAPE){
                setPaused(!paused);
            }
        }
		
        @Override
        public void keyReleased(KeyEvent released) {
            if(released.getKeyCode() == KeyEvent.VK_W){
                player.setUp(false);
            }
            if(released.getKeyCode() == KeyEvent.VK_A){
                player.setLeft(false);
            }
            if(released.getKeyCode() == KeyEvent.VK_S){
                player.setDown(false);
            }
            if(released.getKeyCode() == KeyEvent.VK_D){
                player.setRight(false);
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        
    }
    
    class MouseListener implements MouseInputListener{
        @Override
        public void mouseDragged(MouseEvent e) {
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
            mousex = e.getX();
            mousey = e.getY();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(inMenu){
                mouse_click = true;
                for(Button b: menu.getButtons()){
                    if(b.contains(new Point(e.getX(), e.getY()))){
                        int action = b.getAction();
                        switch(action){
                            case Button.CONTINUE: 
                                System.out.println("CONTINUE");
                                return;
                            case Button.PLAY:
                                System.out.println("PLAY");
                                init_game();
                                return;
                            case Button.SETTINGS:
                                System.out.println("SETTINGS");
                                return;
                            case Button.HIGHSCORE:
                                System.out.println("HIGHSCORE");
                                return;
                            case Button.EXIT:
                                System.out.println("EXIT");
                                System.exit(0);
                                return;
                            default:
                                break;
                        }
                    }
                }
            }
            else if(paused){
                for(Button b: paused_menu.getButtons()){
                    if(b.contains(new Point(e.getX(), e.getY()))){
                        int action = b.getAction();
                        switch(action){
                            case Button.RESUME:
                                System.out.println("RESUME");
                                setPaused(false);
                                return;
                            case Button.SETTINGS:
                                System.out.println("SETTINGS");
                                return;
                            case Button.EXIT:
                                System.out.println("EXIT");
                                goToMenu();
                                return;
                            default:
                                break;
                        }
                    }
                }
            }
            else{
                attack = true;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            holding_click = true;
            hold_attack_time = System.nanoTime();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(!gameOver){
                attack_strength = (((int) (System.nanoTime()-hold_attack_time))-1)/(100-1);
                System.out.println("Attack_strength: " + attack_strength);
                holding_click = false;
                strong_attack = true;
            }
            
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
    
}
