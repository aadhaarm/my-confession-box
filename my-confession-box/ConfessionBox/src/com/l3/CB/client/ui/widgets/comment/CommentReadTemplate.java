package com.l3.CB.client.ui.widgets.comment;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HRElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.CommentFilter;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Comment;
import com.l3.CB.shared.TO.UserInfo;

public class CommentReadTemplate extends Composite {

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
    Anchor nameSpan;

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

    @UiField
    Anchor ancMore;
    
    @UiField
    HRElement hr;
    
    public CommentReadTemplate(Comment comment) {
	initWidget(uiBinder.createAndBindUi(this));
	this.comment = comment;
	
	btnDislike.getElement().setAttribute("data-uk-tooltip", "");
	btnSecond.getElement().setAttribute("data-uk-tooltip", "");
	
	if(comment != null) {

	    /*
	     * Comment Text
	     */
	    this.strComment.setInnerText(CommonUtils.trunkate(comment.getComment(), 150));
	    if(comment.getComment() != null && comment.getComment().length() < 200) {
		ancMore.setVisible(false);
	    }
	    
	    this.numOfDislike.setInnerText(comment.getNumOfAbuseVote() + "");
	    this.numOfSecond.setInnerText(comment.getNumOfSecond() + "");
	    this.timeStamp.setInnerText(CommonUtils.getDateInAGOFormat(comment.getTimeStamp()));

	    if(comment.isShareAsAnyn()) {
		this.nameSpan.setText(ConfessionBox.cbText.confessedByAnynName());
		imgProfile.setUrl(FacebookUtil.getFaceIconImage(comment.getGender()));
	    } else {
		if(comment.getUserDetailsJSON() != null) {
		    UserInfo userInfo = FacebookUtil.getUserInfo(comment.getUserDetailsJSON());
		    if(userInfo != null) {
			this.nameSpan.setText(userInfo.getName());
			this.nameSpan.setHref(userInfo.getLink());
			imgProfile.setUrl(FacebookUtil.getUserImageUrl(userInfo.getId()));
		    }
		}
	    }
	}
    }

    @UiHandler("ancMore")
    void showMore(ClickEvent event) {
	this.strComment.setInnerText(comment.getComment());
	ancMore.setVisible(false);
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
		btnSecond.addStyleName(Constants.STYLE_CLASS_BLUE_BUTTON);
		comment.setNumOfSecond(comment.getNumOfSecond() + 1);
		numOfSecond.setInnerText(comment.getNumOfSecond() + "");
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
		btnDislike.addStyleName(Constants.STYLE_CLASS_BLUE_BUTTON);
		comment.setNumOfAbuseVote(comment.getNumOfAbuseVote() + 1);
		numOfDislike.setInnerText(comment.getNumOfAbuseVote() + "");
	    }

	    @Override
	    public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub

	    }
	});
    }

    public void hideLastHR() {
	hr.removeFromParent();
    }
}
