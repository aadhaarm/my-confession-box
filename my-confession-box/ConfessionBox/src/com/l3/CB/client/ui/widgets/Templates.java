package com.l3.CB.client.ui.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;

public class Templates {
    
    public interface CBTemplates extends SafeHtmlTemplates {
	
	@Template("'{0} confessed to {2} {1}'")
	SafeHtml confessonPreview(String confesee, String confessor, String pronoun);

	@Template("<div class=\"updateText\"><span class=\"confessionRelation\">{0}</span>: {1}</div><div class=\"time_stamp_update\">{2}</div>")
	SafeHtml confessonUpdate(String confessionRelation, String updateText, String timeStamp);

	@Template("<br/>confessed to <a href=\"{0}\" target=\"_BLANK\">{1}</a> (as '<b>{2}</b>' on feed wall)")
	SafeHtml confessedToPersomalWall(SafeUri profileLink, String name, String relation);

	@Template("{0} confessed to {1}")
	SafeHtml confessedToShare(String confesseeName, String pardonerProNoun);
	
	@Template("{0} earned {1} human points for sharing.")
	SafeHtml shareDescription(String sharerName, int pointsEarned);
	
	@Template("<b>{0}</b> has requested pardon from you for the following confession. {1} identity is hidden to the world except you. If you wish to grant {2} Pardon (or pardon with conditions), press the <b>Pardon</b> button below.")
	SafeHtml confessedToYouWallPardonMessage(String name, String his, String him);
	
	@Template("confessed to {0} <b>{1}</b>")
	SafeHtml confessedToFeedWall(String pronoun,String userName);

	@Template("{0} [<a class=\"questionMark\">?</a>]")
	SafeHtml undoToolTip(String tooltip);

	@Template("{0} [<a class=\"questionMark\">?</a>]")
	SafeHtml shareToolTip(String tooltip);

	@Template("{0}.[<a class=\"questionMark\">?</a>]")
	SafeHtml pardonConditionInfoText(String pardonConditionText);

	@Template("Condition {0}.[<a class=\"questionMark\">?</a>] {1}")
	SafeHtml pardonCondition(String condition, String statusTick);
    
	@Template("<b>{0}</b><br/><div class=\"subtext\">{1} [<a class=\"questionMark_bl\">?</a>]</div>")
	SafeHtml registerConfessionInstructionText(String mainText, String subText);

	@Template("CONFESSION BOX")
	SafeHtml applicationTitle();

	@Template("speak your heart, live free")
	SafeHtml applicationTagLine();

//	@Template("<a>{0}<span class=\"custom warning\">{1}</span></a>")
//	SafeHtml warningToolTip(String linkText, String toolTip);
//
//	@Template("<a>{0}<span class=\"custom critical\">{1}</span></a>")
//	SafeHtml errorToolTip(String linkText, String toolTip);
//	
//	@Template("<a>{0}<span class=\"custom help\">{1}</span></a>")
//	SafeHtml helpToolTip(String linkText, String toolTip);
//	
	@Template("<a title=\"{1}\">{0}</a>")
	SafeHtml infoToolTip(String linkText, String toolTip);

	@Template("{0} in not a member of confessiom Box so it can not send a notification about your confession. Please press 'SEND' in the next window to send a message to {0} informing about the confession you have made.")
	SafeHtml confessorNotRegisteredMessage(String pardonerName);

	@Template("{0}")
	SafeHtml confessionConfirmMessage(String messageText);

	@Template("<img src='{0}'/> {1}")
	SafeHtml voteHelp(SafeUri imagePath, String messageText);

    }
    
    public static final CBTemplates TEMPLATES = GWT.create(CBTemplates.class);
}