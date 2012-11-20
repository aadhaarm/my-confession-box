package com.l3.CB.client.view;

import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.presenter.RegisterConfessionPresenter;
import com.l3.CB.client.ui.widgets.CBTextArea;
import com.l3.CB.client.ui.widgets.CBTextBox;
import com.l3.CB.client.ui.widgets.FriendsSuggestBox;
import com.l3.CB.client.ui.widgets.RelationSuggestBox;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.HelpInfo;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

public class RegisterConfessionView extends Composite implements RegisterConfessionPresenter.Display {

    private final FlowPanel fPnlConfessionForm;
    private final FlowPanel fPnlTop;
    private final FlowPanel fPnlOptions;
    private final FlowPanel fPnlConfession;
    private final CheckBox cbHideIdentity;
    private final CheckBox cbConfessTo;
    private FriendsSuggestBox friendsSuggestBox;
    private RelationSuggestBox relationSuggestBox;
    private final CBTextBox txtTitle;
    private final CBTextArea txtConfession;
    private final FlowPanel fPnlButtons;
    private final Button btnSubmit;
    private final Button btnSave;
    private final Button btnDeleteDraft;
    
    public RegisterConfessionView() {
	super();
	DecoratorPanel contentTableDecorator = new DecoratorPanel();
	contentTableDecorator.addStyleName("reg_main_container");
	initWidget(contentTableDecorator);

	//TOP panel
	fPnlConfessionForm = new FlowPanel();

	fPnlTop = new FlowPanel();
	fPnlTop.setStyleName("selections");
	
	HTML topTitle = new HTML("<b>Submit your confession below</b><br/><span class=\"subtext\">Your confession is never shared to any one unless you opt for the same</span> [?]");
	topTitle.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		HelpInfo.showHelpInfo(HelpInfo.type.REGISTER_CONFESSION_TITLE_HELP);
	    }
	});
	
	topTitle.setStyleName("header_text");
	fPnlTop.add(topTitle);

	fPnlConfessionForm.add(fPnlTop);
	
	// Options panel
	fPnlOptions = new FlowPanel();
	fPnlOptions.setStyleName("options");

	HTML optionsTitle = new HTML("Confession Sharing Options [?]");
	optionsTitle.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		HelpInfo.showHelpInfo(HelpInfo.type.REGISTER_CONFESION_OPTIONS);
	    }
	});
	optionsTitle.setStyleName("options_title");
	fPnlOptions.add(optionsTitle);
	
	cbHideIdentity = new CheckBox(ConfessionBox.cbText.registerPageOptionHideID());
	cbHideIdentity.setValue(true);
	fPnlOptions.add(cbHideIdentity);
	
	cbConfessTo = new CheckBox(ConfessionBox.cbText.registerPageOptionConfessToFriend());
	fPnlOptions.add(cbConfessTo);

	fPnlConfessionForm.add(fPnlOptions);
	
	// Confession
	fPnlConfession = new FlowPanel();
	fPnlConfession.setStyleName("confession");
	
	txtTitle = new CBTextBox();
	fPnlConfessionForm.add(txtTitle);
	
	txtConfession = new CBTextArea(Constants.CONF_MAX_CHARS, true);
	txtConfession.setStyleName(Constants.STYLE_CLASS_REGISTER_CONFESSION_TXT_BOX);
	txtConfession.setSize("100%", "14em");

	fPnlConfessionForm.add(txtConfession);
	
	// Buttons
	fPnlButtons = new FlowPanel();
	fPnlButtons.setStyleName("buttons");

	btnSave = new Button("Save as draft");
	btnSave.setStyleName("save_draft");
	fPnlButtons.add(btnSave);
	
	btnDeleteDraft = new Button("X");
	btnDeleteDraft.setTitle("Delete draft");
	btnDeleteDraft.setStyleName("save_draft");
	btnDeleteDraft.setVisible(false);
	fPnlButtons.add(btnDeleteDraft);
	
	btnSubmit = new Button(ConfessionBox.cbText.buttonTextSubmitConfession());
	btnSubmit.setStyleName("submit_confession");
	fPnlButtons.add(btnSubmit);
	
	fPnlConfessionForm.add(fPnlButtons);
	contentTableDecorator.add(fPnlConfessionForm);	
    }

    @Override
    public void setFriends(Map<String, UserInfo> userfriends) {
	friendsSuggestBox = new FriendsSuggestBox(userfriends);
	fPnlOptions.add(friendsSuggestBox);
	friendsSuggestBox.setFocus();
	
	relationSuggestBox = new RelationSuggestBox();
	fPnlOptions.add(relationSuggestBox);
    }
    
    @Override
    public Widget gethFriendSuggestBox() {
	return friendsSuggestBox;
    }

    @Override
    public Widget asWidget() {
	return this;
    }

    @Override
    public HasClickHandlers getSubmitBtn() {
	return this.btnSubmit;
    }

    @Override
    public String getConfession() {
	return this.txtConfession.getCbTextArea().getHTML();
    }

    @Override
    public boolean isIdentityHidden() {
	return cbHideIdentity.getValue();
    }

    @Override
    public boolean isFriendsListNull() {
	return (friendsSuggestBox == null);
    }

    @Override
    public String getConfessionTitle() {
	return txtTitle.getTxtTitle().getText();
    }

    @Override
    public UserInfo getSharedWith() {
	if(friendsSuggestBox.validate()) {
	    return friendsSuggestBox.getSelectedUser();
	}
	return null;
    }

    /**
     * @param isAnyn -  Is Anonymous
     * @param gender - String
     * @param fbId - facebook id 
     */
    @Override
    public void setProfilePictureTag(boolean isAnyn, String gender, String fbId) {
	Image profileImage = CommonUtils.getProfilePicture(new Confession(gender, fbId), isAnyn);
	profileImage.setStyleName("reg_image");
	fPnlTop.add(profileImage);
//	CommonUtils.parseXFBMLJS(profileImage.getElement());
    }

    @Override
    public boolean isShared() {
	return cbConfessTo.getValue();
    }

    @Override
    public HasClickHandlers getCbHideIdentity() {
	return cbHideIdentity;
    }

    @Override
    public HasClickHandlers getCbConfessTo() {
	return cbConfessTo;
    }

    @Override
    public CBTextBox getTxtTitle() {
	return txtTitle;
    }

    @Override
    public CBTextArea getTxtConfession() {
	return txtConfession;
    }

    @Override
    public CheckBox getCbHideIdentityWidget() {
	return cbHideIdentity;
    }

    @Override
    public CheckBox getCbConfessToWidget() {
	return cbConfessTo;
    }

    @Override
    public Button getBtnSave() {
        return btnSave;
    }

    @Override
    public Button getBtnDeleteDraft() {
        return btnDeleteDraft;
    }
    
    @Override
    public void enableDeleteDraftButton(boolean isVisible){
	btnDeleteDraft.setVisible(isVisible);
    }

    public RelationSuggestBox getRelationSuggestBox() {
        return relationSuggestBox;
    }
}
