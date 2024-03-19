package com.theemd.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.lang.Math;
import java.security.PublicKey;

public class Splash extends ScreenAdapter {


    // for fading logo in and out
    private float elapsedTime;
    private boolean fadeIn ;
    private float fadeInDuration ;
    private float fadeOutDuration ;
    float alpha;



    // drawing
    private SpriteBatch batch;
    private Sprite splash;

    Viewport view;
    OrthographicCamera camera;
    MainMenuScreen menu;

    LauncherClass game;
    public Splash(LauncherClass game){
        this.game=game;
    }
    @Override
    public void show() {
        elapsedTime = 0;
        fadeIn = true;

        // set how long for fade in and fade out in seconds
        fadeInDuration = 2f;
        fadeOutDuration = 4f;


        camera = new OrthographicCamera(); //camera

        // set aspect ratio of scene to 16:9 and applies
        view =  new FitViewport(16,9,camera);
        view.apply();
        // centres camera to scene
        camera.position.set(view.getWorldWidth()/2,view.getWorldHeight()/2,0);


        // gets texture of theemed logo and applies to sprite "splash"
        batch = new SpriteBatch();
        Texture splashTexture = new Texture(Gdx.files.internal("THEEMD-logo.jpg"));
        splash = new Sprite(splashTexture);


    }

    @Override
    public void render(float delta) {
        //update camera
        camera.update();

        //clear screen to re-render
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // set batch coordinates matrix to viewports/camera coords
        batch.setProjectionMatrix(camera.combined);



        /*
            Fade in effect code
            Max value of alpha (opacity) is 1
             - elapsedTime/FadeDuration gives percentage of how far through effect we currently are
                        - 2 seconds elapsed / 4 seconds total is 0.5 (50%)

         */
        elapsedTime+=delta;
        if(fadeIn){
            alpha = Math.min(elapsedTime/fadeInDuration,1f);
            if (elapsedTime>=fadeInDuration) {
                elapsedTime = 0; fadeIn = false; // resets and begins to fade out once fade in duration is reached
            }
        }else{
            alpha = Math.max(1f - elapsedTime/fadeOutDuration,0f);
            if (elapsedTime>=fadeOutDuration) {
                //  elapsedTime = 0; fadeIn = true; // resets and fades back in once time is done - could be used to fade in and out multiple times before starting gam
                game.setScreen(new MainMenuScreen(this.game));
            }
        }



        // setting size of logo and centering
        float logoHeight = 3;
        float logoWidth = 3;
        float x = (view.getWorldWidth() - logoWidth)/2;
        float y = (view.getWorldHeight() - logoHeight)/2;



        batch.begin();
        batch.setColor(1, 1, 1, alpha);
        batch.draw(splash,x,y,logoWidth,logoHeight);
        batch.end();

        Music music = Gdx.audio.newMusic(Gdx.files.internal("menuMusic.wav"));
        music.setVolume(0.5f);
        music.setLooping(true);
        music.play();
    }

    @Override
    public void resize(int width, int height) {
        view.update(width,height);
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
