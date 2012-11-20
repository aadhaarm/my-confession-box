package com.l3.CB.shared.TO;

public enum Relations {
    FRIEND("Friend"), MOTHER("Mother"), FATHER("father"), BROTHER("Brother"), SISTER("Sister"), 
    GRAND_FATHER("Grand-father"), GRAND_MOTHER("Grand-mother"), TEACHER("Teacher"), COLLEAGUE ("Colleague"),
    ACQUAINTANCE ("acquaintance"), STRANGER ("Stranger"), BOY_FRIEND ("Boy-Friend"), GIRL_FRIEND ("Girl-Friend"),
    ENEMY ("Enemy"), PET ("Pet"), UNCLE ("Uncle"), ANT ("Ant"), COUSIN ("Cousin"), DEBTER ("Debter"), GURU ("Guru"),
    X_BOY_FRIEND ("X-BoyFriend"), X_GIRL_FRIEND ("X-GirlFriend"), CELEBRITY ("Celebrity"), WIFE ("Wife"),
    HUSBAND ("Husband"), DAUGHTER ("Daughter"), SON ("Son"), X_WIFE ("X-Wife"), X_HUSBAND ("X-Husband"),
    BOSS ("Boss"), JONIOR ("Junior"), SENIOR("Senior");

    private String displayText;

    private Relations(String displayText) {
	this.displayText = displayText;
    }

    public String getDisplayText() {
	return displayText;
    }

    public void setDisplayText(String displayText) {
	this.displayText = displayText;
    }
}
