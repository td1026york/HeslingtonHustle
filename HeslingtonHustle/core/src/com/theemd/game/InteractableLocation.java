package com.theemd.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class InteractableLocation {
    ArrayList<Activity> activityList;
    Vector2 myLocation;
    String myName;

    public InteractableLocation(){
        activityList = new ArrayList<Activity>();
    }

    public void addActivity(int e, int s, int t, String n){
        activityList.add(new Activity(e, s, t, n));
    }

    public Activity getActivity(int activityNo){
        return activityList.get(activityNo);
    }

    public ArrayList<Activity> getActivityList(){
        return activityList;
    }
}
