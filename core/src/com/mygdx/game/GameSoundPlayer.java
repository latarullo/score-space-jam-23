package com.mygdx.game;

import com.badlogic.gdx.audio.Sound;

public abstract class GameSoundPlayer {
    public static void playMusic(Sound music) {
        music.loop();
//        if (GameSettings.getInstance().isMusicEnabled()) {
//            music.loop(GameSettings.getInstance().getMusicVolume());
//        }
    }

    public static void playSound(Sound sound) {
        sound.play();
//        if (GameSettings.getInstance().isSoundEnabled()) {
//            sound.play(GameSettings.getInstance().getSoundVolume());
//        }
    }

    public static void stop (Sound sound){
        sound.stop();
    }
}
