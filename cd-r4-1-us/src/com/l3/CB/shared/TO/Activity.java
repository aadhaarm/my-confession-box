package com.l3.CB.shared.TO;

public enum Activity {
    SAME_BOAT("Same boat", "has voted 'same boat'", 10, 1), 
    SYMPATHY("Sympathy", "has sympathised", 5, 1), 
    LAME("Lame", "has voted 'lame'", 5, 0), 
    SHOULD_BE_PARDONED("Pardon", "has voted 'Should be pardoned'", 10, 1), 
    SHOULD_NOT_BE_PARDONED("No pardon", "has voted 'Should not be pardoned'", 2, 0), 
    ABUSE("Flag", "has reported abuse", 0, 0);

    String activityName;
    String activityVerb;
    int activitySharePoints;
    int activityPoints;

    private Activity(String activityName, String activityVerb, int activitySharePoints, int activityPoints) {
	this.activityName = activityName;
	this.activityVerb = activityVerb;
	this.activitySharePoints = activitySharePoints;
	this.activityPoints = activityPoints;
    }

    public String getActivityName() {
	return activityName;
    }

    public String getActivityAsVerb() {
	return activityVerb;
    }

    public int getActivitySharePoints() {
	return activitySharePoints;
    }

    public int getActivityPoints() {
	return activityPoints;
    }
}