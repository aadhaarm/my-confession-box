package com.l3.CB.client.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;
import com.kiouri.sliderbar.client.event.BarValueChangedEvent;
import com.kiouri.sliderbar.client.event.BarValueChangedHandler;
import com.kiouri.sliderbar.client.solution.iph.IpSliderBar146;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.ui.widgets.CBTextArea;
import com.l3.CB.client.ui.widgets.CBTextBox;
import com.l3.CB.client.ui.widgets.FriendsSuggestBox;
import com.l3.CB.client.ui.widgets.RelationSuggestBox;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.client.util.HelpInfo;
import com.l3.CB.client.util.RegisterConfessionUtil;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.ConfessionShare;
import com.l3.CB.shared.TO.UserInfo;

public class RegisterConfessionPresenter implements Presenter {

    public interface Display {

	public boolean isIdentityHidden();
	public boolean isShared();

	public boolean isFriendsListNull();
	public HTML getHtmlConfessionPreview();
	public RelationSuggestBox getRelationSuggestBox();

	public void setFriends(Map<String, UserInfo> userfriends);
	public void setProfilePictureTag(boolean isAnyn, String gender, String fbId);

	/*
	 * Input widgets
	 */
	public CBTextBox getTxtTitle();
	public CBTextArea getTxtConfession();
	public UserInfo getSharedWith();
	public CheckBox getCbHideIdentityCheckBox();
	public CheckBox getCbConfessToCheckBox();

	public IpSliderBar146 getShareToSlider();
	public IpSliderBar146 getIdentitySlideBar();
	public HasClickHandlers getCbHideIdentity();
	public HasClickHandlers getCbConfessTo();

	public HasClickHandlers getSubmitBtn();
	public FriendsSuggestBox getFriendSuggestBox();

	/*
	 * Draft
	 */
	public String getConfession();
	public String getConfessionTitle();
	public Button getBtnSave();

	public void setFriendsTrans();

	Widget asWidget();
    }

    private final Display display;
    private Map<String, UserInfo> userfriends;

    /**
     * Constructor
     * @param display
     */
    public RegisterConfessionPresenter(final Display display) {
	super();
	this.display = display;
	display.setProfilePictureTag(true, ConfessionBox.getLoggedInUserInfo().getGender(), ConfessionBox.getLoggedInUserInfo().getId());
	checkForDraft();
	bind();
    }

    /**
     * Check for draft on-load
     */
    private void checkForDraft() {
	ConfessionBox.confessionService.getConfessionDraft(ConfessionBox.getLoggedInUserInfo().getUserId(), ConfessionBox.getLoggedInUserInfo().getId(), new AsyncCallback<Confession>() {
	    @Override
	    public void onSuccess(Confession result) {
		if(result != null) {
		    display.getTxtTitle().getTxtTitle().setText(result.getConfessionTitle());
		    display.getTxtConfession().getCbTextArea().setHTML(result.getConfession());
		    if(display.getCbHideIdentityCheckBox() != null) {
			display.getCbHideIdentityCheckBox().setValue(result.isShareAsAnyn(), true);
		    } else {
			display.getIdentitySlideBar().setValue(result.isShareAsAnyn()? 0 : display.getIdentitySlideBar().getMaxValue());
		    }

		    if(CommonUtils.isNotNullAndNotEmpty(result.getShareToUserIDForSave())) {
			if(display.getCbConfessToCheckBox() != null) {
			    display.getCbConfessToCheckBox().setValue(true, true);
			} else {
			    display.getShareToSlider().setValue(display.getShareToSlider().getMaxValue());
			}
			getMyFriends(result.getShareToUserIDForSave());
			if(CommonUtils.isNotNullAndNotEmpty(result.getShareToRelationForSave())) {
			    display.getRelationSuggestBox().getRelationSuggestBox().setValue(result.getShareToRelationForSave());
			    display.getRelationSuggestBox().getRelationSuggestBox().setVisible(true);
			}
		    }
		    RegisterConfessionUtil.handlePreview(display);
		    display.setProfilePictureTag(display
			    .isIdentityHidden(), ConfessionBox
			    .getLoggedInUserInfo().getGender(),
			    ConfessionBox.getLoggedInUserInfo().getId());
		}
	    }

	    @Override
	    public void onFailure(Throwable caught) {
		Error.handleError("RegisterConfessionPresenter", "onFailure", caught);
	    }
	});
    }

    /**
     * @param facebookService
     * @param display
     * @param accessToken
     */
    private void getMyFriends(final String preselectedFriend) {
	userfriends = CommonUtils.friendsMap;
	if(userfriends != null && !userfriends.isEmpty()) {
	    display.setFriends(userfriends);
	    if(preselectedFriend != null) {
		display.getFriendSuggestBox().setSelectedUser(preselectedFriend);	
	    }
	} else {
	    display.setFriendsTrans();
	    ConfessionBox.facebookService.getFriends(ConfessionBox.accessToken, new AsyncCallback<String>() {
		@Override
		public void onSuccess(String result) {
		    if(result != null) {
			userfriends = CommonUtils.getFriendsUserInfo(result);
			display.setFriends(userfriends);
			if(preselectedFriend != null) {
			    display.getFriendSuggestBox().setSelectedUser(preselectedFriend);
			}
		    }
		}
		@Override
		public void onFailure(Throwable caught) {
		    Error.handleError("RegisterConfessionPresenter", "onFailure", caught);
		}
	    });
	}
    }

    /**
     * Bind events
     */
    private void bind() {
	/*
	 * Bind Confession Submit 
	 */
	onConfessionSubmit();

	/*
	 * RelationSuggestBox - Add Selection Handler
	 * CbHideIdentity - AddClickHandler
	 * CbConfessTo - AddClickHandler
	 */
	bindOptionSelection();

	/*
	 * TxtTitle - AddBlurHandler
	 * CbTextArea - AddBlurHandler
	 */
	bindTextFields();

	/*
	 * BtnSave - AddClickHandler
	 */
	bindSaveDraft();
    }

    /**
     * BtnSave - AddClickHandler
     */
    private void bindSaveDraft() {
	display.getBtnSave().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if(display.getTxtTitle().validate() && display.getTxtConfession().validate(Constants.CONF_MIN_CHARS, Constants.CONF_MAX_CHARS)) {
		    display.getBtnSave().setText("Saving");
		    // Get confession to be saved
		    final Confession confession = new Confession();
		    confession.setConfessionTitle(display.getConfessionTitle());
		    confession.setConfession(display.getConfession());
		    confession.setUserId(ConfessionBox.getLoggedInUserInfo().getUserId());
		    confession.setShareAsAnyn(display.isIdentityHidden());
		    if(display.isShared() && display.getSharedWith() != null && CommonUtils.isNotNullAndNotEmpty(display.getSharedWith().getName())) {
			confession.setShareToUserIDForSave(display.getSharedWith().getName());
		    }
		    if(display.isShared() && display.getRelationSuggestBox() != null && display.getRelationSuggestBox().getSelectedRelation() != null) {
			confession.setShareToRelationForSave(display.getRelationSuggestBox().getSelectedRelation().getDisplayText());
		    }

		    ConfessionBox.confessionService.registerConfessionDraft(confession, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
			    display.getBtnSave().setText(ConfessionBox.cbText.saveDraftButtonText());
			    Timer t = new Timer() {
				@Override
				public void run() {
				    display.getBtnSave().setText(ConfessionBox.cbText.saveAsDraftButtonLabelText());
				}
			    };
			    t.schedule(3000);
			}

			@Override
			public void onFailure(Throwable caught) {
			    Error.handleError("RegisterConfessionPresenter",
				    "onFailure", caught);
			}
		    });
		}
	    }
	});
    }

    /**
     * TxtTitle - AddBlurHandler
     * CbTextArea - AddBlurHandler
     */
    private void bindTextFields() {
	display.getTxtTitle().getTxtTitle().addBlurHandler(new BlurHandler() {
	    @Override
	    public void onBlur(BlurEvent event) {
		display.getTxtTitle().validate();
	    }
	});

	display.getTxtConfession().getCbTextArea().addBlurHandler(new BlurHandler() {
	    @Override
	    public void onBlur(BlurEvent event) {
		display.getTxtConfession().validate(Constants.CONF_MIN_CHARS, Constants.CONF_MAX_CHARS);
	    }
	});
    }

    /**
     * RelationSuggestBox - Add Selection Handler
     * CbHideIdentity - AddClickHandler
     * CbConfessTo - AddClickHandler
     */
    private void bindOptionSelection() {

	/**
	 * Add Relationship suggest box selection handler
	 */
	if(display.getRelationSuggestBox().getRelationSuggestBox() != null) {
	    display.getRelationSuggestBox().getRelationSuggestBox().addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
		@Override
		public void onSelection(SelectionEvent<Suggestion> event) {
		    RegisterConfessionUtil.handlePreview(display);;
		}
	    });
	}

	/**
	 * Add Relationship Suggest list change handler
	 */
	if(display.getRelationSuggestBox().getRelationSuggestList() != null) {
	    display.getRelationSuggestBox().getRelationSuggestList().addChangeHandler(new ChangeHandler() {
		@Override
		public void onChange(ChangeEvent event) {
		    RegisterConfessionUtil.handlePreview(display);;
		}
	    });
	}

	/**
	 * If TOUCH enabled device
	 */
	if(ConfessionBox.isTouchEnabled) {
	    /*
	     * Handle Identity share slider value change
	     */
	    display.getIdentitySlideBar().addBarValueChangedHandler(new BarValueChangedHandler() {
		@Override
		public void onBarValueChanged(BarValueChangedEvent event) {
		    RegisterConfessionUtil.handlePreview(display);;
		    display.setProfilePictureTag(display
			    .isIdentityHidden(), ConfessionBox
			    .getLoggedInUserInfo().getGender(),
			    ConfessionBox.getLoggedInUserInfo().getId());
		    if(!ConfessionBox.isMobile) {
			HelpInfo.showHelpInfo(HelpInfo.type.REGISTER_CONF_HIDE_ID_CHECKBOX);
		    }
		}
	    });
	    /*
	     *	Handle Request pardon slider value change
	     */
	    display.getShareToSlider().addBarValueChangedHandler(new BarValueChangedHandler() {
		@Override
		public void onBarValueChanged(BarValueChangedEvent event) {
		    RegisterConfessionUtil.handlePreview(display);
		    if(!ConfessionBox.isMobile) {
			HelpInfo.showHelpInfo(HelpInfo.type.REGISTER_CONF_SHARE_WITH_CHECKBOX);
		    }
		    if(display.isShared()) {
			if(display.isFriendsListNull()) {
			    getMyFriends(null);
			}  
			if(display.getFriendSuggestBox() != null) {
			    display.getFriendSuggestBox().setVisible(true);
			    display.getRelationSuggestBox().setVisible(true);
			}
		    } else {
			if(display.getFriendSuggestBox() != null) {
			    display.getFriendSuggestBox().setVisible(false);
			    display.getRelationSuggestBox().setVisible(false);
			}
		    }
		}
	    });
	} else {
	    /*
	     * If TOUCH UN-AVAILABLE
	     */
	    display.getCbHideIdentity().addClickHandler(new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
		    RegisterConfessionUtil.handlePreview(display);;
		    display.setProfilePictureTag(display.isIdentityHidden(), ConfessionBox.getLoggedInUserInfo().getGender(), ConfessionBox.getLoggedInUserInfo().getId());
		    HelpInfo.showHelpInfo(HelpInfo.type.REGISTER_CONF_HIDE_ID_CHECKBOX);
		}
	    });

	    display.getCbConfessTo().addClickHandler(new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
		    RegisterConfessionUtil.handlePreview(display);;
		    HelpInfo.showHelpInfo(HelpInfo.type.REGISTER_CONF_SHARE_WITH_CHECKBOX);
		    if(display.isShared()) {
			if(display.isFriendsListNull()) {
			    getMyFriends(null);
			}
			if(display.getFriendSuggestBox() != null) {
			    display.getFriendSuggestBox().setVisible(true);
			    display.getRelationSuggestBox().setVisible(true);
			}
		    } else {
			if(display.getFriendSuggestBox() != null) {
			    display.getFriendSuggestBox().setVisible(false);
			    display.getRelationSuggestBox().setVisible(false);
			}
		    }
		}
	    });
	}
    }

    /**
     * Bind Confession Submit 
     */
    private void onConfessionSubmit() {
	// On Submit confession
	this.display.getSubmitBtn().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {

		if(display.getTxtTitle().validate() && display.getTxtConfession().validate(Constants.CONF_MIN_CHARS, Constants.CONF_MAX_CHARS)) {

		    // Get confession to be saved
		    final Confession confession = RegisterConfessionUtil.getConfessionToBeSaved(display);

		    if(confession != null) {

			//Register Shared-To info
			if(display.isShared()) {

			    final List<ConfessionShare> lstConfessTo = new ArrayList<ConfessionShare>();
			    UserInfo fbSharedUser = display.getSharedWith();
			    if(fbSharedUser != null && display.getRelationSuggestBox().validate()) {

				// Check if the user confessed to is a member and send a fb notification message
				RegisterConfessionUtil.checkIfUserRegistered(confession, display);

				final ConfessionShare confessTo = RegisterConfessionUtil.getConfessedShareTO(fbSharedUser, display);
				if(confessTo != null) {
				    lstConfessTo.add(confessTo);
				    confession.setConfessedTo(lstConfessTo);
				    //Finally register confession
				    RegisterConfessionUtil.finallyRegisterConfession(confession, display);
				}
			    }
			} else  {
			    //Finally register confession
			    RegisterConfessionUtil.finallyRegisterConfession(confession, display);
			}
		    }
		}
	    }
	});
    }

    /**
     * Kick start Presenter
     */
    @Override
    public void go(HasWidgets container) {
	RootPanel.get(Constants.DIV_MAIN_CONTENT).clear();
	RootPanel.get(Constants.DIV_MAIN_CONTENT).add(display.asWidget());		
    }
}