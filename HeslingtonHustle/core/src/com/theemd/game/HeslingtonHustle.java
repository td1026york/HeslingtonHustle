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




public class HeslingtonHustle extends Game implements InputProcessor {
	Texture img;
	TiledMap tiledMap;
	OrthographicCamera camera;
	HexagonalTiledMapRenderer tiledMapRenderer;

	// Declares variables for later use

	@Override
	public void create () {
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
	public void render () {
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
	public void dispose () {
		tiledMap.dispose();
		img.dispose();
	}
}
