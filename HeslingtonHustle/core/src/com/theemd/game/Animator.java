package com.theemd.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.w3c.dom.Text;

public class Animator implements ApplicationListener {

    SpriteBatch spriteBatch;
    Animation<TextureRegion> walkAnimation;
    Texture walkSheet;
    Texture charSheet;
    float stateTime;

    @Override
    public void create() {

        charSheet = new Texture((Gdx.files.internal("characters.png")));

        TextureRegion[][] tmp = TextureRegion.split(charSheet,
                                charSheet.getWidth() / 2,
                                charSheet.getHeight() / 2);
        // get character type
        Texture tmp1 = tmp[0][0].getTexture();
        TextureRegion[][] tmp2 = TextureRegion.split(tmp1,
                                tmp1.getWidth() / 4,
                                tmp1.getHeight() / 4);
        TextureRegion[] walkFrames = new TextureRegion[4 * 4];
        int index = 0;
        for (int i=0; i < 4; i++){
            for (int j=0; j < 4; j++){
                walkFrames[index++] = tmp2[i][j];
            }
        }

        walkAnimation = new Animation<TextureRegion>(0.025f, walkFrames);

        spriteBatch = new SpriteBatch();
        stateTime = 0f;

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void render() {
        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, 50, 50);
        spriteBatch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        walkSheet.dispose();

    }
}
