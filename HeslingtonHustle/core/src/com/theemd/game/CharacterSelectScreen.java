package com.theemd.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class CharacterSelectScreen implements Screen {

    final LauncherClass game;
    private OrthographicCamera camera;
    private int spriteWidth;
    private int spriteHeight;
    private int screenSizeX;
    private int screenSizeY;
    private int selectedChar = 0;
    private Stage charStage;
    private FitViewport viewport;
    private Texture[] charTexture;

    public CharacterSelectScreen(final LauncherClass gam) {
        game = gam;

        charTexture = new Texture[4];

        for(int i = 0; i < 4; i++){
            charTexture[i] = new Texture(Gdx.files.internal("characterSelect" + (i+1) +".png"));
        }
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        //this is placeholder code, these variables will ideally scale to the size of the screen
        //each sprite is 14 pixels tall and 12 pixels wide
        spriteHeight = 14*10;
        spriteWidth = 12*10;

        //need to be updated later to be set to the current size of the screen to allow for resizing.
        screenSizeX = 800;
        screenSizeY = 480;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenSizeX, screenSizeY);

        viewport = new FitViewport(screenSizeX, screenSizeY, camera);
        charStage = new Stage(viewport);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(194, 237, 180, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Please choose your character! Then press space to begin the game!", 100, 300);
        game.batch.draw(charTexture[selectedChar], (float) (screenSizeX - spriteWidth) /2, (float) (screenSizeY - spriteHeight) /2, spriteWidth, spriteHeight);
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            HeslingtonHustle myMap = new HeslingtonHustle(game);
            myMap.setCharacter(selectedChar);
            game.setScreen(myMap);
            dispose();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if(selectedChar < 3) {
                selectedChar++;
            }
            else {
                selectedChar = 0;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if(selectedChar > 0) {
                selectedChar--;
            }
            else {
                selectedChar = 3;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        screenSizeX = Gdx.graphics.getWidth();
        screenSizeY = Gdx.graphics.getWidth();
        viewport.update(width, height);
        charStage.getViewport().update(width, height, true);
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
        for(Texture t : charTexture){
            t.dispose();
        }
        charStage.dispose();
    }
}
