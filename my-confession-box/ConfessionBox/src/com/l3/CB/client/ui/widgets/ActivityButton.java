package com.l3.CB.client.ui.widgets;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.ActivityEvent;
import com.l3.CB.client.event.CancelActivityEvent;
import com.l3.CB.client.event.ShowToolTipEvent;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.client.util.EventUtils;
import com.l3.CB.client.util.HelpInfo;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;

public class ActivityButton extends AbsolutePanel {

    final PushButton btn;
    Label lblTimerCount = null;
    Label lblTick = null;
    Image loader = null;
    Anchor ancShare = null;
    int timerCountNumber = 5;

    public ActivityButton(final Activity activity, final Confession confession, String titleText, String btnStyleName, final Image buttonImage) {
	super();
	this.setStyleName(Constants.DIV_ACTIVITY_BUTTON_CONTAINER);
	long count = getCount(confession, activity);

	btn = new PushButton(buttonImage);
	btn.addStyleName(btnStyleName);
	this.setTitle(titleText);
	final Label btnCount = new Label(getCountToDisplay(Long.toString(count)));

	getTimerWrapAnimation();

	ancShare = new Anchor(ConfessionBox.cbText.activityButtonShareButtonLabel());
	ancShare.setTitle("Share this vote on your Facebook wall.");
	ancShare.setStyleName(Constants.DIV_ACTIVITY_BUTTON_SHARE_BUTTON);


	if(ConfessionBox.isTouchEnabled && ConfessionBox.isMobile) {
	    ancShare.addStyleName(Constants.STYLE_CLASS_SHARE_LINK_TO_BTN);
	    ancShare.addTouchEndHandler(new TouchEndHandler() {
		@Override
		public void onTouchEnd(TouchEndEvent event) {
		    onSharePress(activity, confession, buttonImage);
		}
	    });
	} else {
	    ancShare.addClickHandler(new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
		    onSharePress(activity, confession, buttonImage);
		}

	    });
	}

	// Register activity with TIMER
	final Timer timer = getActivityTimer(activity, confession, btnCount);

	addVoteButtonClickEvent(timer);

	this.add(btn);
	btnCount.setStyleName(Constants.STYLE_CLASS_BUTTON_WRAPPER);
	this.add(btnCount, btn.getElement());

	lblTick = new Label("✔");
	lblTick.setStyleName("btnNameText");
	lblTick.setVisible(false);
	this.add(lblTick, btn.getElement());

	bind(activity, btn);
    }

    /**
     * @param activity
     * @param confession
     * @param buttonImage
     */
    private void onSharePress(final Activity activity,
	    final Confession confession, final Image buttonImage) {
	if(ConfessionBox.isLoggedIn) {
	    // POST ON WALL popup
	    CommonUtils.postOnWall(
		    FacebookUtil.getActivityUrl(confession.getConfId()),
		    getImageUrl(buttonImage.getUrl()),
		    getActivityTitle(activity),
		    getActivityCaption(confession),
		    getActivityDescription(activity),
		    activity.getActivitySharePoints());
	} else {
	    CommonUtils.login(0);
	}
    }

    private String getImageUrl(String url) {
	return Location.getHost() + url;
    }

    private String getActivityDescription(Activity activity) {
	return ConfessionBox.getLoggedInUserInfo().getFirst_name()
		+ ConfessionBox.cbText.activityButtonShareClickText1()
		+ Integer.toString(activity.getActivitySharePoints())
		+ ConfessionBox.cbText.activityButtonShareClickText2();
    }

    private String getActivityCaption(Confession confession) {
	return confession.getConfessionTitle();
    }

    private String getActivityTitle(Activity activity) {
	return ConfessionBox.getLoggedInUserInfo().getName() + " " + activity.getActivityAsVerb() + " to a confession.";
    }

    /**
     * @param timer
     */
    private void addVoteButtonClickEvent(final Timer timer) {
	if(ConfessionBox.isTouchEnabled) {
	    btn.addTouchEndHandler(new TouchEndHandler() {
		boolean inEvent = false;
		@Override
		public void onTouchEnd(TouchEndEvent event) {
		    if(ConfessionBox.isLoggedIn) {
			if(inEvent) {
			    inEvent = false;
			    timer.cancel();
			    lblTimerCount.setVisible(false);
			    loader.setVisible(false);
			} else {
			    inEvent = true;
			    lblTimerCount.setVisible(true);
			    loader.setVisible(true);
			    timer.scheduleRepeating(1000);
			    add(loader, btn.getElement());
			    add(lblTimerCount, btn.getElement());
			}
		    } else {
			CommonUtils.login(0);
		    }
		}
	    });
	} else {
	    btn.addClickHandler(new ClickHandler() {
		boolean inEvent = false;
		@Override
		public void onClick(ClickEvent event) {
		    if(ConfessionBox.isLoggedIn) {
			if(inEvent) {
			    inEvent = false;
			    timer.cancel();
			    lblTimerCount.setVisible(false);
			    loader.setVisible(false);
			} else {
			    inEvent = true;
			    lblTimerCount.setVisible(true);
			    loader.setVisible(true);
			    timer.scheduleRepeating(1000);
			    add(loader, btn.getElement());
			    add(lblTimerCount, btn.getElement());
			}
		    } else {
			CommonUtils.login(0);
		    }
		}
	    });

	}
    }

    /**
     * TIMER ANIMATION
     */
    private void getTimerWrapAnimation() {
	lblTimerCount = new Label(Integer.toString(timerCountNumber));
	lblTimerCount.setStyleName("timerCountWrap");
	loader = new Image(Constants.LOAD_ACTIVITY_TIMER_PROGRESS);
	loader.setStyleName("loaderWrap");
    }

    /**
     * @param activity
     * @param confession
     * @param userInfo
     * @param confessionService
     * @param btnCount
     * @return
     */
    private Timer getActivityTimer(final Activity activity, final Confession confession, final Label btnCount) {
	final Timer timer = new Timer() {
	    int count = ActivityButton.this.timerCountNumber-1;

	    @Override
	    public void cancel() {
		super.cancel();
		ConfessionBox.eventBus.fireEvent(new CancelActivityEvent(confession.getConfId()));
		count = ActivityButton.this.timerCountNumber-1;
		lblTimerCount.setText(Integer.toString(ActivityButton.this.timerCountNumber));
	    }

	    @Override
	    public void run() {
		ConfessionBox.eventBus.fireEvent(new ActivityEvent(confession.getConfId()));
		if(count > 0) {
		    lblTimerCount.setText(Integer.toString(count));
		    count--;
		} else {
		    cancel();
		    ConfessionBox.confessionService.registerUserActivity(ConfessionBox.getLoggedInUserInfo().getUserId(), confession.getConfId(), activity, new Date(), new AsyncCallback<Long>() {
			@Override
			public void onSuccess(Long result) {
			    lblTimerCount.setVisible(false);
			    loader.setVisible(false);
			    btnCount.setText(getCountToDisplay(result.toString()));

			    btn.setEnabled(false);
			    lblTick.setVisible(true);

			    ancShare.setVisible(true);
			    add(ancShare);
			    EventUtils.raiseUpdateHPEvent(activity.getActivityPoints());
			    ConfessionBox.eventBus.fireEvent(new ShowToolTipEvent(confession.getConfId()));
			}

			@Override
			public void onFailure(Throwable caught) {
			    btn.setEnabled(true);
			    Error.handleError("ActivityButton", "onFailure", caught);
			}
		    });
		}
	    }
	};
	return timer;
    }

    /**
     * Bind user events
     * 
     * @param activity
     * @param btn
     */
    private void bind(Activity activity, PushButton btn) {
	if(activity != null) {
	    switch (activity) {
	    case SAME_BOAT:
		btn.addMouseOverHandler(new MouseOverHandler() {
		    @Override
		    public void onMouseOver(MouseOverEvent event) {
			HelpInfo.showHelpInfo(HelpInfo.type.SAME_BOAT_BUTTON);
		    }
		});
		break;
	    case ABUSE:
		btn.addMouseOverHandler(new MouseOverHandler() {
		    @Override
		    public void onMouseOver(MouseOverEvent event) {
			HelpInfo.showHelpInfo(HelpInfo.type.ABUSE_BUTTON);
		    }
		});
		break;
	    case LAME:
		btn.addMouseOverHandler(new MouseOverHandler() {
		    @Override
		    public void onMouseOver(MouseOverEvent event) {
			HelpInfo.showHelpInfo(HelpInfo.type.LAME_BUTTON);
		    }
		});
		break;
	    case SHOULD_BE_PARDONED:
		btn.addMouseOverHandler(new MouseOverHandler() {
		    @Override
		    public void onMouseOver(MouseOverEvent event) {
			HelpInfo.showHelpInfo(HelpInfo.type.SHOULD_BE_PARDONED_BUTTON);
		    }
		});
		break;
	    case SHOULD_NOT_BE_PARDONED:
		btn.addMouseOverHandler(new MouseOverHandler() {
		    @Override
		    public void onMouseOver(MouseOverEvent event) {
			HelpInfo.showHelpInfo(HelpInfo.type.SHOULD_NOT_BE_PARDONED_BUTTON);
		    }
		});
		break;
	    case SYMPATHY:
		btn.addMouseOverHandler(new MouseOverHandler() {
		    @Override
		    public void onMouseOver(MouseOverEvent event) {
			HelpInfo.showHelpInfo(HelpInfo.type.SYMPATHY_BUTTON);
		    }
		});
		break;
	    default:
		break;
	    }
	}
    }

    /**
     * Get count of votes
     * 
     * @param confession
     * @param activity
     * @return
     */
    private long getCount(Confession confession, Activity activity) {
	long count = 0;
	if(confession != null && activity != null) {
	    switch (activity) {
	    case ABUSE:
		count = confession.getNumOfAbuseVote();
		break;
	    case LAME:
		count = confession.getNumOfLameVote();
		break;
	    case SAME_BOAT:
		count = confession.getNumOfSameBoatVote();
		break;
	    case SHOULD_BE_PARDONED:
		count = confession.getNumOfShouldBePardonedVote();
		break;
	    case SHOULD_NOT_BE_PARDONED:
		count = confession.getNumOfShouldNotBePardonedVote();
		break;
	    case SYMPATHY:
		count = confession.getNumOfSympathyVote();
		break;
	    }
	}
	return count;
    }

    /**
     * Get counts in ago format
     * @param count
     * @return
     */
    private String getCountToDisplay(String count) {
	if(count == null) {
	    return "";
	}
	if(count != null && count.length() == 4) {
	    count = count.substring(0, 0) + "k";
	} else if(count != null && count.length() == 5) {
	    count = count.substring(0, 1) + "k";
	} else if(count != null && count.length() > 5) {
	    count = "⬆" + count.substring(0, 2) + "k";
	}
	return count;
    }

    /**
     * Disable activity button
     * Enable the Share button
     */
    public void disableBtn() {
	btn.setEnabled(false);
	ancShare.setVisible(true);
	lblTick.setVisible(true);
	add(ancShare);
    }
}