package com.theemd.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class ScoreScreen implements Screen{
    final LauncherClass game;
    private OrthographicCamera camera;
    private int score;

    public ScoreScreen(final LauncherClass gam, int s) {
        game = gam;
        score = s;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

        ScreenUtils.clear(194, 237, 180, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.setColor(Color.BLACK);
        game.font.draw(game.batch, "Final score: " + score + "/100", 20, 150);
        game.font.draw(game.batch, "Press ENTER play again!", 20, 50);
        game.batch.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new MainMenuScreen(game));
            this.dispose();
        }

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
