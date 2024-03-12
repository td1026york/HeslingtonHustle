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
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class HeslingtonHustle implements Screen, InputProcessor {
	//Modified to now implement Screen instead of extending game
	//Seemingly functions as before, however this is not my section
	final LauncherClass game;
	Texture img;
	TiledMap tiledMap;
	OrthographicCamera camera;
	HexagonalTiledMapRenderer tiledMapRenderer;

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

		Gdx.input.setInputProcessor(this);
		// creates input processor
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
			if (keycode == Input.Keys.A)
				camera.translate(-32, 0);
			if (keycode == Input.Keys.D)
				camera.translate(32, 0);
			if (keycode == Input.Keys.W)
				camera.translate(0, 32);
			if (keycode == Input.Keys.S)
				camera.translate(0, -32);

			// debugging layers
			if (keycode == Input.Keys.NUM_1)
				tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
			if (keycode == Input.Keys.NUM_2)
				tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
		return false;
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
		img.dispose();
	}
}
