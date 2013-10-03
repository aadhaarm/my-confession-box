package com.l3.CB.client.ui.widgets.comment;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.comment.CommentEvent;
import com.l3.CB.client.event.comment.CommentEventHandler;
import com.l3.CB.shared.CommentFilter;
import com.l3.CB.shared.TO.Comment;

public class CommentListWidget extends FlowPanel {

    private Long confId;
    private int page = 0;
    private Anchor anchMore;

    public CommentListWidget(Long confId) {
	super();
	this.confId = confId;
	this.setStyleName("coment_read");

	anchMore = new Anchor("more");
	initializeWidget(true);

	bind();
    }

    private void bind() {
	anchMore.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		page++;
		initializeWidget(false);
	    }
	});
	
	ConfessionBox.eventBus.addHandler(CommentEvent.TYPE, new CommentEventHandler() {
	    @Override
	    public void commentAdded(CommentEvent event) {
		if(event.getConfId().longValue() == confId.longValue()) {
		    page = 0;
		    initializeWidget(true);
		}
	    }
	});
    }

    private void initializeWidget(final boolean clear) {
	this.remove(anchMore);
	ConfessionBox.confessionService.getComments(getFilter(), new AsyncCallback<List<Comment>>() {
	    @Override
	    public void onSuccess(List<Comment> result) {
		if(result != null && !result.isEmpty()) {
		    if(clear) {
			clear();
		    }
		    for (final Comment comment : result) {
			if(comment != null) {
			    CommentReadTemplate commentReadTemplate = new CommentReadTemplate(comment);
			    commentReadTemplate.setStyleName("individual_comment_read");
			    add(commentReadTemplate);
			}
		    }
		    add(anchMore);
		} else {
		    remove(anchMore);
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
	commentFilter.setPageSize(3);
	return commentFilter;
    }    
}