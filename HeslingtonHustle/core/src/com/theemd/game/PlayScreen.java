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

    float mapWidth = 48, mapHeight = 32;
    float mapScale = 16;// pixels to a tile (square in this case)
    float portWidth = 15, portHeight = 12.5f; // how much map is seen at once (1/4 in this case)

    BitmapFont font;
    SpriteBatch uiBatch;

    // InteractableLocations
    InteractableLocation[] locations;
    final static int EAT_LOCATION = 0;
    final static int SLEEP_LOCATION = 1;
    final static int RECREATION_LOCATION = 2;
    final static int STUDY_LOCATION = 3;



    /**
     * Creates an instance of PlayScreen and returns it
     * @param game the LauncherClass used throughout the game
     * @param selection an integer used to determine which character has been selected by the user
     */
    public PlayScreen(LauncherClass game, int selection){
        this.game = game;
        // selection for which character from previous screen : collision for character collision detection - set in show method as map is not yet loaded
        player = new Player(selection);

        locations = new InteractableLocation[4];
        //create new locations and add their activities
        locations[EAT_LOCATION] = new InteractableLocation();
        locations[EAT_LOCATION].addActivity(1,2,2,"Eat");

        locations[SLEEP_LOCATION] = new InteractableLocation();
        //sleep takes 8 hours
        locations[SLEEP_LOCATION].addActivity(0,3,8,"Go to bed");

        locations[RECREATION_LOCATION] = new InteractableLocation();
        locations[RECREATION_LOCATION].addActivity(3,2,2,"Have fun");

        locations[STUDY_LOCATION] = new InteractableLocation();
        locations[STUDY_LOCATION].addActivity(3,10,3,"Study");
    }

    @Override
    public void show() {
        tiledMap = new TmxMapLoader().load("Squaremap/Map.tmx"); // loads map
        tiledMap.getLayers().get("Collision").setOpacity(0); // sets collision layer transparent
        tiledMap.getLayers().get("Interaction").setOpacity(0); // sets interation layer transparent
        //tiledMap.getLayers().get("Map").setOpacity(1); // sets map visible (maybe redundant)
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap,1/mapScale); // sets whole world as wide/tall as map - no need to work in pixels

        // spritesheet for character animations - contains four characters. Correct one is selected in player.animate(); - could pass selection here as opposed to constructor
        Texture characters = new Texture(Gdx.files.internal("Characters.png"));
        player.setSize(1f,1f); // set size of character (half a tile of the map in this case);
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

        // grabs the different layers as they need to be rendered at different times
        int[] bottomLayers = {
                        tiledMap.getLayers().getIndex("Collision"),
                        tiledMap.getLayers().getIndex("Interaction"),
                        tiledMap.getLayers().getIndex("Floors")};
        int[] middleLayers = {tiledMap.getLayers().getIndex("ShortObjects")};
        int[] topLayers = {tiledMap.getLayers().getIndex("TallObjects")};


        tiledMapRenderer.setView(camera);

        // render bottom and middle layers
        tiledMapRenderer.render(bottomLayers);
        tiledMapRenderer.render(middleLayers);
        tiledMapRenderer.getBatch().begin();



        // draws player passing how much time has passed since last frame for player movement and animation
        player.draw(tiledMapRenderer.getBatch(),v);
        tiledMapRenderer.getBatch().end();

        // render remaining layers
        tiledMapRenderer.render(topLayers);

        tiledMapRenderer.getBatch().begin();
        prompt.setPosition(player.getX()+player.getWidth(), player.getY()+player.getHeight());

        if(player.eatDesire()){
            prompt.setTexture(new Texture(Gdx.files.internal("eat.png")));
            prompt.draw(tiledMapRenderer.getBatch());
        }
        else if(player.playDesire()){
            prompt.setTexture(new Texture(Gdx.files.internal("play.png")));
            prompt.draw(tiledMapRenderer.getBatch());
        }
        else if(player.studyDesire()){
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



        font.draw(uiBatch, "Predicted Score: " + score ,0, Gdx.graphics.getHeight());
        font.draw(uiBatch, "Energy: " + player.getEnergy() ,Gdx.graphics.getWidth()/4f, Gdx.graphics.getHeight());
        font.draw(uiBatch, "Time left Today: " + time ,Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight());
        font.draw(uiBatch, "Day: " + day +"/7" ,Gdx.graphics.getWidth()/1.3333f, Gdx.graphics.getHeight());

        font.draw(uiBatch, "Sleep Count: " + sleepCount ,0, 15*(Gdx.graphics.getHeight()/600f));
        font.draw(uiBatch, "Study Count: " + studyCount ,Gdx.graphics.getWidth()/4f, 15*(Gdx.graphics.getHeight()/600f));
        font.draw(uiBatch, "Recreation Count: " + recCount ,Gdx.graphics.getWidth()/2f, 15*(Gdx.graphics.getHeight()/600f));
        font.draw(uiBatch, "Eat Count: " + eatCount ,Gdx.graphics.getWidth()/1.3333f, 15*(Gdx.graphics.getHeight()/600f) );




        uiBatch.end();



        //renderInteractableAreas(); Dont uncomment it crashes

    }


    public void game(){
        if(day==8){
            game.setScreen(new ScoreScreen(game, score));
        }

        if(time <=0){
            day ++;
            player.setEnergy(10);
            time = 16;
        }

        if(player.isAction()&& System.currentTimeMillis() - lastAction > 5000){
            if(player.eatDesire()&& player.getEnergy()>=1){
                Activity eatActivity = locations[EAT_LOCATION].getActivity(0);
                if(eatActivity.checkActivity(player.getEnergy(), time)){
                    eatCount++;
                    player.setEnergy(player.getEnergy()-eatActivity.getEnergyCost());
                    time -= eatActivity.getTimeCost();
                    score += eatActivity.getScoreImpact();

                    lastAction = System.currentTimeMillis();
                }
            }

            if(player.studyDesire()&& player.getEnergy()>=3){
                Activity studyActivity = locations[STUDY_LOCATION].getActivity(0);
                if(studyActivity.checkActivity(player.getEnergy(), time)){
                    studyCount++;

                    player.setEnergy(player.getEnergy()-studyActivity.getEnergyCost());
                    time -= studyActivity.getTimeCost();
                    score += studyActivity.getScoreImpact();

                    lastAction = System.currentTimeMillis();
                }
            }

            if(player.playDesire()&& player.getEnergy()>=2){
                Activity funActivity = locations[RECREATION_LOCATION].getActivity(0);
                if(funActivity.checkActivity(player.getEnergy(), time)){
                    recCount++;
                    player.setEnergy(player.getEnergy()-funActivity.getEnergyCost());
                    time -= funActivity.getTimeCost();
                    score += funActivity.getScoreImpact();

                    lastAction = System.currentTimeMillis();
                }
            }


        }

    }

    @Override
    public void resize(int i, int i1) {
        viewport.update(i,i1);
        font.getData().setScale(Gdx.graphics.getWidth()/800f,Gdx.graphics.getHeight()/600f);

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
    //this seems to never be used at present
    //combine InteractableArea and InteractableLocation
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
