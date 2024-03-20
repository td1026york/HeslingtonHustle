package com.theemd.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.*;


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
    int score = 0;
    int day = 0;
    int time = 0;
    long lastAction =0;

    boolean energy = true;
    float energyRenderTimer; // for timing how long to change energy to red when they try an action with no energy

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
        locations[SLEEP_LOCATION].addActivity(-3,0,8,"Go to bed");

        locations[RECREATION_LOCATION] = new InteractableLocation();
        locations[RECREATION_LOCATION].addActivity(3,2,2,"Have fun");

        locations[STUDY_LOCATION] = new InteractableLocation();
        locations[STUDY_LOCATION].addActivity(6,10,3,"Study");
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

        energyRenderTimer = 0;

    }



    @Override
    public void render(float v) {
        // runs the game function for user interaction. A tick rate of 60 probably. Better than the terrible fortnite servers
        game();
        // clear screen to render next frame
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();


        // grabs the different layers as they need to be rendered at different times
        int[] bottomLayers = {
                        tiledMap.getLayers().getIndex("Collision"),
                        tiledMap.getLayers().getIndex("Interaction"),
                        tiledMap.getLayers().getIndex("Floors")};
        int[] middleLayers = {tiledMap.getLayers().getIndex("ShortObjects")};
        int[] topLayers = {tiledMap.getLayers().getIndex("TallObjects")};

        // renders map based on camera
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


        // for drawing moving sprites
        tiledMapRenderer.getBatch().begin();
        // prompt  for telling user to interact with building when they get close
        // set directly next to the top right of the player character
        prompt.setPosition(player.getX()+player.getWidth(), player.getY()+player.getHeight());
        // sets the texture of the prompt depending on which interactable location the player is currently near
        // the prompt is only drawn when a player is near a location
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
        else if(player.sleepDesire()){

            prompt.setTexture(new Texture(Gdx.files.internal("sleep.png")));
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



        // sets the transformation matrix for drawing the ui simply to the pixel height and width of the screen
        uiMatrix.setToOrtho2D(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiBatch.setProjectionMatrix(uiMatrix);

        // drawing the ui with seperate batch
        // needed to be done as text doesn't scale well at the scale the game viewpoint is using
        uiBatch.begin();


        // these are drawn at the top, with the screen seperated into 4 for them
        font.draw(uiBatch, "Predicted Score: " + score ,0, Gdx.graphics.getHeight());
        // when they try an action with no energy
        if (!energy) {
            energyRenderTimer +=v; // increments timer by time since last framew
            font.setColor(Color.RED); // sets font color to red
            if (energyRenderTimer>=2f){
                // after two seconds pass of energy being red
                font.setColor(Color.BLUE); // sets font back to blue
                energy = true; // sets this to true so they have to try agaijn for it to turn red again
                energyRenderTimer =0; // resets timer
            }
        }
        font.draw(uiBatch, "Energy: " + player.getEnergy() ,Gdx.graphics.getWidth()/4f, Gdx.graphics.getHeight());
        font.setColor(Color.BLUE);
        font.draw(uiBatch, "Time left Today: " + time ,Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight());
        font.draw(uiBatch, "Day: " + day +"/7" ,Gdx.graphics.getWidth()/1.3333f, Gdx.graphics.getHeight());


        // these are drawn at the bottom
        // they are drawn 15 pixels above the bottom with a screen size of 800x600.
        // they need to be drawn higher with higher screen res so they don't draw offscreen as the font size scales with screen size (in the resize function)

        font.draw(uiBatch, "Sleep Count: " + sleepCount ,0, 15*(Gdx.graphics.getHeight()/600f));
        font.draw(uiBatch, "Study Count: " + studyCount ,Gdx.graphics.getWidth()/4f, 15*(Gdx.graphics.getHeight()/600f));
        font.draw(uiBatch, "Recreation Count: " + recCount ,Gdx.graphics.getWidth()/2f, 15*(Gdx.graphics.getHeight()/600f));
        font.draw(uiBatch, "Eat Count: " + eatCount ,Gdx.graphics.getWidth()/1.3333f, 15*(Gdx.graphics.getHeight()/600f) );

        uiBatch.end();
    }

    // runs the actual character interaction with the map and their stats
    public void game(){
        // ends game after 7 days
        if(day==8){
            game.setScreen(new ScoreScreen(game, score));
        }
        // goes to new day once the 16 hours are up
        if(time <=0){
            day ++;
            player.setEnergy(10);
            time = 16;
        }

        // checks if player wants to do an action(pressing e) and that there have been 5 seconds since their last action
        // last action checker simply so they don't perform a bunch of actions with a single click as this runs every frame
        if(player.isAction()&& System.currentTimeMillis() - lastAction > 3000){

            if(player.eatDesire()){
                Activity eatActivity = locations[EAT_LOCATION].getActivity(0);
                if(eatActivity.checkActivity(player.getEnergy(), time)){
                    eatCount++; // adds one to eat count
                    player.setEnergy(player.getEnergy()-eatActivity.getEnergyCost()); // takes away energy used from player
                    time -= eatActivity.getTimeCost(); // decrements time by time the activity takes
                    score += eatActivity.getScoreImpact(); // increments score

                    lastAction = System.currentTimeMillis(); // sets their time of last action to current time - next time they can do an action is 3 secs
                }
            }

            if(player.studyDesire()){
                Activity studyActivity = locations[STUDY_LOCATION].getActivity(0);
                if(studyActivity.checkActivity(player.getEnergy(), time)){
                    studyCount++;

                    player.setEnergy(player.getEnergy()-studyActivity.getEnergyCost());
                    time -= studyActivity.getTimeCost();
                    score += studyActivity.getScoreImpact();

                    lastAction = System.currentTimeMillis();
                }else energy = false;
            }

            if(player.playDesire()){
                Activity funActivity = locations[RECREATION_LOCATION].getActivity(0);
                if(funActivity.checkActivity(player.getEnergy(), time)){
                    recCount++;
                    player.setEnergy(player.getEnergy()-funActivity.getEnergyCost());
                    time -= funActivity.getTimeCost();
                    score += funActivity.getScoreImpact();

                    lastAction = System.currentTimeMillis();
                }else energy = false;
            }

            if(player.sleepDesire()){
                Activity sleepActivity = locations[SLEEP_LOCATION].getActivity(0);
                if(sleepActivity.checkActivity(100, 100)){ //large numbers to allow player to sleep whenever
                    sleepCount++;
                    player.setEnergy(player.getEnergy()-sleepActivity.getEnergyCost());
                    time -= sleepActivity.getTimeCost();
                    score += sleepActivity.getScoreImpact();

                    lastAction = System.currentTimeMillis();
                }
            }

            if(score>=100){
                score = 100;
            }


        }

    }

    @Override
    public void resize(int i, int i1) {
        // updates viewpoint scaling when screen resolution is changed
        viewport.update(i,i1);
        // font size is scaled with screen size (with a scale of 1 based on window 800x600)
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
   
}
