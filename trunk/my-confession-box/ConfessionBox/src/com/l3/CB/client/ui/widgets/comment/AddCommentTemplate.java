package com.l3.CB.client.ui.widgets.comment;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.comment.CommentEvent;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Comment;

public class AddCommentTemplate extends Composite implements HasText {

    private static AddCommentTemplateUiBinder uiBinder = GWT
	    .create(AddCommentTemplateUiBinder.class);

    interface AddCommentTemplateUiBinder extends
    UiBinder<Widget, AddCommentTemplate> {
    }

    Long confId;

    @UiField
    Button btnCommentAsAnyn;
    boolean boolCommentAsAnon;

    @UiField
    Image imgProfile;

    @UiField
    TextArea txtComment;

    @UiField
    Button btnSubmit;

    @UiField
    ParagraphElement pStatusMessage;

    Timer timer = new Timer() {
	@Override
	public void run() {
	    ConfessionBox.eventBus.fireEvent(new CommentEvent(confId));
	    txtComment.setText("");
	    txtComment.setEnabled(true);
	    btnSubmit.setEnabled(true);
	    btnCommentAsAnyn.setEnabled(true);
	}
    };

    Timer timerShowMessage = new Timer() {
	@Override
	public void run() {
	    pStatusMessage.removeClassName("show");
	    pStatusMessage.addClassName("hide");
	}
    };

    public AddCommentTemplate() {
	initWidget(uiBinder.createAndBindUi(this));
	pStatusMessage.addClassName("hide");
	btnSubmit.getElement().setAttribute("data-uk-tooltip", "");
	btnCommentAsAnyn.getElement().setAttribute("data-uk-tooltip", "");
    }

    public AddCommentTemplate(Long confId) {
	initWidget(uiBinder.createAndBindUi(this));
	btnSubmit.getElement().setAttribute("data-uk-tooltip", "");
	btnCommentAsAnyn.getElement().setAttribute("data-uk-tooltip", "");
	pStatusMessage.addClassName("hide");
	initialize(confId);
    }

    /**
     * @param confId
     */
    public void initialize(Long confId) {
	this.confId = confId;
	setupImage();

	getCommentWidth();

	bind();
    }

    /**
     * @return
     */
    private void getCommentWidth() {
	if(ConfessionBox.isMobile) {
	    int width = Window.getClientWidth() - 110;
	    txtComment.setWidth(width + "px");
	} else {
	    txtComment.setCharacterWidth(68);
	}
    }

    private void bind() {
	btnCommentAsAnyn.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if(ConfessionBox.isLoggedIn) {
		    boolCommentAsAnon = !boolCommentAsAnon;
		    if(boolCommentAsAnon) {
			btnCommentAsAnyn.addStyleName("uk-button-primary");
		    } else {
			btnCommentAsAnyn.removeStyleName("uk-button-primary");
		    }
		} else {
		    CommonUtils.login(0);
		    boolCommentAsAnon = false;
		}
		setupImage();
	    }
	});
    }

    /**
     * Setup image according to gender and visibility preference 
     */
    private void setupImage() {
	String gender = "male";
	if(ConfessionBox.getLoggedInUserInfo() != null) {
	    gender = ConfessionBox.getLoggedInUserInfo().getGender();
	    String fbId = ConfessionBox.getLoggedInUserInfo().getId();
	    if (boolCommentAsAnon) {
		imgProfile.setUrl(FacebookUtil.getFaceIconImage(gender));
	    } else if(fbId != null){
		imgProfile.setUrl(FacebookUtil.getUserImageUrl(fbId));
		CommonUtils.parseXFBMLJS(imgProfile.getElement());
	    } else {
		imgProfile.setUrl(FacebookUtil.getFaceIconImage(gender));
	    }
	} else {
	    imgProfile.setUrl(FacebookUtil.getFaceIconImage(gender));
	}
    }

    public void setText(String text) {
    }

    public String getText() {
	return null;
    }

    @UiHandler("btnSubmit")
    void onSubmit(ClickEvent e) {
	if(ConfessionBox.isLoggedIn) {
	    if(validate()) {
		Comment c = getComment();

		txtComment.setEnabled(false);
		btnSubmit.setEnabled(false);
		btnCommentAsAnyn.setEnabled(false);

		ConfessionBox.confessionService.saveComment(c, new AsyncCallback<Void>() {

		    @Override
		    public void onSuccess(Void result) {
			timer.cancel();
			timer.schedule(2000);
			
			timerShowMessage.cancel();
			pStatusMessage.addClassName("show");
			timerShowMessage.schedule(2000);
		    }

		    @Override
		    public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub

		    }
		});
	    } else {
		Window.alert("Enter a valid comment.");
	    }
	} else {
	    CommonUtils.login(0);
	}
    }

    private boolean validate() {
	if(txtComment != null && txtComment.getText() != null && txtComment.getText().length() >= 3) {
	    return true;
	}
	return false;
    }

    private Comment getComment() {
	Comment comment = new Comment();
	comment.setConfId(confId);

	if(ConfessionBox.getLoggedInUserInfo() != null) {
	    comment.setFbId(ConfessionBox.getLoggedInUserInfo().getId());
	}

	comment.setShareAsAnyn(boolCommentAsAnon);
	comment.setComment(txtComment.getText());
	comment.setGender(ConfessionBox.getLoggedInUserInfo().getGender());
	comment.setLocale(ConfessionBox.getLoggedInUserInfo().getLocale());
	comment.setTimeStamp(new Date());
	comment.setUserId(ConfessionBox.getLoggedInUserInfo().getUserId());

	return comment;
    }
}
