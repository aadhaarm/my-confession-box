package com.l3.CB.client.ui.widgets.comment;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.CommentFilter;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Comment;
import com.l3.CB.shared.TO.UserInfo;

public class CommentReadTemplate extends Composite implements HasText {

    private static CommentReadTemplateUiBinder uiBinder = GWT
	    .create(CommentReadTemplateUiBinder.class);

    interface CommentReadTemplateUiBinder extends
    UiBinder<Widget, CommentReadTemplate> {
    }

    public CommentReadTemplate() {
	initWidget(uiBinder.createAndBindUi(this));
    }

    Comment comment;

    @UiField
    SpanElement strComment;

    @UiField
    SpanElement nameSpan;

    @UiField
    SpanElement timeStamp;

    @UiField
    Button btnSecond;

    @UiField
    Button btnDislike;

    @UiField
    SpanElement numOfSecond;

    @UiField
    SpanElement numOfDislike;

    @UiField
    Image imgProfile;

    public CommentReadTemplate(Comment comment) {
	initWidget(uiBinder.createAndBindUi(this));
	this.comment = comment;
	if(comment != null) {
	    btnSecond.setText("L");
	    btnDislike.setText("D");
	    this.strComment.setInnerText(comment.getComment());
	    this.numOfDislike.setInnerText(comment.getNumOfAbuseVote() + "");
	    this.numOfSecond.setInnerText(comment.getNumOfSecond() + "");
	    this.timeStamp.setInnerText(CommonUtils.getDateInAGOFormat(comment.getTimeStamp()));

	    if(comment.isShareAsAnyn()) {
		this.nameSpan.setInnerText(ConfessionBox.cbText.confessedByAnynName());
		imgProfile.setUrl(FacebookUtil.getFaceIconImage(comment.getGender()));
	    } else {
		if(comment.getUserDetailsJSON() != null) {
		    UserInfo userInfo = FacebookUtil.getUserInfo(comment.getUserDetailsJSON());
		    if(userInfo != null) {
			this.nameSpan.setInnerText(userInfo.getFirst_name());
			imgProfile.setUrl(FacebookUtil.getUserImageUrl(comment.getFbId()));
			//		    imgProfile.setUrl(userInfo.getLink());
		    }
		}
	    }
	}
    }

    @Override
    public String getText() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void setText(String text) {
	// TODO Auto-generated method stub

    }

    @UiHandler("btnSecond")
    void onSecondClick(ClickEvent e) {
	btnSecond.setEnabled(false);
	CommentFilter filter = new CommentFilter();
	filter.setCommentId(comment.getCommId());
	filter.setVoteReport(false);
	filter.setVoteSecond(true);

	ConfessionBox.confessionService.voteOnComment(filter , new AsyncCallback<Void>() {

	    @Override
	    public void onSuccess(Void result) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub

	    }
	});
    }
    @UiHandler("btnDislike")
    void onDislikeClick(ClickEvent e) {
	btnDislike.setEnabled(false);

	CommentFilter filter = new CommentFilter();

	filter.setCommentId(comment.getCommId());
	filter.setVoteReport(true);
	filter.setVoteSecond(false);

	ConfessionBox.confessionService.voteOnComment(filter , new AsyncCallback<Void>() {

	    @Override
	    public void onSuccess(Void result) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub

	    }
	});
    }
}
