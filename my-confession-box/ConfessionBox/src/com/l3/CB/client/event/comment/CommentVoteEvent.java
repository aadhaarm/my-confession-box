package com.l3.CB.client.event.comment;

import com.google.gwt.event.shared.GwtEvent;

public class CommentVoteEvent extends GwtEvent<CommentVoteEventHandler> {
    public static Type<CommentVoteEventHandler> TYPE = new Type<CommentVoteEventHandler>();

    private final Long commentId;

    private boolean voteReport = false;
    
    private boolean voteSecond = false;
    
    public CommentVoteEvent(Long commentId, boolean voteReport, boolean voteSecond) {
	super();
	this.commentId = commentId;
	this.voteReport = voteReport;
	this.voteSecond = voteSecond;
    }

    @Override
    public Type<CommentVoteEventHandler> getAssociatedType() {
	return TYPE;
    }

    @Override
    protected void dispatch(CommentVoteEventHandler handler) {
	handler.registerVote(this);
    }

    public boolean isVoteReport() {
        return voteReport;
    }

    public void setVoteReport(boolean voteReport) {
        this.voteReport = voteReport;
    }

    public boolean isVoteSecond() {
        return voteSecond;
    }

    public void setVoteSecond(boolean voteSecond) {
        this.voteSecond = voteSecond;
    }

    public Long getCommentId() {
        return commentId;
    }
}