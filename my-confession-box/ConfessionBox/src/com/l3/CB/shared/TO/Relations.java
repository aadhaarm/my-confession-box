package com.l3.CB.shared.TO;

public enum Relations {
    FRIEND("Friend"), MOTHER("Mother"), FATHER("Father"), BROTHER("Brother"), SISTER("Sister"), 
    GRAND_FATHER("Grand-father"), GRAND_MOTHER("Grand-mother"), TEACHER("Teacher"), COLLEAGUE ("Colleague"),
    ACQUAINTANCE ("acquaintance"), STRANGER ("Stranger"), BOY_FRIEND ("Boy-Friend"), GIRL_FRIEND ("Girl-Friend"),
    ENEMY ("Enemy"), PET ("Pet"), UNCLE ("Uncle"), ANT ("Ant"), COUSIN ("Cousin"), DEBTER ("Debter"), GURU ("Guru"),
    EX_BOY_FRIEND ("Ex-BoyFriend"), EX_GIRL_FRIEND ("Ex-GirlFriend"), CELEBRITY ("Celebrity"), WIFE ("Wife"),
    HUSBAND ("Husband"), DAUGHTER ("Daughter"), SON ("Son"), EX_WIFE ("Ex-Wife"), EX_HUSBAND ("Ex-Husband"),
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
