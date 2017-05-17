/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.ArrayList;
import objects.Entity;
import pkg2dgame.Game;

/**
 *
 * @author joel_
 */
public class GetSound {
    ArrayList<Sound> zombie_hurt;
    private GetSound instance;
    
    private GetSound(){
    }
    
    public void init(ArrayList<Sound> zombie_hurt){
        if(instance == null){
            instance = new GetSound();
        }
        this.zombie_hurt = zombie_hurt;
    }
    
    public Sound getHurt(int type){
        if(type == (Entity.ZOMBIE) && zombie_hurt != null){
            return zombie_hurt.get(Game.getRandom(0,zombie_hurt.size()));
        }
        return null;
    }
}
