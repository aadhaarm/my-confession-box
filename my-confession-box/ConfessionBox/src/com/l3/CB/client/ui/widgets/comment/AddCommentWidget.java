package com.l3.CB.client.ui.widgets.comment;

import com.google.gwt.user.client.ui.FlowPanel;

public class AddCommentWidget extends FlowPanel {

    private Long confId;

//    private HTMLPanel imageContainer;
//    private Image imgProfile;
//    private TextArea txtComment;
//    private CheckBox cBoxAnynComment;
//    private Button btnSubmit;

    
    
    public AddCommentWidget(Long confId) {
	super();
	this.confId = confId;

	initializeWidget();

	bind();
    }

    public AddCommentWidget() {
	super();
	bind();
    }

    private void bind() {
//	cBoxAnynComment.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//	    @Override
//	    public void onValueChange(ValueChangeEvent<Boolean> event) {
//		//Profile picture
//		imgProfile = getProfileImage();
//		imageContainer.clear();
//		imageContainer.add(imgProfile);
//	    }
//	});

//	btnSubmit.addClickHandler(new ClickHandler() {
//	    @Override
//	    public void onClick(ClickEvent event) {
//		ConfessionBox.confessionService.saveComment(getComment(), new AsyncCallback<Void>() {
//
//		    @Override
//		    public void onSuccess(Void result) {
//
//		    }
//
//		    @Override
//		    public void onFailure(Throwable caught) {
//			// TODO Auto-generated method stub
//
//		    }
//		});
//	    }
//
//	});
    }

//    private Comment getComment() {
//	Comment comment = new Comment();
//	comment.setConfId(confId);
//
//	if(ConfessionBox.getLoggedInUserInfo() != null) {
//	    comment.setFbId(ConfessionBox.getLoggedInUserInfo().getId());
//	}
//
//	comment.setShareAsAnyn(cBoxAnynComment.getValue());
//	comment.setComment(txtComment.getText());
//	comment.setGender(ConfessionBox.getLoggedInUserInfo().getGender());
//	comment.setLocale(ConfessionBox.getLoggedInUserInfo().getLocale());
//	comment.setTimeStamp(new Date());
//	comment.setUserId(ConfessionBox.getLoggedInUserInfo().getUserId());
//
//	return comment;
//    }

    public void initializeWidget(Long confId) {
	this.confId = confId;
	initializeWidget();
    }
    
    private void initializeWidget() {
	AddCommentTemplate addCommentWidget = new AddCommentTemplate(confId);
	this.add(addCommentWidget);
	
	//	//Profile picture
//	imageContainer = new HTMLPanel("");
//	imgProfile = getProfileImage();
//	imageContainer.add(imgProfile);
//	// Comment
//	txtComment = new TextArea();
//	// Check-box
//	cBoxAnynComment = new CheckBox();
//	// Submit
//	btnSubmit = new Button("Send");
//
//	this.add(imageContainer);
//	this.add(txtComment);
//	this.add(cBoxAnynComment);
//	this.add(btnSubmit);
    }

//    private Image getProfileImage() {
//	Image profileImage = null;
//	String gender = "male";
//
//	if(ConfessionBox.getLoggedInUserInfo() != null) {
//	    gender = ConfessionBox.getLoggedInUserInfo().getGender();
//	    String fbId = ConfessionBox.getLoggedInUserInfo().getId();
//	    if (cBoxAnynComment != null && cBoxAnynComment.getValue()) {
//		profileImage = new Image(FacebookUtil.getFaceIconImage(gender));
//	    } else {
//		profileImage = new Image(FacebookUtil.getUserImageUrl(fbId));
//	    }
//	} else {
//	    profileImage = new Image(FacebookUtil.getFaceIconImage(gender));
//	}
//	profileImage.setWidth("30px");
//	profileImage.setHeight("30px");
//	profileImage.setStyleName(Constants.DIV_PROFILE_IMAGE);
//	CommonUtils.parseXFBMLJS(profileImage.getElement());
//
//	return profileImage;
//    }
}