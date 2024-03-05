package org.example.Custom_Elements;

import org.example.Config.CONFIG;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class Sound {

    public static void PlayOofSound(){

        URL file = null;

        try {
            file = new URL("file:src/Package_Sounds/Shoot.wav");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }

        Clip clip = null;

        try {
            if (file != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(ais);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (clip != null) {
            clip.setFramePosition(0);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-CONFIG.SHOOTING_VOLUME);

            clip.start();
        }
    }
    public static void PlayBackgroundSound(){

        URL file = null;

        try {
            file = new URL("file:src/Package_Sounds/Background.wav");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }

        Clip clip = null;

        try {
            if (file != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(ais);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(100000);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-CONFIG.BACKGROUND_VOLUME);

            clip.start();
            System.out.println(clip.getLevel());
        }
    }

}
