package com.theemd.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

/** Screen displaying text explaining how to play the game
 */
public class HelpScreen implements Screen{
    final LauncherClass game;
    private OrthographicCamera camera;

    /**
     * Creates amd returns an instance of the HelpScreen
     * @param game the final LauncherClass used throughout the game
     */
    public HelpScreen(final LauncherClass game) {
        this.game = game;

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
        game.font.getData().setScale(1.9f,1.9f);
        game.font.draw(game.batch, "HELP SCREEN", 300, 450);
        game.font.getData().setScale(1.2f,1.2f);
        game.font.draw(game.batch, "* Move around the map using using WASD", 20, 420);
        game.font.draw(game.batch, "* Press E when close to a building to perform an activity", 20, 400);
        game.font.draw(game.batch, "* There are 4 types of activities: ", 20, 380);
        game.font.draw(game.batch, "1: Studying - make sure you study enough to do well! But don't overwork yourself! ", 40, 360);
        game.font.draw(game.batch, "2: Recreational activities  - ensure you make time for yourself to relax and have fun! ", 40, 340);
        game.font.draw(game.batch, "3: Eating - don't forget to eat enough! ", 40, 320);
        game.font.draw(game.batch, "4: Sleeping - rest is important, especially when an exam is close! ", 40, 300);
        game.font.draw(game.batch, "* Activities consume time and cost energy to do, so manage both resources carefully!", 20, 280);
        game.font.draw(game.batch, "* The aim of the game is to get as high of a score on your exam as possible!", 20, 260);
        game.font.draw(game.batch, "* You have 7 days, each day has 16 hours: have fun!", 20, 240);
        game.font.draw(game.batch, "* The game can be muted by pressing [ ] or by pressing the mute button on the interface", 20, 70);
        game.font.draw(game.batch, "* Press ESCAPE to return to the main menu", 20, 50);
        game.batch.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
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
