package com.l3.CB.shared.TO;

public enum Activity {
	SAME_BOAT, SYMPATHY, LAME, SHOULD_BE_PARDONED, SHOULD_NOT_BE_PARDONED, ABUSE;

	public String getActivityAsVerb() {
		String returnVal = "";
		switch (this) {
		case ABUSE:
			returnVal = "reported abuse";
			break;
		case SAME_BOAT:
			returnVal = "voted 'same boat'";
			break;
		case LAME:
			returnVal = "voted 'lame'";
			break;
		case SHOULD_BE_PARDONED:
			returnVal = "voted 'Should be pardoned'";
			break;
		case SHOULD_NOT_BE_PARDONED:
			returnVal = "voted 'Should not be pardoned'";
			break;
		case SYMPATHY:
			returnVal = "sympathised";
			break;
		}
		return returnVal;
	}

	public int getActivitySharePoints() {
		int returnVal = 0;
		switch (this) {
		case ABUSE:
			returnVal = 10;
			break;
		case SAME_BOAT:
			returnVal = 10;
			break;
		case LAME:
			returnVal = 5;
			break;
		case SHOULD_BE_PARDONED:
			returnVal = 10;
			break;
		case SHOULD_NOT_BE_PARDONED:
			returnVal = 2;
			break;
		case SYMPATHY:
			returnVal = 5;
			break;
		}
		return returnVal;
	}
	
	public int getActivityPoints() {
		int returnVal = 0;
		switch (this) {
		case ABUSE:
			returnVal = 0;
			break;
		case SAME_BOAT:
			returnVal = 1;
			break;
		case LAME:
			returnVal = 1;
			break;
		case SHOULD_BE_PARDONED:
			returnVal = 1;
			break;
		case SHOULD_NOT_BE_PARDONED:
			returnVal = 1;
			break;
		case SYMPATHY:
			returnVal = 1;
			break;
		}
		return returnVal;
	}

}
