package com.theemd.game;


import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;

public class Player extends Sprite  implements InputProcessor {
    int character; // the skin the player chooses
    final int yOffset, xOffsett; // For choosing the quadrant of character a player has chosen walking animations
    final int cDimensions = 16; // each seperate image of a character is 16x16 pixels

    private Vector2 velocity = new Vector2(); // for player movement
    private float animationTime;

    Animation<TextureRegion>  upWalking;
    Animation<TextureRegion> downWalking;
    Animation<TextureRegion>  rightWalking;
    Animation<TextureRegion>  leftWalking;

    Animation<TextureRegion> still;


    public Player(int character) {
        this.character = character;
        velocity.x = 0;
        velocity.y = 0;


        switch (character) {
            case 0:
                yOffset = 64;
                xOffsett = 0;

                break;
            case 1:
                yOffset = 64;
                xOffsett = 64;

                break;

            case 2:
                yOffset = 0;
                xOffsett = 0;

                break;

            case 3:
                yOffset = 0;
                xOffsett = 64;

                break;
            default:
                yOffset = 0;
                xOffsett = 0;

        }

    }


    @Override
    public void draw(Batch batch,float delta) {

        update(delta);

        super.draw(batch);
    }
    public void update(float delta){
        setX(getX() + velocity.x * delta);
        setY(getY() + velocity.y * delta);
        animationTime += delta;

        setRegion(velocity.y < 0 ? downWalking.getKeyFrame(animationTime,true) : velocity.y > 0 ? upWalking.getKeyFrame(animationTime,true):
                 velocity.x < 0 ? rightWalking.getKeyFrame(animationTime,true) : velocity.x > 0 ? rightWalking.getKeyFrame(animationTime,true):
                         still.getKeyFrame(animationTime,true));

    }




    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Keys.W:
                velocity.y = 0.5f;
                animationTime = 0;
                break;
            case Keys.A:
                velocity.x = -0.5f;
                animationTime = 0;
                break;
            case Keys.S:
                velocity.y = -0.5f;
                animationTime = 0;
                break;
            case Keys.D:
                velocity.x = 0.5f;
                animationTime = 0;
        }
        return true;



    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode) {
            case Keys.A:
            case Keys.D:
                velocity.x = 0;
                animationTime = 0;
                break;
            case Keys.W:
            case Keys.S:
                velocity.y = 0;
                animationTime = 0;

        }
        return true;
    }

    public void animate(Texture temp){

        TextureRegion help = new TextureRegion(temp,xOffsett,yOffset,64,64);






        TextureRegion[] walkFrames = new TextureRegion[4 * 4];
        int index = 0;
        for (int i=0; i < 4; i++){
            for (int j=0; j < 4; j++){
                walkFrames[index++] =  new TextureRegion(help,j*16,i*16,16,16);;
            }
        }

        rightWalking= new Animation<TextureRegion> (0.25f, Arrays.copyOfRange(walkFrames, 0, 3));
        upWalking= new Animation<TextureRegion> (0.25f, Arrays.copyOfRange(walkFrames, 4, 7));
        downWalking= new Animation<TextureRegion> (0.25f, Arrays.copyOfRange(walkFrames, 8, 11));
        still = new Animation<TextureRegion> (0.25f, Arrays.copyOfRange(walkFrames, 12, 14));
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}

