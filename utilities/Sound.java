/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import pkg2dgame.Main;

/**
 *
 * @author joel_
 */
public class Sound {

    private Clip clip;
    private AudioInputStream inputStream;
    private String soundFile;
    
    static ArrayList<AudioInputStream> cachedSounds = new ArrayList<>();
    
    public static final int BLOWDARTS = 0;
    public static final int EATING_CRUNCHY = 1;
    public static final int SWORD = 2;
    public static final int ZOMBIE_AGGRO_1 = 2;
    public static final int ZOMBIE_IDLE_1 = 3;
    public static final int ZOMBIE_IDLE_2 = 4;
    public static final int ZOMBIE_IDLE_3 = 5;
    public static final int ZOMBIE_HURT_1 = 6;
    public static final int ZOMBIE_HURT_2 = 7;
    
    
    
    public Sound(String soundFile){
        this.soundFile = soundFile;
        try {
            clip = AudioSystem.getClip();
            inputStream = AudioSystem.getAudioInputStream(
                    Main.class.getResourceAsStream("/resources/sounds/" + soundFile));
            clip.open(inputStream);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        /*
        File[] audioFiles = new File("./resources/sounds").listFiles();
        System.out.println("utilities.Sound.<init>() audioFiles: " + audioFiles);
        for (File file : audioFiles)
        {
            AudioInputStream reusableAudioInputStream = null;
            try {
                reusableAudioInputStream = createReusableAudioInputStream(file);
            } catch (IOException ex) {
                Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedAudioFileException ex) {
                Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
            }
            cachedSounds.add(reusableAudioInputStream);
        }
        */
    }
    
    public void play(){
        clip.start();
        reset();
    }
    
    public void reset(){
        Thread t = new Thread(){
            public void run() {
                Thread.yield();
                System.out.println(".run()1");
                try {
                    sleep(clip.getMicrosecondLength());
                } catch (InterruptedException ex) {
                    Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println(".run()2");
                try {
                    clip.stop();
                    clip.close();
                    clip = AudioSystem.getClip();
                    inputStream = AudioSystem.getAudioInputStream(
                            Main.class.getResourceAsStream("/resources/sounds/" + soundFile));
                    clip.open(inputStream);
                } catch (IOException ex) {
                    Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
                } catch (LineUnavailableException ex) {
                    Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedAudioFileException ex) {
                    Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
                }
                Thread.currentThread().interrupt();
            }
            
        };
        t.start();
        
    }
    


    public void playCachedSound(int i) 
        throws IOException, LineUnavailableException
    {
        AudioInputStream stream = cachedSounds.get(i);
        stream.reset();
        Clip clip = AudioSystem.getClip();
        clip.open(stream);
        clip.start();
    }

    private static AudioInputStream createReusableAudioInputStream(File file) 
        throws IOException, UnsupportedAudioFileException
    {
        AudioInputStream ais = null;
        try
        {
            ais = AudioSystem.getAudioInputStream(file);
            byte[] buffer = new byte[1024 * 32];
            int read = 0;
            ByteArrayOutputStream baos = 
                new ByteArrayOutputStream(buffer.length);
            while ((read = ais.read(buffer, 0, buffer.length)) != -1)
            {
                baos.write(buffer, 0, read);
            }
            AudioInputStream reusableAis = 
                new AudioInputStream(
                        new ByteArrayInputStream(baos.toByteArray()),
                        ais.getFormat(),
                        AudioSystem.NOT_SPECIFIED);
            return reusableAis;
        }
        finally
        {
            if (ais != null)
            {
                ais.close();
            }
        }
    }
}
    
