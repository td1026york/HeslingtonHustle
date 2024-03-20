package com.theemd.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Screen used to display the start menu of the game after logo fades away
 */
public class MainMenuScreen implements Screen {
    final LauncherClass game;
    private OrthographicCamera camera;
    private static final int buttonNo = 2;
    private SpriteBatch batch;
    private Viewport viewport;
    private Rectangle[] buttons;
    private Vector3 touchPoint;
    private TextureRegion whiteTextureRegion; // filler texture to draw quadrants
    BitmapFont font;

    /**
     * Creates and returns an instance of the MainMenuScreen
     * @param game the final LauncherClass used throughout the game
     */
    public MainMenuScreen(final LauncherClass game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(194, 237, 180, 1);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.setColor(Color.GRAY);
        batch.draw(whiteTextureRegion,0,550,800,50);
        batch.setColor(Color.BLACK);
        font.draw(batch,"Welcome to Heslington Hustle!",30,viewport.getWorldHeight()-15);

        batch.setColor(new Color(Color.valueOf("4cd463")));

        //draws start button and help button
        for (int i = 0; i < buttonNo; i++) {
            batch.draw(whiteTextureRegion, buttons[i].x, buttons[i].y, buttons[i].width, buttons[i].height);
        }
        font.getData().setScale(3,3);
        font.draw(batch,"HELP", buttons[0].x+100, buttons[0].y+50);
        font.draw(batch,"START", buttons[1].x+100, buttons[1].y+50);

        batch.end();

        if (Gdx.input.justTouched()) {
            viewport.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            for (int i = 0; i < buttonNo; i++) {
                if (buttons[i].contains(touchPoint.x, touchPoint.y)) {
                    if(i==1){
//                        System.out.println("Top button");
                        game.setScreen(new CharacterSelectScreen(game));
                        this.dispose();
                    }
                    else
                    {
//                        System.out.println("Bottom button");
                        game.setScreen(new HelpScreen(game));
                    }
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
        batch.dispose();
        font.dispose();
    }

    @Override
    public void show() {
        touchPoint = new Vector3();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLACK);

        // to draw the four quadrants and initially make them white
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(76, 212, 99, 1));
        pixmap.fill();
        Texture whiteTexture = new Texture(pixmap); // Create texture from pixmap
        whiteTextureRegion = new TextureRegion(whiteTexture);

        // Dispose the pixmap after creating the texture
        pixmap.dispose();

        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera); // 4:3 aspect ratio for character select
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        camera.position.set(worldWidth / 2f, worldHeight / 2f, 0); // centers camera
        // camera.position.set(1, 1, 0);
        camera.update();

        // creates buttons
        buttons = new Rectangle[buttonNo];
        for (int i = 0; i < buttonNo; i++) {
            buttons[i] = new Rectangle();
        }

        // Calculate quadrant size and positions

        //size - Leaves 50 units top and bottom for confirmation button and prompt for user
//        float quadrantWidth = worldWidth / 2f;
        float quadrantHeight = (worldHeight - 100)/ 2f;

        // positions
        for (int i = 0; i < 2; i++) {
            buttons[i].set(20, (quadrantHeight * i)+(50+10*i), worldWidth - 40, quadrantHeight-(10*i));
        }
    }

}
