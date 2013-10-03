package com.l3.CB.client.ui.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.shared.FacebookUtil;


public class FBLikeWidget {

    HTML fbLikeHtml;

    public interface FBLikeTemplate extends SafeHtmlTemplates {
	@Template("<fb:like href=\"{0}\" send=\"true\" width=\"{1}\" show_faces=\"false\" font=\"lucida grande\"></fb:like>")
	SafeHtml messageWithLink(SafeUri url, String width);
    }

    private static final FBLikeTemplate TEMPLATES = GWT.create(FBLikeTemplate.class);

    public FBLikeWidget(final Long confId) {
	super();
	SafeUri url = new SafeUri() {

	    @Override
	    public String asString() {
		return FacebookUtil.getActivityUrl(confId);
	    }
	};
	InlineHTML messageWithLinkInlineHTML = new InlineHTML(
		TEMPLATES.messageWithLink(url, Integer.toString(getCommentWidth())));
	fbLikeHtml = new HTML(messageWithLinkInlineHTML.getHTML());
	fbLikeHtml.setStyleName("fbLikeBar");
    }

    public HTML getFbLikeHtml() {
	return fbLikeHtml;
    }
    
    /**
     * @return
     */
    private int getCommentWidth() {
	int width = Window.getClientWidth() - 100;
	if(ConfessionBox.isMobile) {
	    return width;
	} else {
	    return 542;
	}
    }
}
