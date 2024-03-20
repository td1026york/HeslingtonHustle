package com.theemd.game;


import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.Arrays;

/**
 * Player class containing the information relating to the player throughout the game
 */
public class Player extends Sprite  implements InputProcessor {
    int character; // the skin the player chooses
    final int yOffset, xOffsett; // For choosing the quadrant of character a player has chosen walking animations
    final int cDimensions = 16; // each seperate image of a character is 16x16 pixels
    final float speed= 5f;
    private Vector2 velocity = new Vector2(); // for player movement
    private float animationTime; // for timing walking animation

    private TiledMapTileLayer collision;
    private TiledMapTileLayer interaction;

    int energy; // for keeping track of player energy day to day.

    boolean action = false; // for whether or not the player is pressing e and actin

    boolean musicPlaying = true;


    // self-explanatory icl
    Animation<TextureRegion>  upWalking;
    Animation<TextureRegion> downWalking;
    Animation<TextureRegion>  rightWalking;
    Animation<TextureRegion>  leftWalking;
    Animation<TextureRegion> still;


    /**
     * Creates and returns an instance of the Player, selecting the character sprite to be displayed.
     * @param character an integer used to determine which character
     */
    public Player(int character) {

        this.character = character; // user character choice
        velocity.x = 0;
        velocity.y = 0;

        // In character sheet character animations are separated into a quadrant for each character - selects correct quadrant.
        // used an offset of 64 as each frame of an animation is 16 pixels wide and tall, 4 frames in an animation and 4 animations per character
        // creating a 64 by 64 pixel grid for all four of the characters
        switch (character) {
            case 0:
                yOffset = 64;
                xOffsett = 0;

                break;
            case 1:
                yOffset = 64;
                xOffsett = 64;

                break;

            //this case duplicates the default branch, is it redundant?
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

    /**
     * Sets Player's collision attribute to the parameter passed
     * @param collision the TiledMapTileLayer to be assigned to the Player as collision
     */
    public void setCollision(TiledMapTileLayer collision) {
        this.collision = collision;
    }

    /**
     * Sets Player's interaction attribute to the parameter passed
     * @param interaction the TiledMapTileLayer to be assigned to the Player as interaction
     */
    public void setInteraction(TiledMapTileLayer interaction) {
        this.interaction = interaction;
    }

    // draws sprite - overridden to take in time for update() which calculated character movement and animation
    @Override
    public void draw(Batch batch,float delta) {

        update(delta);

        super.draw(batch);
    }
    public void update(float delta){

        // gets the coordinates of the cell the character is about to move into
        // adds half character height and width to set coords to center of characers
        // math.floor may be redundant
        int newX = (int) Math.floor(getX() + this.getWidth()/2 + velocity.x * delta);
        int newY = (int) Math.floor((getY() + this.getHeight()/3+ velocity.y * delta));

        // gets the cell based on coords
        Cell cell = collision.getCell(newX,newY);
        // if the cell isn't empty, checks if the cell is blocked, if it is, player position is not updated

        if (cell !=null && !cell.getTile().getProperties().containsKey("blocked") ){
            setX(getX() + velocity.x * delta);
            setY(getY() + velocity.y * delta);
        }

        // animation time for tracking which frame in the animaiton a character should be in -
        // resets to zero every time they change direction or stop moving in order to start next animation from the beginning
        animationTime += delta;


        // sets the character model based on which frame their animation currentlly is based on animationTime
        // as well as which animation they are in based on their velocity
        setRegion(velocity.y < 0 ? downWalking.getKeyFrame(animationTime,true) : velocity.y > 0 ? upWalking.getKeyFrame(animationTime,true):
                 velocity.x < 0 ? leftWalking.getKeyFrame(animationTime,true) : velocity.x > 0 ? rightWalking.getKeyFrame(animationTime,true):
                         still.getKeyFrame(animationTime,true));

    }

    /**
     *
     * @return
     */
    public boolean eatDesire(){
        Cell cell = interaction.getCell((int) getX(), (int) getY());
        return (cell !=null && cell.getTile().getProperties().containsKey("eat")) ;


    }

    /**
     *
     * @return
     */
    public boolean studyDesire(){
        Cell cell = interaction.getCell((int) getX(), (int) getY());
        return  (cell !=null && cell.getTile().getProperties().containsKey("study")) ;


    }

    /**
     *
     * @return
     */
    public boolean playDesire(){
        Cell cell = interaction.getCell((int) getX(), (int) getY());
        return cell !=null && cell.getTile().getProperties().containsKey("play");
    }

    /**
     *
     * @return
     */
    public boolean sleepDesire(){
        Cell cell = interaction.getCell((int) getX(), (int) getY());
        return cell !=null && cell.getTile().getProperties().containsKey("sleep");
    }

    /**
     * Returns the player's energy
     */
    public int getEnergy() {
        return energy;
    }

    /**
     * Sets the Player's energy to the value passed
     * @param energy the new energy value for the Player
     */
    public void setEnergy(int energy) {
        this.energy = energy;
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
                break;
            case Keys.E:
                action = true;
                break;
            case Keys.M:
                if(musicPlaying){Resources.music.stop(); musicPlaying=false; }else{Resources.music.play(); musicPlaying=true;}
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
                break;
            case Keys.E:
                action = false;

        }
        return true;
    }

    public boolean isAction() {
        return action;
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

