package com.theemd.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class PlayScreen extends ScreenAdapter {

    private String characterSelect;
    Player player;
    TiledMap tiledMap;
    HexagonalTiledMapRenderer tiledMapRenderer;
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

        Texture test = new Texture(Gdx.files.internal("Characters.png"));
        player.setSize(.5f,.5f);
        player.setTexture(test);
        player.setPosition(10,7.5f);



        tiledMap = new TmxMapLoader().load("mainMap.tmx");
        tiledMapRenderer = new HexagonalTiledMapRenderer(tiledMap,1/120f);
        int x = tiledMap.getProperties().get("width",Integer.class) * tiledMap.getProperties().get("tilewidth",Integer.class);
        int y = tiledMap.getProperties().get("height",Integer.class) * tiledMap.getProperties().get("tileheight",Integer.class);
        camera = new OrthographicCamera( );
        viewport = new FitViewport(5 ,5,camera);
        viewport.apply();

        // camera.position.set(0.75f,  1f,0);
        // camera.zoom = 0.5f;
        camera.update();
        // sets up camera
        player.animate(test);
        Gdx.input.setInputProcessor(player);

    }

   /* private void handleInput() {
        // Zoom in with the up arrow key
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.zoom -= ZOOM_SPEED;
        }
        // Zoom out with the down arrow key
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.zoom += ZOOM_SPEED;
        }
    }
*/

    @Override
    public void render(float v) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        tiledMapRenderer.getBatch().begin();




        player.draw(tiledMapRenderer.getBatch(),v);

        Vector2 max = new Vector2(20,15);

        tiledMapRenderer.getBatch().end();
        if(player.getX()<=18 && player.getX()>=2 ) {
            camera.position.set(player.getX() + player.getWidth() / 2, camera.position.y, 0);
        }
        if(player.getY()>=2 && player.getY()<=11) {
            camera.position.set(camera.position.x, player.getY() + player.getHeight() / 2, 0);
        }
        System.out.println(player.getX());
        System.out.println(player.getY());
        camera.update();

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
