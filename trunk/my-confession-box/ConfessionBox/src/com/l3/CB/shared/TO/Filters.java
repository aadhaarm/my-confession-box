package com.l3.CB.shared.TO;

public enum Filters {
    RANDOM("Confession Posted World-wide", "You have no confessions in this view!"),
    ALL("Confession Posted World-wide", "You have no confessions in this view!"), 
    LOCALE_SPECIFIC("All Confessions in your Language Locale", "Oops, no confessions posted in your language. Be the first one to post a confession in your language."), 
    MOST_SAME_BOATS("Confessions with maximum 'Same Boat' votes globally", ""), 
    OPEN("Confessions with open identity", ""), 
    CLOSED("Global Anonymous Confession Feed", ""), 
    MOST_LAME("Confessions with maximum 'Lame' votes globally", ""), 
    MOST_SYMPATHY("Confessions with maximum 'Sympathy' votes globally", ""), 
    MOST_SHOULD_BE_PARDONED("Confessions with maximum 'Should be pardoned' votes globally", ""), 
    MOST_SHOULD_NOT_BE_PARDONED("Confessions with maximum 'Should not be pardoned' votes globally", ""), 
    USER_ACTIVITY("All Confessions where you ever voted", "Hey, it seems you are new here. Go to the confession wall and vote for a few confessions to earn human points. Your votes remain anonymous to everyone."), 
    SUBSCRIBED("All Confessions you subscribed", "You have not subscribed to any confession. By Subscribing you get updates of comments & pardon status of a confession. Subscribe now.");

    String filterInfoText;
    String emptyPageText;
    
    private Filters(String filterInfoText, String emptyPageText) {
	this.filterInfoText = filterInfoText;
	this.emptyPageText = emptyPageText;
    }

    public String getFilterInfoText() {
        return filterInfoText;
    }

    public String getEmptyPageText() {
        return emptyPageText;
    }
}