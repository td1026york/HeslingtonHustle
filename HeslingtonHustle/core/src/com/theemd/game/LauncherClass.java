package com.theemd.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/**
 * LauncherClass to be run first when the game is opened.
 * Creates a new instance of the class MainMenuScreen and then sets the current screen to it.
 */
public class LauncherClass extends Game implements ApplicationListener {
    SpriteBatch batch;
    BitmapFont font;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        // Creates the first instance of the Main Menu Screen and then displays it.
        Resources.load();
        this.setScreen(new Splash(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

}