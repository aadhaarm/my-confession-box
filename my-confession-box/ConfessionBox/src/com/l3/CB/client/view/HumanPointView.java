package com.l3.CB.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.presenter.HumanPointPresenter;

public class HumanPointView extends Composite implements HumanPointPresenter.Display {

    private final Label points;

    public HumanPointView() {
	super();
	DecoratorPanel contentTableDecorator = new DecoratorPanel();
	initWidget(contentTableDecorator);

	Image imgHumanPoint = new Image("images/human-points.jpg");
	imgHumanPoint.setStyleName("humanPointImg");

	Label humanPoint = new Label(ConfessionBox.cbText.humanPointWidgetLabelText());
	humanPoint.setStyleName("humanPointLabel");
	
	points = new Label();
	points.setStyleName("humanPointLabel");

	FlowPanel fPnlHumanPoint = new FlowPanel();
	fPnlHumanPoint.setWidth("140px");
	fPnlHumanPoint.add(imgHumanPoint);
	fPnlHumanPoint.add(points);
	fPnlHumanPoint.add(humanPoint);

	contentTableDecorator.removeStyleName("gwt-DecoratorPanel");
	contentTableDecorator.add(fPnlHumanPoint);
    }

    @Override
    public void setPoint(int points) {
	this.points.setText(Integer.toString(points));
    }

    @Override
    public Widget asWidget() {
	return this;
    }
}
