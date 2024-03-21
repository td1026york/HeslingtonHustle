package com.theemd.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * Contains music for the game
 */
public class Resources {
    public static Music music;
    public Resources() {}
    public static void load() {
        music = Gdx.audio.newMusic(Gdx.files.internal("menuMusic.wav"));
        music.setVolume(0.5f);
        music.setLooping(true);
    }

}
