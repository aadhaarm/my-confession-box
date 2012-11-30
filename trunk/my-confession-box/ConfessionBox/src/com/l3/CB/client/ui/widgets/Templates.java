package com.l3.CB.client.ui.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;

public class Templates {
    
    public interface CBTemplates extends SafeHtmlTemplates {
	
	@Template("<div class=\"confessionPreview\">'{0} confessed to {1}'</div>")
	SafeHtml confessonPreview(String confesee, String confessor);

	@Template("<div class=\"updateText\"><span class=\"confessionRelation\">{0}</span>: {1}</div><div class=\"time_stamp_update\">{2}</div>")
	SafeHtml confessonUpdate(String confessionRelation, String updateText, String timeStamp);

	@Template("confessed to <a href=\"{0}\" target=\"_BLANK\">{1}</a> (as '{2}' on feed wall)")
	SafeHtml confessedToPersomalWall(SafeUri profileLink, String name, String relation);

	@Template("confessed to {0} {1}")
	SafeHtml confessedToFeedWall(String pronoun,String userName);

	@Template("{0} [<a class=\"questionMark\">?</a>]")
	SafeHtml undoToolTip(String tooltip);

	@Template("{0} [<a class=\"questionMark\">?</a>]")
	SafeHtml shareToolTip(String tooltip);

	@Template("{0}.[<a class=\"questionMark\">?</a>]")
	SafeHtml pardonConditionInfoText(String pardonConditionText);

	@Template("{0}.[<a class=\"questionMark\">?</a>] {1}")
	SafeHtml pardonCondition(String condition, String statusTick);
    
	@Template("<b>{0}</b><br/><span class=\"subtext\">{1}</span> [<a class=\"questionMark\">?</a>]")
	SafeHtml registerConfessionInstructionText(String mainText, String subText);

	@Template("<div class=\"logoLink\">CONFESSION BOX</div><div class=\"tagLineText\">speak your heart, live free</div>")
	SafeHtml applicationLogoAndTagLine();
    }
    
    public static final CBTemplates TEMPLATES = GWT.create(CBTemplates.class);
}