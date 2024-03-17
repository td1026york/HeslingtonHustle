package com.theemd.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LauncherClass extends Game {
//ideally rename this to HeslingtonHustle.java and change in DesktopLauncher
    /** LauncherClass to be run first when the game is opened.
     * Creates a new instance of the class MainMenuScreen and then sets the current screen to it.
     */
    SpriteBatch batch;
    BitmapFont font;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        // Creates the first instance of the Main Menu Screen and then displays it.
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