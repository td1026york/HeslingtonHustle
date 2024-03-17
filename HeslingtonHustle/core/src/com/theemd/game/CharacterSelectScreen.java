package com.theemd.game;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sun.tools.jconsole.JConsole;

import java.awt.*;

public class CharacterSelectScreen implements Screen {
    LauncherClass game;
    private static final int quadrantCount = 4; // for the four character options

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Rectangle[] quadrants; // stores the height,width and position for characters
    private Color[] quadrantColors; // to change color of a quadrant indicating selection
    // point that is touched
    private Vector3 touchPoint;
    private TextureRegion whiteTextureRegion; // filler texture to draw quadrants
    BitmapFont font;

    private Rectangle confirmation;
    private int selection;

    Texture characters[];
    Texture char1;
    Texture char2;
    Texture char3;
    Texture char4;
    public CharacterSelectScreen(LauncherClass game){

        this.game = game;
    }

    @Override
    public void show() {
        touchPoint = new Vector3();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        characters = new Texture[4];

        confirmation = new Rectangle(200,0,400,50);
        selection = -1;

        // to draw the four quadrants and initially make them white
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1); // Set color to white
        pixmap.fill(); // Fill the pixmap with white color
        Texture whiteTexture = new Texture(pixmap); // Create texture from pixmap
        whiteTextureRegion = new TextureRegion(whiteTexture);

        // Dispose the pixmap after creating the texture
        pixmap.dispose();

        for(int i = 0;i<4;i++){
            characters[i] = new Texture(Gdx.files.internal("characterSelect"+Integer.toString(i) + ".png"));


        }

        char1 = new Texture(Gdx.files.internal("characterSelect0.png"));
        char2 = new Texture(Gdx.files.internal("characterSelect1.png"));
        char3 = new Texture(Gdx.files.internal("characterSelect2.png"));
        char4 = new Texture(Gdx.files.internal("characterSelect3.png"));




        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera); // 4:3 aspect ratio for character select
        camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 0); // centers camera
        // camera.position.set(1, 1, 0);
        camera.update();

        // creates quadrants and sets them all to white
        quadrants = new Rectangle[quadrantCount];
        quadrantColors = new Color[quadrantCount];
        for (int i = 0; i < quadrantCount; i++) {
            quadrants[i] = new Rectangle();
            quadrantColors[i] = Color.WHITE;
        }



        // Calculate quadrant size and positions

        //size - Leaves 50 units top and bottom for confirmation button and prompt for user
        float quadrantWidth = (viewport.getWorldWidth() )/ 2f;
        float quadrantHeight = (viewport.getWorldHeight()  -100)/ 2f;

        // positions
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                quadrants[i * 2 + j].set((quadrantWidth * j), (quadrantHeight * i)+50, quadrantWidth, quadrantHeight);
            }
        }

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // Title/Instructions for Gamer
        batch.setColor(Color.GRAY);
        batch.draw(whiteTextureRegion,0,550,800,50);
        batch.setColor(Color.BLACK);
        font.draw(batch,"SELECT A CHARACTER",300,viewport.getWorldHeight()-20);






        // draws four quadrants with color according to quadrantColor
        //also draws character options
        for (int i = 0; i < quadrantCount; i++) {
            batch.setColor(quadrantColors[i]);
            batch.draw(whiteTextureRegion, quadrants[i].x, quadrants[i].y, quadrants[i].width, quadrants[i].height);
            batch.draw(characters[i],(quadrants[i].x+quadrants[i].width/2)-50,(quadrants[i].y+quadrants[i].height/2)-50,100,100);

        }
        // draw the background for the confirmation button
        if(selection!=-1) {
            batch.setColor(Color.GRAY);
            batch.draw(whiteTextureRegion, confirmation.x, confirmation.y, confirmation.width, confirmation.height);
            font.draw(batch,"Confirm Selection",340, viewport.getWorldHeight() - 575);
        }

        batch.end();

        // Handle user selection and confirmation
        if (Gdx.input.justTouched()) {
            viewport.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0)); // converts screen pixel coordinates into viewpoint coordinates (same system the quadrants are using)
            // when clicked cycles through to find which quadrant was clicked
            // Turns all quadrants white to clear any previous selection, then turns selected quadrant yellow to indicate selection
            if(selection!=-1) {
                if (confirmation.contains(touchPoint.x, touchPoint.y)) {
                    game.setScreen(new PlayScreen(game,selection));
                    this.dispose();
                }
            }


            for (int i = 0; i < quadrantCount; i++) {
                if (quadrants[i].contains(touchPoint.x, touchPoint.y)) {
                    for (int j = 0; j < quadrantCount; j++) {
                        quadrantColors[j] = Color.WHITE;


                    }
                    quadrantColors[i] = Color.YELLOW;
                    selection = i;
                    break;
                }
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
        batch.dispose();

    }
}
