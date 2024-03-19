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
    final float speed= 5f;
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

        if (getY()>=13 && velocity.y>0){velocity.y=0;}
        if (getX()<=0.4 && velocity.x<0){velocity.x=0;}
        if (getY()<=0.3 && velocity.y<0){velocity.y=0;}
        if (getX()>=19.5 && velocity.x>0){velocity.x=0;} // prevents character from walking off screen


        setX(getX() + velocity.x * delta);
        setY(getY() + velocity.y * delta);
        animationTime += delta;

        setRegion(velocity.y < 0 ? downWalking.getKeyFrame(animationTime,true) : velocity.y > 0 ? upWalking.getKeyFrame(animationTime,true):
                 velocity.x < 0 ? leftWalking.getKeyFrame(animationTime,true) : velocity.x > 0 ? rightWalking.getKeyFrame(animationTime,true):
                         still.getKeyFrame(animationTime,true));

    }




    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Keys.W:
                velocity.y = speed;
                animationTime = 0;
                break;
            case Keys.A:
                velocity.x = -speed;
                animationTime = 0;
                break;
            case Keys.S:
                velocity.y = -speed;
                animationTime = 0;
                break;
            case Keys.D:
                velocity.x = speed;
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






        TextureRegion[] walkFrames = new TextureRegion[4 * 5];

        for (int j=0; j < 4; j++){
            if(character%2==0){
                walkFrames[j] =  new TextureRegion(help,j*16+16,0,-16 ,16);
                walkFrames[j+4] =  new TextureRegion(help,j*16,0, 16,16);
            }else{
                walkFrames[j] =  new TextureRegion(help,j*16,0,16,16);
                walkFrames[j+4] =  new TextureRegion(help,j*16+16,0, -16 ,16);
            }

        }


        int index = 8;
        for (int i=1; i < 4; i++){
            for (int j=0; j < 4; j++){

                walkFrames[index++] =  new TextureRegion(help,j*16,i*16,16,16);
            }
        }


        help.flip(true,false);



        rightWalking= new Animation<TextureRegion> (0.25f, Arrays.copyOfRange(walkFrames, 0, 3));
        leftWalking= new Animation<TextureRegion> (0.25f, Arrays.copyOfRange(walkFrames, 4, 7));
        upWalking= new Animation<TextureRegion> (0.25f, Arrays.copyOfRange(walkFrames, 8, 11));
        downWalking= new Animation<TextureRegion> (0.25f, Arrays.copyOfRange(walkFrames, 12, 15));
        still = new Animation<TextureRegion> (0.25f, Arrays.copyOfRange(walkFrames, 16, 19));

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

