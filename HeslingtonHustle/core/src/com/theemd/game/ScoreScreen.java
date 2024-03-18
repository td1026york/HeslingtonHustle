package com.theemd.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

/** Screen used to display the final score attained by the player at the end of the game
 * Allows the game to be restarted from the MainMenuScreen
 */
public class ScoreScreen implements Screen{
    final LauncherClass game;
    private OrthographicCamera camera;
    private int score;

    /**
     * Creates a new instance of ScoreScreen and returns it
     * @param game the final LauncherClass
     * @param s: the final score attained by the player
     */
    public ScoreScreen(final LauncherClass game, int s) {
        this.game = game;
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
