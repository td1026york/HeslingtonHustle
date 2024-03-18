package com.theemd.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class HeslingtonHustle implements Screen, InputProcessor {
//if possible, rename this class to Map
	// is this the same class as the PlayScreen?
	final LauncherClass game;
	Texture img;
	TiledMap tiledMap;
	OrthographicCamera camera;
	HexagonalTiledMapRenderer tiledMapRenderer;
	TiledMapImageLayer background;
	int sleepCount = 0;
	int eatCount = 0;
	int studyCount = 0;
	int recCount = 0;
	//variable to track the current score of the user
	int score = 30;

	boolean upHeld;
	boolean leftHeld;
	boolean downHeld;
	boolean rightHeld;
	//variable to identify which sprite set to use for the Character
	int charSelected;

	// Declares variables for later use

	public HeslingtonHustle (final LauncherClass gam) {
		this.game = gam;
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		// get size

		camera = new OrthographicCamera();
		camera.setToOrtho(false,w,h);
		camera.update();
		// sets up camera

		tiledMap = new TmxMapLoader().load("mainMap.tmx");

		tiledMapRenderer = new HexagonalTiledMapRenderer(tiledMap);

		// load map and renderer
		//tiledMapRenderer.renderImageLayer(background);

		Gdx.input.setInputProcessor(this);
		// creates input processor

		System.out.println(tiledMapRenderer.getViewBounds());
	}

	public void setCharacter(int charSelected){
		this.charSelected = charSelected;
		System.out.println("Character " + (this.charSelected + 1) + " selected");
	}

	public void changeInputStatus(int keycode, boolean truthVal){
		switch (keycode){
			case Input.Keys.W:
				upHeld = truthVal;
				break;
			case Input.Keys.A:
				leftHeld = truthVal;
				break;
			case Input.Keys.S:
				downHeld = truthVal;
				break;
			case Input.Keys.D:
				rightHeld = truthVal;
				break;

		}
	}

	@Override
	public boolean keyUp(int keycode) {
		changeInputStatus(keycode, false);
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		changeInputStatus(keycode, true);
		keyHeld(keycode);
		return false;
	}

	public void keyHeld(int keycode){
		//System.out.println(upHeld);System.out.println(leftHeld);
		//System.out.println(downHeld);System.out.println(rightHeld);

		if (leftHeld) {
			if (camera.position.x - 32 >= 320)
				camera.translate(-32, 0);
		}
		if (rightHeld) {
			if (camera.position.x + 32 <= 2144)
				camera.translate(32, 0);
		}
		if (upHeld) {
			if (camera.position.y + 32 <= 1360)
				camera.translate(0, 32);
		}
		if (downHeld) {
			if (camera.position.y - 32 >= 240)
				camera.translate(0, -32);
			}

			//System.out.println(camera.position);

		if (keycode == Input.Keys.EQUALS) {
			System.out.println(camera.zoom); // Z00M IN
			if (camera.zoom >= 0.5)
				camera.zoom -= 0.05;
		}
		if (keycode == Input.Keys.MINUS) // Z00M OUT
			{System.out.println(camera.zoom);
				if (camera.zoom <= 3.8)
					camera.zoom += 0.05;
			}

			//if (keycode == Input.Keys.Q)
			//	camera.rotate(2, 0, 0, 1);
			//if (keycode == Input.Keys.E)
			//	camera.rotate(-2, 0, 0, 1); // rotation mechanic

			System.out.println(tiledMap.getLayers());
			// debugging layers
			if (keycode == Input.Keys.NUM_1)
				tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
			if (keycode == Input.Keys.NUM_2)
				tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
	}

	@Override
	public boolean keyTyped(char character) {

		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}

	public boolean touchCancelled(int a, int b, int c, int d){
		return false;
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float v) {
		// Moved code from previous render() class into render(float) class provided by screen, otherwise code was left
		// unmodified to prevent issues when running the game
		Gdx.gl.glClearColor(86/255f, 200/255f, 118/255f, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// clear screen

		camera.update();
		tiledMapRenderer.setView(camera);
		// set view of camera

		tiledMapRenderer.render();
		tiledMapRenderer.getBatch().begin();
		tiledMapRenderer.getBatch().end();
		// render tilemap

		/* Temporary debug method to manually move to the final score screen
		 */
		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			game.setScreen(new ScoreScreen(game, score));
			this.dispose();
		}
	}

	//default methods were added when modifying the HeslingtonHustle class
	@Override
	public void resize(int i, int i1) {

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
	public void dispose () {
		tiledMap.dispose();
		//commented out for now since img is never instantiated in this version of the code
		//img.dispose();
	}
}
