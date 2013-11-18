package com.l3.CB.client.ui.widgets.comment;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.comment.CommentEvent;
import com.l3.CB.client.event.comment.CommentEventHandler;
import com.l3.CB.shared.CommentFilter;
import com.l3.CB.shared.TO.Comment;

public class CommentListTemplate extends Composite {

    private static CommentListTemplateUiBinder uiBinder = GWT
	    .create(CommentListTemplateUiBinder.class);

    interface CommentListTemplateUiBinder extends
    UiBinder<Widget, CommentListTemplate> {
    }

    private Long confId;
    private int page = 0;
    private int commentsAdded = 0;

    private CommentReadTemplate commentReadTemplate = null;

    @UiField
    SpanElement numComments;

    @UiField
    HTMLPanel hPnlBody;
    
    @UiField
    Anchor ancMore;

    public CommentListTemplate() {
	initWidget(uiBinder.createAndBindUi(this));
	bind();
    }

    public CommentListTemplate(String firstName) {
	initWidget(uiBinder.createAndBindUi(this));
	bind();
    }

    public CommentListTemplate(long confId) {
	initWidget(uiBinder.createAndBindUi(this));

	this.confId = confId;
	
	getComments(true);
	
	bind();
    }

    private void bind() {
	ConfessionBox.eventBus.addHandler(CommentEvent.TYPE, new CommentEventHandler() {
	    @Override
	    public void commentAdded(CommentEvent event) {
		if(event.getConfId().longValue() == confId.longValue()) {
		    page = 0;
		    getComments(true);
		}
	    }
	});
    }


    @UiHandler("ancMore")
    void loadMoreComments(ClickEvent event) {
	page = page + 1;
	getComments(false);
    }
    
    private void getComments(final boolean clear) {
	ConfessionBox.confessionService.getComments(getFilter(), new AsyncCallback<CommentFilter>() {
	    @Override
	    public void onSuccess(CommentFilter result) {
		
		if(clear) {
		    hPnlBody.clear();
		}

		if(result != null && result.getComments() != null && !result.getComments().isEmpty()) {
		    for (final Comment comment : result.getComments()) {
			if(comment != null) {
			    commentReadTemplate = new CommentReadTemplate(comment);
			    hPnlBody.add(commentReadTemplate);
			}
		    }
		    commentsAdded = commentsAdded + result.getComments().size();
		} else {
		    ancMore.setVisible(false);
		}
		
		numComments.setInnerText(result.getTotalNumberOfComments() + "");
		if(result.getTotalNumberOfComments() <= commentsAdded) {
		    if(commentReadTemplate != null) {
			commentReadTemplate.hideLastHR();
		    }
		    ancMore.setVisible(false);
		}
	    }

	    @Override
	    public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub

	    }
	});
    }

    private CommentFilter getFilter() {
	CommentFilter commentFilter = new CommentFilter();
	commentFilter.setConfId(confId);
	commentFilter.setPage(page);
	commentFilter.setPageSize(2);
	return commentFilter;
    }

    public void initializeWidget(Long confId2) {
	this.confId = confId2;
	getComments(true);
    }    
}