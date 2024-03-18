package com.theemd.game;

public class Activity {
    private int energyCost;
    private int scoreImpact;
    private int timeCost;
    private String name;

    /** Creates a new instance of the Activity class with the passed parameters and returns it
     * @param e the energy cost of the Activity
     * @param s the impact on the score of the Activity (positive or negative)
     * @param t the time cost of the Activity in hours
     * @param n the name of the Activity
     * @return new Activity instance created
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

    /** Returns the energy cost of the Activity
     * @return int energyCost
     */
    public int getEnergyCost(){
        return energyCost;
    }
    /** Returns the score impact of the Activity
     * @return int scoreImpact
     */
    public int getScoreImpact(){
        return scoreImpact;
    }
    /** Returns the time cost of the Activity in hours
     * @return int timeCost
     */
    public int getTimeCost(){
        return timeCost;
    }

    /** Returns the name of the Activity
     * @return String name
     */
    public String getName(){
        return name;
    }

}
