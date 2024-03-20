package com.theemd.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class PlayScreen extends ScreenAdapter {

    private String characterSelect;
    Player player;
    Sprite prompt; // for prompting player action when they are near a building
    TiledMap tiledMap;
    OrthogonalTiledMapRenderer tiledMapRenderer;
    OrthographicCamera camera;
    Matrix4 uiMatrix;
    Viewport viewport;
    LauncherClass game;
    //variables to track resources
    int sleepCount = 0;
    int eatCount = 0;
    int studyCount = 0;
    int recCount = 0;
    //variable to track the current score of the user
    int score = 30;
    int day = 0;
    int time = 0;
    long lastAction =0;

    float mapWidth = 40, mapHeight = 30;
    float mapScale = 32;// pixels to a tile (square in this case)
    float portWidth = 10, portHeight = 7.5f; // how much map is seen at once (1/4 in this case)

    BitmapFont font;
    SpriteBatch uiBatch;







    /**
     * Creates an instance of PlayScreen and returns it
     * @param game the LauncherClass used throughout the game
     * @param selection an integer used to determine which character has been selected by the user
     */
    public PlayScreen(LauncherClass game, int selection){
        this.game = game;
        characterSelect = Integer.toString(selection);
        // selection for which character from previous screen : collision for character collision detection - set in show method as map is not yet loaded
        player = new Player(selection);


    }

    @Override
    public void show() {
        tiledMap = new TmxMapLoader().load("Squaremap/Map.tmx"); // loads map
        tiledMap.getLayers().get("Collision").setOpacity(0); // sets collision layer transparent
        tiledMap.getLayers().get("Interaction").setOpacity(0); // sets interation layer transparent
        tiledMap.getLayers().get("Map").setOpacity(1); // sets map visible (maybe redundant)
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap,1/mapScale); // sets whole world as wide/tall as map - no need to work in pixels




        // spritesheet for character animations - contains four characters. Correct one is selected in player.animate(); - could pass selection here as opposed to constructor
        Texture characters = new Texture(Gdx.files.internal("Characters.png"));
        player.setSize(.5f,.5f); // set size of character (half a tile of the map in this case);
        player.animate(characters); // created animations in player based on the character the user has chosen
        player.setPosition(20,10f);
        player.setCollision((TiledMapTileLayer) tiledMap.getLayers().get("Collision"));
        player.setInteraction((TiledMapTileLayer) tiledMap.getLayers().get("Interaction"));
        Gdx.input.setInputProcessor(player); // player can now move themselves


        font = new BitmapFont();
        font.setColor(Color.BLUE);

        // created camera and viewport  - viewport as large as portWidth/Height
        camera = new OrthographicCamera( );
        viewport = new FitViewport(portWidth,portHeight,camera);
       // viewport = new FitViewport(40,30,camera);
        viewport.apply();
        camera.update(); // updates camera start to initial setup


        lastAction = System.currentTimeMillis()-5000;
        uiMatrix = camera.combined.cpy();

        uiBatch = new SpriteBatch();

        prompt = new Sprite();
        prompt.setSize(1,1);
        prompt.setTexture(new Texture(Gdx.files.internal("eat.png")));
        prompt.setRegion(0,0,109,122);




    }



    @Override
    public void render(float v) {
        game();
        // clear screen to render next frame
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        // renders map based on camera
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        tiledMapRenderer.getBatch().begin();




        // draws player passing how much time has passed since last frame for player movement and animation
        player.draw(tiledMapRenderer.getBatch(),v);
        prompt.setPosition(player.getX()+player.getWidth(), player.getY()+player.getHeight());
        if(player.eatDesire()){

            prompt.setTexture(new Texture(Gdx.files.internal("eat.png")));
            prompt.draw(tiledMapRenderer.getBatch());
        }else if(player.playDesire()){
            prompt.setTexture(new Texture(Gdx.files.internal("play.png")));
            prompt.draw(tiledMapRenderer.getBatch());
        }else if(player.studyDesire()){
            prompt.setTexture(new Texture(Gdx.files.internal("study.png")));
            prompt.draw(tiledMapRenderer.getBatch());
        }



        tiledMapRenderer.getBatch().end();




        /* - ensured camera stays within bound of map when player gets close to the edge
        // - checks if player(who is centred within camera), is within half the view width from the edge (meaning any further and the camera would be off the map)
        // - adds player width to right moving check (as x is increasing) as player is rendered from the bottom right
             (so player position starts there) in order to remove black bar when camera moves a player width too fat
            - X and Y are checked seperately so camera follows player if they are in top or bottom of map moving left
              or up and down if they are in edges of map
        */
        boolean x = player.getX() + portWidth/2 + player.getWidth() <= mapWidth && player.getX() - portWidth/2 > 0;
        boolean y = player.getY()+portHeight/2 +player.getWidth() < mapHeight && player.getY() - portHeight/2 >0; // same as above for y im not typing that out again
        viewport.apply();
        if(x){
            camera.position.set(player.getX() + player.getWidth() / 2, camera.position.y, 0);


        }
        if(y) {
            camera.position.set(camera.position.x, player.getY() + player.getHeight() / 2, 0);


        }

        camera.update(); // updated camera - changes position if above was true




        uiMatrix.setToOrtho2D(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiBatch.setProjectionMatrix(uiMatrix);

        uiBatch.begin();



        font.draw(uiBatch, "Score: " + score ,0, Gdx.graphics.getHeight());
        font.draw(uiBatch, "Energy: " + player.getEnergy() ,Gdx.graphics.getWidth()/4f, Gdx.graphics.getHeight());
        font.draw(uiBatch, "Time left Today: " + time ,Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight());
        font.draw(uiBatch, "Day: " + day +"/7" ,Gdx.graphics.getWidth()/1.3333f, Gdx.graphics.getHeight());

        font.draw(uiBatch, "Sleep Count: " + sleepCount ,0, 15);
        font.draw(uiBatch, "Study Count: " + studyCount ,Gdx.graphics.getWidth()/4f, 15);
        font.draw(uiBatch, "Recreation Count: " + recCount ,Gdx.graphics.getWidth()/2f, 15);
        font.draw(uiBatch, "Eat Count: " + eatCount ,Gdx.graphics.getWidth()/1.3333f, 15);




        uiBatch.end();



        //renderInteractableAreas(); Dont uncomment it crashes

    }


    public void game(){
        if(day==8){

            // game finished
        }

        if(time <=0){
            day ++;
            player.setEnergy(10);
            time = 16;
        }

        if(player.isAction()&& System.currentTimeMillis() - lastAction > 5000){
            if(player.eatDesire()&& player.getEnergy()>=1){

                eatCount++;
                player.setEnergy(player.getEnergy()-1);
                time-=2;
                lastAction = System.currentTimeMillis();

            }
            if(player.studyDesire()&& player.getEnergy()>=3){

                studyCount++;
                player.setEnergy(player.getEnergy()-2);
                time-=3;
                lastAction = System.currentTimeMillis();

            }
            if(player.playDesire()&& player.getEnergy()>=2){

                eatCount++;
                player.setEnergy(player.getEnergy()-2);
                time-=2;
                lastAction = System.currentTimeMillis();

            }



        }





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

    private void renderInteractableAreas(){
        int objectLayerID = 5;
        TiledMapTileLayer ObjectLayer = (TiledMapTileLayer)tiledMap.getLayers().get(objectLayerID);
        MapObjects InteractableAreas = ObjectLayer.getObjects();

        for (RectangleMapObject space : InteractableAreas.getByType(RectangleMapObject.class)){

            Rectangle area = space.getRectangle();
            if (Intersector.overlaps(area, player.getBoundingRectangle())){
                MapProperties properties = space.getProperties();
                String name = properties.get("name",String.class);
                System.out.println(name);
                InteractableArea interactableArea = new InteractableArea();
                interactableArea.collidedWithArea(name);

            }
        }
    }
}
