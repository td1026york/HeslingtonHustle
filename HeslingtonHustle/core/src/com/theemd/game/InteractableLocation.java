package com.theemd.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class InteractableLocation {
    ArrayList<Activity> activityList;
    Vector2 myLocation;
    String myName;

    /**
     * Creates a new instance of InteractableLocation and returns it
     * The activityList is set to empty
     */
    public InteractableLocation(){
        activityList = new ArrayList<Activity>();
    }

    /**
     * Creates a new instance of InteractableLocation and returns it
     * @param myActivities the activityList available at the location
     */
    public InteractableLocation(ArrayList<Activity> myActivities){
        activityList = myActivities;
    }

    /**
     * Create a new Activity and add it to the end of the activity list.
     * @param e the energy cost of the Activity
     * @param s the score impact of the Activity
     * @param t the time cost of the Activity
     * @param n the name of the Activity
     */
    public void addActivity(int e, int s, int t, String n){
        activityList.add(new Activity(e, s, t, n));
    }

    /**
     * Replace the list of activities stored at this location with the new one passed in the function
     * @param newList the new list of Activities to be available at this location
     */
    public void newActivityList(ArrayList<Activity> newList){
        activityList = newList;
    }


    /**
     * Returns the Activity specified by the paramater activityNo in the list
     * @param activityNo the position in the activityList you wish to retrieve
     * @return the activity at position ActivityNo in the list
     */
    public Activity getActivity(int activityNo){
        return activityList.get(activityNo);
    }

    /**
     * @return activityList, the list of all the Activities available at this location
     */
    public ArrayList<Activity> getActivityList(){
        return activityList;
    }

    /**
     * Returns the location of the building as a 2D vector
     * @return Vector2 myLocation
     */
    public Vector2 getLocation() {
        return myLocation;
    }

    /**
     * Sets the Vector2 location to the one passed
     * @param location the new location of the InteractableLocation
     */
    public void setLocation(Vector2 location) {
        myLocation = location;
    }

    /**
     * Returns the name of the InteractableLocation
     * @return String myName
     */
    public String getName() {
        return myName;
    }

    /**
     * Sets the name of the InteractableLocation
     * @param name the new name of the InteractableLocation
     */
    public void setName(String name){
        myName = name;
    }
}
