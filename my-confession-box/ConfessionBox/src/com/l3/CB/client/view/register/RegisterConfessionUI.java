package com.l3.CB.client.view.register;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.presenter.CopyOfRegisterConfessionPresenter;
import com.l3.CB.client.ui.widgets.Templates;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.client.view.misc.CBButton;
import com.l3.CB.client.view.misc.CBTextAreaUI;
import com.l3.CB.client.view.misc.CBTextBoxUI;
import com.l3.CB.client.view.misc.FriendsSuggestBoxUI;
import com.l3.CB.client.view.misc.RelationSuggestBoxUI;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.UserInfo;

public class RegisterConfessionUI extends Composite implements CopyOfRegisterConfessionPresenter.Display {

    private static final String CLASS_HIDE = "hide";

    private static final String CLASS_SHOW = "show";

    private static RegisterConfessionUIUiBinder uiBinder = GWT
	    .create(RegisterConfessionUIUiBinder.class);

    interface RegisterConfessionUIUiBinder extends
    UiBinder<Widget, RegisterConfessionUI> {
    }

    private boolean isFriendNotRegistered = true;

    @UiField
    CBButton btnHideIdentity;

    @UiField
    CBButton btnDedicateConfession;

    @UiField
    CBButton btnRequestPardon;

    @UiField
    FriendsSuggestBoxUI friendsSuggestBox;

    @UiField
    RelationSuggestBoxUI relationSuggestBox;

    @UiField
    Image profileImage;

    @UiField
    SpanElement htmlConfessionPreview;

    @UiField
    CBTextBoxUI txtTitle;

    @UiField
    CBTextAreaUI txtConfession;

    @UiField
    DivElement pOptions;

    @UiField
    Button btnProceed;

    @UiField
    Button btnEdit;

    @UiField
    Button btnSubmit;

    @UiField
    SpanElement spanUserMessage;

    @UiField
    SpanElement spanConfirmMessage;

    @UiField
    ParagraphElement pConfirmPanel;
    
    @UiField
    Button btnShowRules;
    
    @UiField
    HTMLPanel divRules;

    public RegisterConfessionUI() {
	initWidget(uiBinder.createAndBindUi(this));

	divRules.addStyleName("hide");
	
	txtConfession.setupWidget(Constants.CONF_MAX_CHARS, true, "Write your confession here..");

	btnDedicateConfession.getElement().setAttribute("data-uk-tooltip", "");
	btnEdit.getElement().setAttribute("data-uk-tooltip", "");
	btnHideIdentity.getElement().setAttribute("data-uk-tooltip", "");
	btnProceed.getElement().setAttribute("data-uk-tooltip", "");
	btnRequestPardon.getElement().setAttribute("data-uk-tooltip", "");
	btnSubmit.getElement().setAttribute("data-uk-tooltip", "");
	
	//Hide confirm panel
	pConfirmPanel.addClassName(CLASS_HIDE);
	
	// Set on Load values
	btnDedicateConfession.setValue(true);
	btnHideIdentity.setValue(false);
	btnRequestPardon.setValue(false);
	
	// Handle Preview
	handlePreview();

	bind();
    }

    private void bind() {
	friendsSuggestBox.getFriendsSuggestBox().addSelectionHandler(new SelectionHandler<MultiWordSuggestOracle.Suggestion>() {
	    @Override
	    public void onSelection(SelectionEvent<Suggestion> event) {
		handlePreview();
	    }
	});
	friendsSuggestBox.getFriendsSuggestBox().addValueChangeHandler(new ValueChangeHandler<String>() {
	    @Override
	    public void onValueChange(ValueChangeEvent<String> event) {
		handlePreview();		
	    }
	});
	friendsSuggestBox.getFriendsSuggestList().addChangeHandler(new ChangeHandler() {
	    @Override
	    public void onChange(ChangeEvent event) {
		handlePreview();
	    }
	});

	
	relationSuggestBox.getRelationSuggestBox().addSelectionHandler(new SelectionHandler<MultiWordSuggestOracle.Suggestion>() {
	    @Override
	    public void onSelection(SelectionEvent<Suggestion> event) {
		handlePreview();
	    }
	});
	relationSuggestBox.getRelationSuggestBox().addValueChangeHandler(new ValueChangeHandler<String>() {
	    @Override
	    public void onValueChange(ValueChangeEvent<String> event) {
		handlePreview();
	    }
	});
	relationSuggestBox.getRelationSuggestList().addChangeHandler(new ChangeHandler() {
	    @Override
	    public void onChange(ChangeEvent event) {
		handlePreview();
	    }
	});
    }

    public void disableForm() {
	txtConfession.disable();
	txtTitle.disable();
	btnDedicateConfession.setEnabled(false);
	btnHideIdentity.setEnabled(false);
	btnRequestPardon.setEnabled(false);
    }

    public void enableForm() {
	txtConfession.enable();
	txtTitle.enable();
	btnDedicateConfession.setEnabled(true);
	btnHideIdentity.setEnabled(true);
	btnRequestPardon.setEnabled(true);

    }

    @UiHandler("btnHideIdentity")
    void onHideIdentityClick(ClickEvent event) {
	// Refresh Display Image & Preview Confession Heading
	handlePreview();
    }

    @UiHandler("btnDedicateConfession")
    void onDedicateConfessionClick(ClickEvent event) {
	// Refresh Display Image & Preview Confession Heading
	handlePreview();

	if(btnDedicateConfession.getValue()) {
	    btnRequestPardon.setVisible(true);

	    pOptions.removeClassName(CLASS_HIDE);
	    pOptions.addClassName(CLASS_SHOW);
	} else {
	    btnRequestPardon.setVisible(false);

	    pOptions.removeClassName(CLASS_SHOW);
	    pOptions.addClassName(CLASS_HIDE);
	}
    }

    @UiHandler("btnRequestPardon")
    void onRequestPardonClick(ClickEvent event) {
	// Refresh Display Image & Preview Confession Heading
	handlePreview();
    }

    @UiHandler("btnProceed")
    void onBtnProceedClick(ClickEvent event) {
	boolean validateTitle = txtTitle.validate(); 
	boolean validateConfession = txtConfession.validate(Constants.CONF_MIN_CHARS, Constants.CONF_MAX_CHARS);
	boolean validateFriend = false;
	boolean validateRelation = false;

	if(btnDedicateConfession.getValue()) {
	    validateFriend = friendsSuggestBox.validate(); 
	    validateRelation = relationSuggestBox.validate(); 
	}
	
	if(validateTitle && validateConfession) {
	    if(btnDedicateConfession.getValue()) {
		if(validateFriend && validateRelation) {
		    proceedToConfirm();
		}
	    } else {
		proceedToConfirm();
	    }
	}
    }

    @UiHandler("btnEdit")
    void onBtnEditClick(ClickEvent event) {
	//Hide confirm panel
	pConfirmPanel.removeClassName(CLASS_SHOW);
	pConfirmPanel.addClassName(CLASS_HIDE);
	btnProceed.setVisible(true);
	// Enable Form
	enableForm();
    }

    @UiHandler("btnShowRules")
    void onShowRules(ClickEvent event) {
	divRules.removeStyleName("hide");
	divRules.addStyleName("show");
	btnShowRules.addStyleName("hide");
    }
    
    /**
     * Check is user registered - Show FB send dialog
     */
    public void proceedToConfirm() {
	final UserInfo confessedToUser = friendsSuggestBox.getSelectedUser();
	if(confessedToUser != null) {
	    ConfessionBox.confessionService.isUserRegistered(confessedToUser.getId(), new AsyncCallback<UserInfo>() {

		@Override
		public void onSuccess(UserInfo result) {
		    boolean isFriendNotRegistered = false;
		    if(result == null) {
			isFriendNotRegistered = true;
		    }
		    setupConfirmPanel(isFriendNotRegistered, confessedToUser.getName());
		}

		@Override
		public void onFailure(Throwable caught) {
		    Error.handleError("RegisterConfessionUtil", "onFailure", caught);
		}
	    });
	} else {
	    setupConfirmPanel(false, null);
	}
    }

    public void setupConfirmPanel(boolean isFriendNotRegistered, String pardonerName) {
	disableForm();

	this.isFriendNotRegistered = isFriendNotRegistered;

	if(isFriendNotRegistered && pardonerName != null) {
	    spanUserMessage.setInnerSafeHtml(Templates.TEMPLATES.confessorNotRegisteredMessage(pardonerName));
	}

	spanConfirmMessage.setInnerSafeHtml(Templates.TEMPLATES.confessionConfirmMessage(
		ConfessionBox.cbText.confirmMessageWhenSubmittingConfession()));

	btnProceed.setVisible(false);
	pConfirmPanel.removeClassName(CLASS_HIDE);
	pConfirmPanel.addClassName(CLASS_SHOW);
	btnEdit.setVisible(true);
	btnSubmit.setVisible(true);
    }


    /**
     * Make preview description and profile image
     * @param display - View object
     */
    private void handlePreview() {
	// Refresh Display Image
	profileImage.setUrl(CommonUtils.getProfilePictureURL(new Confession(ConfessionBox.getLoggedInUserInfo().getGender(), 
		ConfessionBox.getLoggedInUserInfo().getId()), 
		btnHideIdentity.getValue()));

	// Refresh Preview Confession Heading
	String confesee = ConfessionBox.cbText.confessedByAnynName();
	String confessor = ConfessionBox.cbText.confessedToWorld();

	if(btnHideIdentity.getValue()){
	    confesee = ConfessionBox.cbText.confessedByAnynName();
	} else {
	    confesee = ConfessionBox.getLoggedInUserInfo().getName();
	}

	if(btnDedicateConfession.getValue() && relationSuggestBox.getSelectedRelation() != null) {
	    confessor = relationSuggestBox.getSelectedRelation().getDisplayText();
	}

	if((btnDedicateConfession.getValue() || btnRequestPardon.getValue()) 
		&& relationSuggestBox.getSelectedRelation() != null) {
	    htmlConfessionPreview.setInnerSafeHtml(Templates.TEMPLATES.confessonPreview(confesee, confessor, 
			    CommonUtils.getPronoun(ConfessionBox.loggedInUserInfo.getGender())));
	} else {
	    htmlConfessionPreview.setInnerSafeHtml(Templates.TEMPLATES.confessonPreview(confesee, confessor, "the"));
	}
    }

    public CBButton getBtnHideIdentity() {
	return btnHideIdentity;
    }

    public CBButton getBtnDedicateConfession() {
	return btnDedicateConfession;
    }

    public CBButton getBtnRequestPardon() {
	return btnRequestPardon;
    }

    public FriendsSuggestBoxUI getFriendsSuggestBox() {
	return friendsSuggestBox;
    }

    public RelationSuggestBoxUI getRelationSuggestBox() {
	return relationSuggestBox;
    }

    public CBTextBoxUI getTxtTitle() {
	return txtTitle;
    }

    public CBTextAreaUI getTxtConfession() {
	return txtConfession;
    }

    public HasClickHandlers getBtnSubmit() {
	return btnSubmit;
    }

    public boolean isFriendNotRegistered() {
	return isFriendNotRegistered;
    }
}