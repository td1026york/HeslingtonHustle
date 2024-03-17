package com.theemd.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;


public class PlayScreen extends ScreenAdapter {

    private String characterSelect;
    LauncherClass game;
    public PlayScreen(LauncherClass game, int selection){
        this.game = game;
        characterSelect = Integer.toString(selection);


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
