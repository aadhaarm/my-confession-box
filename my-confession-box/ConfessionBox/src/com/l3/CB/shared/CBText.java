package com.l3.CB.shared;

import com.google.gwt.i18n.client.Constants;

public interface CBText extends Constants {

	@DefaultStringValue("Error while communication with server. Please try reloading the application after some time.")
	String applicationError();
	
	@DefaultStringValue("Feed")
	String cbMenuConfessionFeed();
	
	@DefaultStringValue("Confess")
	String cbMenuConfessionConfess();
	
	@DefaultStringValue("My Confessions")
	String cbMenuConfessionMyConfessions();
	
	@DefaultStringValue("Confessed to me")
	String cbMenuConfessionConfessToMe ();

	@DefaultStringValue("Confession Box")
	String applicationTitle();
	
	@DefaultStringValue("Anonymous")
	String confessedByAnynName();
	
	@DefaultStringValue("Report Abuse!")
	String buttonTitleReportAbuse();
	
	@DefaultStringValue("You should not be pardoned")
	String buttonTitleShouldNotBePardoned();

	@DefaultStringValue("You should be pardoned")
	String buttonTitleShouldBePardoned();
	
	@DefaultStringValue("Such a Lame confession")
	String buttonTitleLameConfession();
	
	@DefaultStringValue("I have sympathy for you")
	String buttonTitleSympathy();
	
	@DefaultStringValue("I am in the same boat")
	String buttonTitleSameBoat();

	@DefaultStringValue("Hide my identity")
	String registerPageOptionHideID();
	
	@DefaultStringValue("Confess to a friend")
	String registerPageOptionConfessToFriend();

	@DefaultStringValue("Choose a friend")
	String registerPageChooseFriend();
	
	@DefaultStringValue("Submit your confession")
	String buttonTextSubmitConfession();
	
	@DefaultStringValue("Please submit your confession here. (Your confession is never shared to any one unless you want it)")
	String registerPageTitle();

	@DefaultStringValue("I pardon you on one condition, Only when you Un-Hide your identity to the world.")
	String pardonPopupOpenIdentityCondition();
	
	@DefaultStringValue("I pardon you on one condition, Only when ")
	String pardonPopupPardonActivityConditionPartOne();	
	
	@DefaultStringValue("people give you 'Should be pardoned' vote.")
	String pardonPopupPardonActivityConditionPartTwo();
	
	@DefaultStringValue("I hereby pardon you for the above mentioned confession, only when you fulfill the above conditions that I have selected")
	String pardonPopupAcceptance();
	
	@DefaultStringValue("Pardon")
	String pardonPopupPardonButtonText();
	
	@DefaultStringValue("Cancel")
	String pardonPopupCancelButtonText();
	
	@DefaultStringValue("Pardoned")
	String pardonedStatus();

	@DefaultStringValue("Yet to be pardoned")
	String yetToBePardonedStatus();

	@DefaultStringValue("Un-Hide Identity: Condition fulfilled")
	String pardonPopupOpenIdentityConditionFulfilled();

	@DefaultStringValue("Un-Hide Identity: Yet to be fulfilled")
	String pardonPopupOpenIdentityConditionYetToBoFulfilled();

	@DefaultStringValue("people should give you 'Should be pardoned' vote: Condition fulfilled")
	String pardonPopupPardonActivityConditionFulfilled();

	@DefaultStringValue("people should give you 'Should be pardoned' vote: Yet to be fulfilled")
	String pardonPopupPardonActivityConditionYetToBoFulfilled();

	@DefaultStringValue("Filters to choose the confessions you want to read.")
	String feedPageFilterToolTip();

	@DefaultStringValue("Vote if you are or were once in kind of same situation. This vote will not be disclosed to anyone.")
	String sameBoatButtonToolTip();

	@DefaultStringValue("Vote if you find the confession inappropriate for the confession wall.")
	String abuseButtonToolTip();

	@DefaultStringValue("Vote if the confession is just lame and you done find its a real confession.")
	String lameButtonToolTip();

	@DefaultStringValue("Vote if you have sympathies for the confesser.")
	String sympathyButtonToolTip();

	@DefaultStringValue("Vote if you feel the confesser should be pardoned for the act.")
	String shouldBePardonedButtonToolTip();

	@DefaultStringValue("Vote if you feel the confesser should not be pardoned for the act.")
	String shouldNotBePardonedButtonToolTip();

	@DefaultStringValue("Confession Title")
	String confessionTitleLabel();

	@DefaultStringValue("Tick this if you want to share the confession anonymaously without disclosing your identity to any one.")
	String registerConfHideIdCheckBoxToolTip();

	@DefaultStringValue("Tick this if you want to confess to one of your friends. You need to cheoose your friend in the box below. Your identity with be disclosed to the person you confess to.")
	String registerConfShareWithCheckBoxToolTip();

	@DefaultStringValue("'Like', 'Comments' & 'Send'...")
	String getMeJustOpenLinkText();
}
