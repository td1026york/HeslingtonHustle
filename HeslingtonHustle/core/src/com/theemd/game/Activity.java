package com.theemd.game;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class Activity {
    private int energyCost;
    private int scoreImpact;
    private int timeCost;
    private String name;
    private String popupString;

    /** Creates a new instance of the Activity class with the passed parameters and returns it
     * @param e the energy cost of the Activity
     * @param s the impact on the score of the Activity (positive or negative)
     * @param t the time cost of the Activity in hours
     * @param n the name of the Activity
     * @return the new Activity instance created
     */
    public Activity(int e, int s, int t, String n){
        energyCost = e;
        scoreImpact = s;
        timeCost = t;
        name = n;
    }

    /** Checks if there is enough time and energy remaining to perform this activity
     * @param charEnergy character's current energy value
     * @param hoursLeft amount of hours remaining in the day
     * @return boolean True or False determining if the activity can be performed
     */
    public boolean checkActivity(int charEnergy, int hoursLeft){
        return (charEnergy >= energyCost && hoursLeft >= timeCost);
    }

    public int getEnergyCost(){
        /** Returns the energy cost of the Activity
         * @return int energyCost
         */
        return energyCost;
    }

    public int getScoreImpact(){
        /** Returns the score impact of the Activity
         * @return int scoreImpact
         */
        return scoreImpact;
    }

    public int getTimeCost(){
        /** Returns the time cost of the Activity in hours
         * @return int timeCost
         */
        return timeCost;
    }

    public String getName(){
        /** Returns the name of the Activity
         * @return String name
         */
        return name;
    }

    public String getPopupString() {
        return popupString;
    }

    public void setPopupString(String popupString) {
        this.popupString = popupString;
    }

    public Activity returnSelf(){
        return (this);
    }
    public void openPopup(){
        TextField text = new TextField(this.popupString, new Skin());
        Table popup = new Table();
        popup.add(text).width(100);
    }

}
