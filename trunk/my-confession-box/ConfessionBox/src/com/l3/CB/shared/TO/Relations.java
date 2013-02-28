package com.l3.CB.shared.TO;

public enum Relations {
    FRIEND("Friend"), MOTHER("Mother"), FATHER("Father"), BROTHER("Brother"), SISTER("Sister"), 
    GRAND_FATHER("Grand father"), GRAND_MOTHER("Grand mother"), TEACHER("Teacher"), COLLEAGUE ("Colleague"),
    ACQUAINTANCE ("Acquaintance"), STRANGER ("Stranger"), BOY_FRIEND ("Boy Friend"), GIRL_FRIEND ("Girl Friend"),
    ENEMY ("Enemy"), PET ("Pet"), UNCLE ("Uncle"), ANT ("Ant"), COUSIN ("Cousin"), DEBTER ("Debter"), GURU ("Guru"),
    EX_BOY_FRIEND ("Ex-boyfriend"), EX_GIRL_FRIEND ("Ex-girlfriend"), CELEBRITY ("Celebrity"), WIFE ("Wife"),
    HUSBAND ("Husband"), DAUGHTER ("Daughter"), SON ("Son"), EX_WIFE ("Ex-wife"), EX_HUSBAND ("Ex-husband"),
    BOSS ("Boss"), JONIOR ("Junior"), SENIOR("Senior"), NEPHEW("Nephew"), NIECE("Niece"), GRAND_SON("Grand son"),
    GRAND_DAUGHTER("Grand daughter"), GOD("God"), RELATIVE("Relative"), BEST_FRIEND("Best friend"),
    SON_IN_LAW("Son in-law"), DAUGHTER_IN_LAW("Daughter in-law"), FIANCEE("Fiancee");

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