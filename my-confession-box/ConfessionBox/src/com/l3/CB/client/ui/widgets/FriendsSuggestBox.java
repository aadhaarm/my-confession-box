package com.l3.CB.client.ui.widgets;

import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.UserInfo;

public class FriendsSuggestBox extends FlowPanel {

    private Map<String, UserInfo> userfriends = null;
    private MultiWordSuggestOracle friendsOracle;
    private final Label errMsg = new Label(ConfessionBox.cbText.shareConfessionSuggestBoxErrorMessage());
    private final Label label = new Label(ConfessionBox.cbText.friendsSuggestionBoxLabel());
    private SuggestBox friendsSuggestBox;
    private ListBox friendsSuggestList;
    private Image friendsImage;

    public FriendsSuggestBox(final Map<String, UserInfo> userfriends) {
	super();
	errMsg.setStyleName("errorMessage");

	this.userfriends = userfriends;

	this.setStyleName(Constants.DIV_FRIENDS_SUGGEST_BOX);

	if(Navigator.isJavaEnabled()) {
	    setupFriendSuggestBox(userfriends);
	} else {
	    setFriendSuggestList(userfriends);
	}
    }

    /**
     * @param userfriends
     */
    private void setFriendSuggestList(final Map<String, UserInfo> userfriends) {
	friendsSuggestList = new ListBox();
	if(userfriends != null && !userfriends.isEmpty()) {
	    friendsSuggestList.addItem("Choose person you are confessing to");
	    //iterating over keys only
	    for (String friendsName : userfriends.keySet()) {
		friendsSuggestList.addItem(friendsName);
	    }
	    this.add(label);
	    this.add(friendsSuggestList);

	    friendsSuggestList.addChangeHandler(new ChangeHandler() {

		@Override
		public void onChange(ChangeEvent event) {
		    validate();
		    ListBox lstBoxFilter = (ListBox)event.getSource();
		    if(lstBoxFilter != null) {
			String selectedFilter = lstBoxFilter.getValue(lstBoxFilter.getSelectedIndex());
			if(selectedFilter != null) {
			    final UserInfo friend = userfriends.get(selectedFilter);
			    if(friend !=null) {
				if(friendsImage != null) {
				    remove(friendsImage);
				}
				friendsImage = new Image(FacebookUtil.getUserImageUrl(friend.getId()));
				friendsImage.setStyleName(Constants.DIV_FRIENDS_SUGGEST_IMAGE);
				// Friends image as link
				friendsImage.addClickHandler(new ClickHandler() {

				    @Override
				    public void onClick(ClickEvent event) {
					Window.open(FacebookUtil.getProfileFBLink(friend.getId()).asString(), friend.getName(), "");
				    }
				});
				add(friendsImage);
			    } else {
				if(friendsImage != null) {
				    remove(friendsImage);
				}
			    }
			} else {
			    if(friendsImage != null) {
				remove(friendsImage);
			    }
			}

		    } else {
			if(friendsImage != null) {
			    remove(friendsImage);
			}
		    }
		}
	    });
	}
    }

    /**
     * @param userfriends
     */
    private void setupFriendSuggestBox(final Map<String, UserInfo> userfriends) {
	if(userfriends != null && !userfriends.isEmpty()) {
	    friendsOracle = new MultiWordSuggestOracle();
	    //iterating over keys only
	    for (String friendsName : userfriends.keySet()) {
		friendsOracle.add(friendsName);
	    }
	    friendsSuggestBox = new SuggestBox(friendsOracle);
	    this.add(label);
	    this.add(friendsSuggestBox);
	}
	friendsSuggestBox.setWidth("150px");
	friendsSuggestBox.addSelectionHandler(new SelectionHandler<MultiWordSuggestOracle.Suggestion>() {

	    @Override
	    public void onSelection(SelectionEvent<Suggestion> event) {
		validate();

		String selection = event.getSelectedItem().getReplacementString();
		if(selection != null && !"".equals(selection)) {
		    final UserInfo friend = userfriends.get(selection);
		    if(friend !=null) {
			if(friendsImage != null) {
			    remove(friendsImage);
			}
			friendsImage = new Image(FacebookUtil.getUserImageUrl(friend.getId()));
			friendsImage.setStyleName(Constants.DIV_FRIENDS_SUGGEST_IMAGE);
			// Friends image as link
			friendsImage.addClickHandler(new ClickHandler() {

			    @Override
			    public void onClick(ClickEvent event) {
				Window.open(FacebookUtil.getProfileFBLink(friend.getId()).asString(), friend.getName(), "");
			    }
			});
			add(friendsImage);
		    } else {
			if(friendsImage != null) {
			    remove(friendsImage);
			}
		    }
		} else {
		    if(friendsImage != null) {
			remove(friendsImage);
		    }
		}
	    }
	});

	friendsSuggestBox.addValueChangeHandler(new ValueChangeHandler<String>() {
	    @Override
	    public void onValueChange(ValueChangeEvent<String> event) {
		validate();
		String selection = event.getValue();
		if(selection != null && !"".equals(selection)) {
		    final UserInfo friend = userfriends.get(selection);
		    if(friend !=null) {
			if(friendsImage != null) {
			    remove(friendsImage);
			}
			friendsImage = new Image(FacebookUtil.getUserImageUrl(friend.getId()));
			friendsImage.setStyleName(Constants.DIV_FRIENDS_SUGGEST_IMAGE);
			// Friends image as link
			friendsImage.addClickHandler(new ClickHandler() {

			    @Override
			    public void onClick(ClickEvent event) {
				Window.open(FacebookUtil.getProfileFBLink(friend.getId()).asString(), friend.getName(), "");
			    }
			});
			add(friendsImage);
		    } else {
			if(friendsImage != null) {
			    remove(friendsImage);
			}
		    }
		} else {
		    if(friendsImage != null) {
			remove(friendsImage);
		    }
		}
	    }
	});
    }

    public UserInfo getSelectedUser() {
	if(friendsSuggestBox != null) {
	    return userfriends.get(friendsSuggestBox.getValue());
	} else if(friendsSuggestList != null) {
	    return userfriends.get(friendsSuggestList.getValue(friendsSuggestList.getSelectedIndex()));
	}
	return null;
    }

    public boolean validate() {
	if(friendsSuggestBox != null) {
	    if(userfriends.get(friendsSuggestBox.getValue()) == null) {
		this.add(errMsg);
		return false;
	    } 
	    this.remove(errMsg);
	} else if(friendsSuggestList != null) {
	    if(userfriends.get(friendsSuggestList.getValue(friendsSuggestList.getSelectedIndex())) == null) {
		this.add(errMsg);
		return false;
	    } 
	    this.remove(errMsg);
	}
	return true;
    }

    public void setFocus() {
	if(friendsSuggestBox != null) {
	    friendsSuggestBox.setFocus(true);
	} else if(friendsSuggestList != null) {
	    friendsSuggestList.setFocus(true);
	}
    }
}