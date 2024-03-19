package com.theemd.game;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import java.util.Dictionary;
import java.util.Hashtable;

public class InteractableArea {

    Activity restAct = new Activity(1,1,1,"rest");
    Activity studyAct = new Activity(1,1,1, "study");
    Activity recAct = new Activity(1,1,1, "recreation");

    Dictionary<String, Activity> dict = new Hashtable<String, Activity>();
    //dict.put("rest", restAct.returnSelf());

    private String objectName;
    private Activity activity;



    public InteractableArea(){

    }

    public Activity getActivity() {
        return activity;
    }
    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    public String getObjectName() {
        return objectName;
    }
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public void collidedWithArea(String objectName){

        activity.openPopup();
    }

}
