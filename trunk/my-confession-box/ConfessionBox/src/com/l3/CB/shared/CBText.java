package com.l3.CB.shared;

import com.google.gwt.i18n.client.Constants;

public interface CBText extends Constants {

    @DefaultStringValue("Oops! Error while communicating with server. Do you want to reload the application?")
    String applicationError();

    @DefaultStringValue("Confession Wall")
    String cbMenuConfessionFeed();

    @DefaultStringValue("Write Confession")
    String cbMenuConfessionConfess();

    @DefaultStringValue("My Confessions")
    String cbMenuConfessionMyConfessions();

    @DefaultStringValue("Confessed To Me")
    String cbMenuConfessionConfessToMe ();

    @DefaultStringValue("Confession Box")
    String applicationTitle();

    @DefaultStringValue("Anonymous")
    String confessedByAnynName();

    @DefaultStringValue("World")
    String confessedToWorld();
    
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

    @DefaultStringValue("Hide my identity")
    String registerPageOptionHideIDSlider();
    
    @DefaultStringValue("Request for pardon")
    String registerPageOptionConfessToFriend();

    @DefaultStringValue("Choose a friend")
    String registerPageChooseFriend();

    @DefaultStringValue("Submit confession")
    String buttonTextSubmitConfession();

    @DefaultStringValue("Please submit your confession here. (Your confession is never shared to any one unless you want it)")
    String registerPageTitle();

    @DefaultStringValue("Reveal your identity to the world.")
    String pardonPopupOpenIdentityCondition();

    @DefaultStringValue("Get ")
    String pardonPopupPardonActivityConditionPartOne();	

    @DefaultStringValue(" 'Should be pardoned' votes.")
    String pardonPopupPardonActivityConditionPartTwo();

    @DefaultStringValue("I pardon you for you confession, only when you fulfill the above conditions that I have selected")
    String pardonPopupAcceptance();

    @DefaultStringValue("Pardon")
    String pardonPopupPardonButtonText();

    @DefaultStringValue("Cancel")
    String pardonPopupCancelButtonText();

    @DefaultStringValue("Pardoned")
    String pardonedStatus();

    @DefaultStringValue("Reveal your Identity")
    String pardonPopupOpenIdentityConditionView();

    @DefaultStringValue("Choose the confessions you want to read.")
    String feedPageFilterToolTip();

    @DefaultStringValue("SAME BOAT: If you feel, that in past or present, you were in a similar situation / position or had similar sentiments expressed in the confession text, you should consider pressing 'Same Boat' button telling the confessee that you understand his/her situation/sentiments and perhaps have shared a similar situation in your life. Your vote will mean a lot to the confessee. You get to earn 1 'human' points for voting and 10 'human' points for sharing your vote thereafter.")
    String sameBoatButtonToolTip();

    @DefaultStringValue("REPOTE ABUSE: Vote report abuse if you find the confession inappropriate.")
    String abuseButtonToolTip();

    @DefaultStringValue("LAME: Vote if the confession is just lame and you don't find it a real confession.")
    String lameButtonToolTip();

    @DefaultStringValue("SYMPATHY: Vote if you have sympathies for the confesser. You get to earn 1 'human' points for voting and 5 'human' points for sharing your vote thereafter.")
    String sympathyButtonToolTip();

    @DefaultStringValue("SHOULD BE PARDONED: Vote if you feel the confesser should be pardoned for the act. You get to earn 1 'human' points for voting and 10 'human' points for sharing your vote thereafter.")
    String shouldBePardonedButtonToolTip();

    @DefaultStringValue("SHOULD NOT BE PARDONED: Vote if you feel the confesser should not be pardoned for the act. You get to earn 2 'human' points for sharing your vote.")
    String shouldNotBePardonedButtonToolTip();

    @DefaultStringValue("Confession Title")
    String confessionTitleLabel();

    @DefaultStringValue("Tick this if you want to share the confession anonymaously without disclosing your identity to any one.")
    String registerConfHideIdCheckBoxToolTip();

    @DefaultStringValue("Tick this if you want to confess to one of your friends. You need to cheoose your friend in the box below. Your identity with be disclosed to the person you confess to.")
    String registerConfShareWithCheckBoxToolTip();

    @DefaultStringValue("Subscribe")
    String subscribeAnchorLabel();

    @DefaultStringValue("Unsubscribe")
    String unSubscribeAnchorLabel();

    @DefaultStringValue("Subscribe to this confession and be updated about pardon status updates")
    String subscribeLinkToolTipText();

    @DefaultStringValue("You can click the voting button again to STOP your vote to be registered, till the timer reaches '0'.")
    String undoVoteToolTip();

    @DefaultStringValue("You can share your vote on your FB wall by clicking 'Share' on the vote button you just clicked.")
    String shareVoteToolTip();

    @DefaultStringValue("Following are the conditions that the pardoner has put and the confesee shall only be pardoned when these conditions are met.")
    String pardonConditionHelpInfoToolTip();

    @DefaultStringValue("The Confessee is pardoned with a condition to reveal identity to the world.")
    String pardonConditionUnHideHelpInfoToolTip();

    @DefaultStringValue("The Confessee is pardoned with a condition to get required number of 'Should be pardoned' votes from the world.")
    String pardonConditionSPVoteHelpInfoToolTip();

    @DefaultStringValue("share")
    String activityButtonShareButtonLabel();

    @DefaultStringValue(" has earned ")
    String activityButtonShareClickText1();

    @DefaultStringValue(" 'HUMAN POINTS' for sharing on wall.")
    String activityButtonShareClickText2();

    @DefaultStringValue(" characters remaining")
    String confessionTextBoxRemainingCharactersMessage();

    @DefaultStringValue("Please enter valid text.")
    String confessionTextBoxErrorMessage();

    @DefaultStringValue("Please enter a valid title.")
    String confessionTitleErrorMessage();

    @DefaultStringValue("Reveal your identity to the world by clicking this.")
    String unHideIdentityButtonTitleUserControl();

    @DefaultStringValue("Hide your identity from the world by clicking this.")
    String hideIdentityButtonTitleUserControl();

    @DefaultStringValue("Hide Confession from public wall")
    String hideConfessionButtonTitleUserControl();

    @DefaultStringValue("Publish your confession to the world by clicking this.")
    String unhideConfessionButtonTitleUserControl();

    @DefaultStringValue("Type your friend's name and choose from the options")
    String shareConfessionSuggestBoxErrorMessage();

    @DefaultStringValue("Request / Dedicate")
    String shareConfessionButtonShareConfessionPopup();

    @DefaultStringValue(" has confessed to you on Confession Box.")
    String shareConfessionFBWallMessage();

    @DefaultStringValue("Ask for pardon from some one.")
    String shareConfessionUserControlButtonTitle();

    @DefaultStringValue("Update your confession.")
    String editConfessionUserControlButtonTitle();

    @DefaultStringValue("more")
    String moreLink();

    @DefaultStringValue("less")
    String lessLink();

    @DefaultStringValue("Select view")
    String filterAllConfessions();

    @DefaultStringValue("Confessions I Subscribed")
    String filterSubscribedConfessions();

    @DefaultStringValue("All Anonymous confessions")
    String filterHiddenIdlConfessions();

    @DefaultStringValue("Global confession wall")
    String filterGlobalConfessions();
    
    @DefaultStringValue("Local confession wall")
    String filterLocaleConfessions();

    @DefaultStringValue("Most 'SAME BOAT'")
    String filterSBVoteConfessions();

    @DefaultStringValue("Most 'LAME'")
    String filterLameVotedConfessions();

    @DefaultStringValue("Most 'SYMPATHAISED'")
    String filterSymVoteConfessions();

    @DefaultStringValue("Most 'SHOULD BE PARDONED'")
    String filterSPVoteConfessions();

    @DefaultStringValue("Most 'SHOULD NOT BE PARDONED'")
    String filterSNPVoteConfessions();
    
    @DefaultStringValue("All Open confessions")
    String filterOpenIdConfessions();

    @DefaultStringValue("Confessions I voted")
    String filterUserActivityConfessions();

    @DefaultStringValue("less than a min ago")    
    String timestampLessThanMinut();

    @DefaultStringValue(" minutes ago")
    String timestampMinutesAgo();

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

    @DefaultStringValue("invites you to Confession Box.")
    String inviteFriendsTextMessage();

    @DefaultStringValue("Shows the Feed preview of the confession visible to the world.")
    String feedPreviewLinkToolTip();

    @DefaultStringValue("world")
    String relationDefaultDisplayText();

    @DefaultStringValue("Preview how this confession appears to everyone.")
    String feedViewPreviewLink();

    @DefaultStringValue("Pardoned with condition: This Confession has been pardoned with the following conditions.")
    String pardonConditionInfoText();

    @DefaultStringValue("Saved")
    String saveDraftButtonText();
    
    @DefaultStringValue("Your confession is now being submitted. Press 'SUBMIT CONFESSION' to proceed with submitting the confession or press 'EDIT' if you want to edit your confession.")
    String confirmMessageWhenSubmittingConfession();

    @DefaultStringValue("Add update")
    String addUpdateLinkText();

    @DefaultStringValue("Add response (Anonymous)")
    String addResponseLinkText();

    @DefaultStringValue("Request for pardon")
    String appealForPardonLinkText();

    @DefaultStringValue("Type your friend's name and choose from the options")
    String friendsSuggestionBoxLabel();

    @DefaultStringValue("Relation of this person to you. (example: friend, brother, wife, uncle, boss etc)")
    String relationsSuggestionBoxLabel();

    @DefaultStringValue("Please choose one of the options provided (Choose 'FRIEND' if not in the options)")
    String relationsSuggestionBoxErrorMessage();

    @DefaultStringValue("Click again to undo your vote till the timer reaches '0'.")
    String undoToolTipBar();

    @DefaultStringValue("Share your vote on you FB wall")
    String shareToolTipBar();

    @DefaultStringValue("Submit")
    String confessionUpdateButtonLabelText();

    @DefaultStringValue("Confessee")
    String confesseeRelationNameUpdateLabel();

    @DefaultStringValue("Show updates")
    String updateWidgetShowUpdatesLabelText();

    @DefaultStringValue("Updates")
    String mobUpdateWidgetShowUpdatesLabelText();
    
    @DefaultStringValue("No updates")
    String updateWidgetNoUpdatesText();

    @DefaultStringValue("Hide updates")
    String updateWidgetHideUpdatesLabelText();

    @DefaultStringValue("You are logged out now!")
    String logoutInfoMessage();

    @DefaultStringValue("You need to login and provide required permissions to the Confession Box.")
    String requireLoginToBeActiveInfoMessage();

    @DefaultStringValue("Since")
    String dateTimeStampPrefix();

    @DefaultStringValue("Pardon")
    String pardonButtonLabelText();

    @DefaultStringValue("About Confession Box")
    String aboutConfessionBoxFooterLinkLabel();

    @DefaultStringValue("Privacy policy")
    String privacyPolicyFooterLinkLabel();

    @DefaultStringValue("Confession Box community")
    String cbNameFooterTextLabel();

    @DefaultStringValue("Human Points")
    String humanPointWidgetLabelText();

    @DefaultStringValue("Invite Friends")
    String inviteFriendsLinkLabelText();

    @DefaultStringValue("CB RuleBook")
    String cbRuleBookLinkLabelText();

    @DefaultStringValue("Logout")
    String logoutLinkLabelText();

    @DefaultStringValue("Submit your confession below")
    String registerConfessionInstructionTextOne();

    @DefaultStringValue("Your identity is never revealed to anyone unless you choose to do so. The confession is not posted on Facebook wall by default.")
    String registerConfessionInstructionTextTwo();

    @DefaultStringValue("Save as draft")
    String saveAsDraftButtonLabelText();

    @DefaultStringValue("Delete draft")
    String deleteDraftButtonLabelText();

    @DefaultStringValue("The person has chosen not to reveal identity to the world.")
    String profileNameAnonymousTileText();

    @DefaultStringValue("Refresh")
    String refreshButtonToolTipText();

    @DefaultStringValue("his")
    String malePronoun();

    @DefaultStringValue("her")
    String femalePronoun();

    @DefaultStringValue("Press pardon button to grant pardon or choose your conditions for pardon.")
    String pardonButtonHelpInfoToolTip();

    @DefaultStringValue("The Philosophy")
    String phillosphyFooterLinkLabel();

    @DefaultStringValue("Anonymous confession")
    String phillosphyAnonConfFooterLinkLabel();

    @DefaultStringValue("Click to un-subscribe")
    String unSubscribeLinkToolTipText();

    @DefaultStringValue("SHARE your vote with your friends")
    String suggestShareVote();

    @DefaultStringValue("Most Voted Confession")
    String filterMostVotedConfessions();
}