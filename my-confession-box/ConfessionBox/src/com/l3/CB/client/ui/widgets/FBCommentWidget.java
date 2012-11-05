package com.l3.CB.client.ui.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.l3.CB.shared.FacebookUtil;


public class FBCommentWidget {

	HTML fbCommentHtml;
	
	public interface FBCommentTemplate extends SafeHtmlTemplates {
		@Template("<fb:comments href=\"{0}\" num_posts=\"2\" width=\"550\"></fb:comments>")
		SafeHtml messageWithLink(SafeUri url);
	}

	private static final FBCommentTemplate TEMPLATES = GWT.create(FBCommentTemplate.class);
	
	public FBCommentWidget(final Long confId) {
		super();
	    SafeUri url = new SafeUri() {
			
			@Override
			public String asString() {
				return FacebookUtil.getActivityUrl(confId);
			}
		};
	    InlineHTML messageWithLinkInlineHTML = new InlineHTML(
	        TEMPLATES.messageWithLink(url));
	    fbCommentHtml = new HTML(messageWithLinkInlineHTML.getHTML()); 
	}

	public HTML getFbCommentHtml() {
		return fbCommentHtml;
	}
}
