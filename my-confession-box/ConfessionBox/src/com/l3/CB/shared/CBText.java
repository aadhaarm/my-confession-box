package com.l3.CB.shared;

import com.google.gwt.i18n.client.Constants;

public interface CBText extends Constants {

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

	@DefaultStringValue("Un-Hide Identity to the world: Condition fulfilled")
	String pardonPopupOpenIdentityConditionFulfilled();

	@DefaultStringValue("Un-Hide Identity to the world: Yet to be fulfilled")
	String pardonPopupOpenIdentityConditionYetToBoFulfilled();

	@DefaultStringValue("Pardon when people give you 'Should be pardoned' vote: Condition fulfilled")
	String pardonPopupPardonActivityConditionFulfilled();

	@DefaultStringValue("Pardon when people give you 'Should be pardoned' vote: Yet to be fulfilled")
	String pardonPopupPardonActivityConditionYetToBoFulfilled();
}
