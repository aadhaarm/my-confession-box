package com.l3.CB.client.ui.widgets;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.util.CommonUtils;
import com.l3.CB.client.util.Error;
import com.l3.CB.client.util.EventUtils;
import com.l3.CB.client.util.HelpInfo;
import com.l3.CB.shared.Constants;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Activity;
import com.l3.CB.shared.TO.Confession;

public class ActivityButton extends AbsolutePanel {

    Logger logger = Logger.getLogger("CBLogger");
    final PushButton btn;
    Label timerCount = null;
    Image loader = null;
    Button btnShare = null;
    int count = 5;

    public ActivityButton(final Activity activity, final Confession confession, String titleText, String btnStyleName, final Image buttonImage) {
	super();
	this.addStyleName("activityButtonContainer");
	long count = getCount(confession, activity);

	btn = new PushButton(buttonImage);
	btn.addStyleName(btnStyleName);
	this.setTitle(titleText);
	final Label btnCount = new Label(getCountToDisplay(Long.toString(count)));

	getTimerWrapAnimation();

	btnShare = new Button("share"); 
	btnShare.setStyleName("shareWrap");
	btnShare.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		CommonUtils.postOnWall(FacebookUtil.getActivityUrl(confession.getConfId()), getImageUrl(buttonImage.getUrl()), getActivityTitle(activity), getActivityCaption(confession), getActivityDescription(activity), activity.getActivitySharePoints());
	    }
	});

	final Timer timer = getActivityTimer(activity, confession, btnCount);

	addVoteButtonClickEvent(timer);

	this.add(btn);
	btnCount.setStyleName(Constants.STYLE_CLASS_BUTTON_WRAPPER);
	this.add(btnCount, btn.getElement());
	bind(activity, btn);
    }

    private String getImageUrl(String url) {
	return Location.getHost() + url;
    }

    private String getActivityDescription(Activity activity) {
	return ConfessionBox.loggedInUserInfo.getName() + " has been given " + Integer.toString(activity.getActivitySharePoints()) + " 'Human Points'.";
    }

    private String getActivityCaption(Confession confession) {
	return confession.getConfessionTitle();
    }

    private String getActivityTitle(Activity activity) {
	return ConfessionBox.loggedInUserInfo.getName() + " has " + activity.getActivityAsVerb();
    }

    /**
     * @param timer
     */
    private void addVoteButtonClickEvent(final Timer timer) {
	btn.addClickHandler(new ClickHandler() {
	    boolean inEvent = false;
	    @Override
	    public void onClick(ClickEvent event) {
		if(inEvent) {
		    inEvent = false;
		    timer.cancel();
		    timerCount.setVisible(false);
		    loader.setVisible(false);
		} else {
		    inEvent = true;
		    timerCount.setVisible(true);
		    loader.setVisible(true);
		    timer.scheduleRepeating(1000);
		    add(loader, btn.getElement());
		    add(timerCount, btn.getElement());
		}
	    }
	});
    }

    /**
     * TIMER ANIMATION
     */
    private void getTimerWrapAnimation() {
	timerCount = new Label(Integer.toString(count));
	timerCount.setStyleName("timerCountWrap");
	loader = new Image("/images/loader_progress.gif");
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
	    int count = ActivityButton.this.count-1;

	    @Override
	    public void cancel() {
		super.cancel();
		count = ActivityButton.this.count-1;
		timerCount.setText(Integer.toString(ActivityButton.this.count));
	    }

	    @Override
	    public void run() {
		if(count > 0) {
		    timerCount.setText(Integer.toString(count));
		    count--;
		} else {
		    cancel();
		    ConfessionBox.confessionService.registerUserActivity(ConfessionBox.loggedInUserInfo.getUserId(), confession.getConfId(), activity, new AsyncCallback<Long>() {
			@Override
			public void onSuccess(Long result) {
			    timerCount.setVisible(false);
			    loader.setVisible(false);
			    btnCount.setText(getCountToDisplay(result.toString()));

			    btn.setEnabled(false);

			    btnShare.setVisible(true);
			    add(btnShare, btn.getElement());
			    EventUtils.raiseUpdateHPEvent(activity.getActivityPoints());
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

    private String getCountToDisplay(String count) {
	if(count == null) {
	    return "";
	}
	if(count != null && count.length() == 4) {
	    count = count.substring(0, 0) + "k";
	} else if(count != null && count.length() == 5) {
	    count = count.substring(0, 1) + "k";
	} else if(count != null && count.length() > 5) {
	    count = "☝" + count.substring(0, 2) + "k";
	}
	return count;
    }

    public void disableBtn() {
	btn.setEnabled(false);
	btnShare.setVisible(true);
	add(btnShare, btn.getElement());
    }
}