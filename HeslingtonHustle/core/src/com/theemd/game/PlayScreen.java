package com.theemd.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class PlayScreen extends ScreenAdapter {

    private String characterSelect;
    Player player;
    TiledMap tiledMap;
    OrthogonalTiledMapRenderer tiledMapRenderer;
    OrthographicCamera camera;
    Viewport viewport;
    LauncherClass game;


    private static final float ZOOM_SPEED = 0.1f;
    public PlayScreen(LauncherClass game, int selection){
        this.game = game;
        characterSelect = Integer.toString(selection);
        player = new Player(selection);


    }

    @Override
    public void show() {
        Texture test = new Texture(Gdx.files.internal("Putt Putt Chars.png"));
        player.setSize(.5f,.5f);
        player.setTexture(test);
        player.setPosition(1,1);



        tiledMap = new TmxMapLoader().load("test.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap,1/32f);
        int x = tiledMap.getProperties().get("width",Integer.class) * tiledMap.getProperties().get("tilewidth",Integer.class);
        int y = tiledMap.getProperties().get("height",Integer.class) * tiledMap.getProperties().get("tileheight",Integer.class);
        camera = new OrthographicCamera( );
        viewport = new FitViewport(1 ,1,camera);
        viewport.apply();

        // camera.position.set(0.75f,  1f,0);
        // camera.zoom = 0.5f;
        camera.update();
        // sets up camera


    }

    private void handleInput() {
        // Zoom in with the up arrow key
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.zoom -= ZOOM_SPEED;
        }
        // Zoom out with the down arrow key
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.zoom += ZOOM_SPEED;
        }
    }


    @Override
    public void render(float v) {
        handleInput();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        tiledMapRenderer.getBatch().begin();

        player.draw(tiledMapRenderer.getBatch());
        //tiledMapRenderer.getBatch().draw(player.getTexture() ,1,1,.5f,.5f);

        tiledMapRenderer.getBatch().end();


    }

    @Override
    public void resize(int i, int i1) {
        viewport.update(i,i1);
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
