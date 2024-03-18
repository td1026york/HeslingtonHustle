package com.theemd.game;


import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite  implements InputProcessor {
    int character; // the skin the player chooses
    final int yOffset, xOffsett; // For choosing the quadrant of character a player has chosen walking animations
    final int cDimensions = 16; // each seperate image of a character is 16x16 pixels

    private Vector2 velocity = new Vector2(); // for player movement

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
    public void draw(Batch batch) {
        setRegion(xOffsett, yOffset + cDimensions * 2, cDimensions, cDimensions);


        super.draw(batch);
    }


    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
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

