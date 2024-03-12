package com.theemd.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;

public class CharacterSelectScreen implements Screen {

    final LauncherClass game;
    OrthographicCamera camera;
    Texture char1;
    Texture char2;
    Texture char3;
    Texture char4;
    int spriteWidth;
    int spriteHeight;

    public CharacterSelectScreen(final LauncherClass gam) {
        game = gam;

        char1 = new Texture(Gdx.files.internal("characterSelect1.png"));
        char2 = new Texture(Gdx.files.internal("characterSelect2.png"));
        char3 = new Texture(Gdx.files.internal("characterSelect3.png"));
        char4 = new Texture(Gdx.files.internal("characterSelect4.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        //this is placeholder code, these variables will ideally scale to the size of the screen
        //each sprite is 14 pixels tall and 12 pixels wide
        spriteHeight = 14*10;
        spriteWidth = 12*10;

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(194, 237, 180, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Please choose your character! Then press space to begin the game!", 100, 225);
        game.batch.draw(char1, 50, 300, spriteWidth, spriteHeight);
        game.batch.draw(char2, 250, 300, spriteWidth, spriteHeight);
        game.batch.draw(char3, 50, 50, spriteWidth, spriteHeight);
        game.batch.draw(char4, 250, 50, spriteWidth, spriteHeight);
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            game.setScreen((new HeslingtonHustle(game)));
            dispose();
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
        char1.dispose();
        char2.dispose();
        char3.dispose();
        char4.dispose();
    }
}
