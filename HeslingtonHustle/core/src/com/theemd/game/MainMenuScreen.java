package com.theemd.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final LauncherClass game;
    private OrthographicCamera camera;

    public MainMenuScreen(final LauncherClass gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(194, 237, 180, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.setColor(Color.BLACK);
        game.font.draw(game.batch, "Welcome to Heslington Hustle ", 100, 150);
        game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
        game.font.draw(game.batch, "Press H to view the help screen!", 100, 50);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen((new CharacterSelectScreen(game)));
            dispose();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            game.setScreen((new HelpScreen(game)));
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
