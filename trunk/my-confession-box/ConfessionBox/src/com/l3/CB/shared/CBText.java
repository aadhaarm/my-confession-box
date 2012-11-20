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

    @DefaultStringValue("Appeal for pardon to someone")
    String registerPageOptionConfessToFriend();

    @DefaultStringValue("Choose a friend")
    String registerPageChooseFriend();

    @DefaultStringValue("Submit your confession")
    String buttonTextSubmitConfession();

    @DefaultStringValue("Please submit your confession here. (Your confession is never shared to any one unless you want it)")
    String registerPageTitle();

    @DefaultStringValue("I pardon you on one condition, Only when you Un-Hide your identity to the world.")
    String pardonPopupOpenIdentityCondition();

    @DefaultStringValue("Get ")
    String pardonPopupPardonActivityConditionPartOne();	

    @DefaultStringValue(" 'Should be pardoned' votes.")
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

    @DefaultStringValue("Reveal your Identity")
    String pardonPopupOpenIdentityConditionView();

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

    @DefaultStringValue("Subscribe")
    String subscribeAnchorLabel();

    @DefaultStringValue("Un-Subscribe")
    String unSubscribeAnchorLabel();

    @DefaultStringValue("Subscribe to comments and pardon status updates of this confession")
    String subscribeLinkToolTipText();

    @DefaultStringValue("You can always click the voting button again to STOP your vote to be registered, till the timer reaches 0.")
    String undoVoteToolTip();

    @DefaultStringValue("You can share your vote on your FB wall by clicking 'Share' on the vote button you just clicked.")
    String shareVoteToolTip();

    @DefaultStringValue("Following are the conditions that the pardoner has put and the confesee shall only be pardoned when these conditions are met.")
    String pardonConditionHelpInfoToolTip();

    @DefaultStringValue("The confessee is pardoned only when the identity is revealed on public wall.")
    String pardonConditionUnHideHelpInfoToolTip();

    @DefaultStringValue("The confessee is pardoned when the required number number of 'Should be Pardoned' votes are given by the all.")
    String pardonConditionSPVoteHelpInfoToolTip();
    
    @DefaultStringValue("share")
    String activityButtonShareButtonLabel();

    @DefaultStringValue(" has been given ")
    String activityButtonShareClickText1();

    @DefaultStringValue(" 'Human Points'.")
    String activityButtonShareClickText2();

    @DefaultStringValue(" characters remaining")
    String confessionTextBoxRemainingCharactersMessage();

    @DefaultStringValue("Please enter valid text.")
    String confessionTextBoxErrorMessage();

    @DefaultStringValue("Please enter a valid title.")
    String confessionTitleErrorMessage();

    @DefaultStringValue("Un-Hide your identity by clicking this.")
    String unHideIdentityButtonTitleUserControl();

    @DefaultStringValue("Hide your identity by clicking this.")
    String hideIdentityButtonTitleUserControl();

    @DefaultStringValue("Hide your confession from anonymous wall.")
    String hideConfessionButtonTitleUserControl();

    @DefaultStringValue("Un-Hide your confession from anonymous wall.")
    String unhideConfessionButtonTitleUserControl();

    @DefaultStringValue("Type your friend's name and choose from the options")
    String shareConfessionSuggestBoxErrorMessage();

    @DefaultStringValue("Appeal")
    String shareConfessionButtonShareConfessionPopup();

    @DefaultStringValue(" has confessed to you on Confession Box.")
    String shareConfessionFBWallMessage();

    @DefaultStringValue("Ask for pardon from some one in your friends.")
    String shareConfessionUserControlButtonTitle();

    @DefaultStringValue("Add update text to your confession.")
    String editConfessionUserControlButtonTitle();

    @DefaultStringValue("more..")
    String moreLink();

    @DefaultStringValue("less..")
    String lessLink();

    @DefaultStringValue("All confessions")
    String filterAllConfessions();

    @DefaultStringValue("Subscribed")
    String filterSubscribedConfessions();

    @DefaultStringValue("Hidden identity")
    String filterHiddenIdlConfessions();

    @DefaultStringValue("Your language")
    String filterLocaleConfessions();

    @DefaultStringValue("Most 'SAME BOAT'")
    String filterSBVoteConfessions();

    @DefaultStringValue("Most 'LAME'")
    String filterLameVotedConfessions();

    @DefaultStringValue("Most SYMPATHAISED")
    String filterSymVoteConfessions();

    @DefaultStringValue("Most 'SHOULD BE PARDONED'")
    String filterSPVoteConfessions();

    @DefaultStringValue("Open identity")
    String filterOpenIdConfessions();

    @DefaultStringValue("Your activity")
    String filterUserActivityConfessions();

    @DefaultStringValue("less than a min ago")    
    String timestampLessThanMinut();

    @DefaultStringValue(" minuts ago")
    String timestampMinutsAgo();

    @DefaultStringValue(" hours ago")
    String timestampHoursAgo();

    @DefaultStringValue("No confessions for you in this view.")
    String noConfessionsInViewMessage();

    @DefaultStringValue("PARDONED")
    String pardonStatusLabel();

    @DefaultStringValue("Awaiting PARDON")
    String awaitingPardonStatusLabel();

    @DefaultStringValue("Your confession is hidden and never shared with anyone.")
    String registerConfessionTitleHelpInfoToolTip();

    @DefaultStringValue("These are the options you can opt for while confessing.")
    String registerConfessionOptionsHelpInfoToolTip();
}