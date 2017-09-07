/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import pkg2dgame.Game;
import static pkg2dgame.Game.getRandom;

/**
 *
 * @author joel_
 */
public class Room {
    
    private static ArrayList<Block> blocks = new ArrayList();
    private static ArrayList<Entity> entities = new ArrayList();
    private static ArrayList<PickUp> pickups = new ArrayList();
    
    ArrayList<Point> taken_points = new ArrayList();
    private int difficulty; //1-9
    
    public Room(int difficulty){
        this.difficulty = difficulty;
        if(blocks != null) this.blocks = blocks;
        int blockSize = Game.getBlockSize();
        int w = Game.getWidth()/blockSize; //Width as coordinates according to squares
        int h = Game.getHeight()/blockSize;//Height as coordinates according to squares
        
        
        Block block = new Block(10, 10, 1, 1, Block.WALL);
        blocks.add(block);
        
        //Generate walls of the room
        for(int i=0; i<w; i++){
        //Left
            if(i <= h-1) {
                Block l = null;
                if(i == h/2 || i == (h/2)-1 || i == (h/2)+1){
                    l = new Block(0, i, 0.5, 1, Block.DOOR);
                }else{
                    l = new Block(0, i, 1, 1, Block.WALL);
                }
                //map[0][i] = l;
                blocks.add(l);
                taken_points.add(new Point(0,i));
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
                //map[i][0] = t;
                blocks.add(t);
                taken_points.add(new Point(i,0));
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
                //map[w-1][i] = r;
                blocks.add(r);
                taken_points.add(new Point(w-1,i));
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
                //map[i][h-1] = b;
                blocks.add(b);
                taken_points.add(new Point(i,h-1));
            }
            //Bottom
            //Generate random blocks in room
            //TODO

            //Generate enemies
            //Generate pickups
            pickups = new ArrayList<>();
            while(pickups.size() < 10){
                int x = getRandom(1*blockSize, (int)((w-2)*blockSize));
                int y = getRandom(1*blockSize, (int)((h-2)*blockSize));
                if(taken_points_contains(new Point((x/blockSize),(y/blockSize))) == false){
                    PickUp p = new PickUp(x, y, PickUp.TACO);
                    pickups.add(p);
                    taken_points.add(new Point((x/blockSize),(y/blockSize)));
                }else{
                    System.out.println("TACO CLASH!\nTACO TACO TACO BURRITO");
                }
            }

            //Generate zombies
            while(entities.size() < 10){
                int x = getRandom(1*blockSize, (int)((w-2)*blockSize));
                int y = getRandom(1*blockSize, (int)((h-2)*blockSize));
                if(Game.collides(new Rectangle(x, y, blockSize, blockSize), 0) == null){
                    Zombie z = new Zombie(x, y);
                    entities.add(z);
                }else{
                    System.out.println("ZOMBIE CLASH!");
                }
            }

        }
    }
    
    public static ArrayList<Block> getBlocks(){
        return blocks;
    }
    public static ArrayList<Entity> getEntities(){
        return entities;
    }
    public static ArrayList<PickUp> getPickups(){
        return pickups;
    }
    
    private boolean taken_points_contains(Point point){
        
        for(Point p: taken_points){
            if(point.x == p.x && point.y == p.y) return true;
        }
        
        return false;
    }

    public static void addPickup(PickUp drop) {
        pickups.add(drop);
    }
    
    
}
