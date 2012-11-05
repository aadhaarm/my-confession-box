package com.l3.CB.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.l3.CB.client.presenter.HumanPointPresenter;

public class HumanPointView extends Composite implements HumanPointPresenter.Display {

	private final Label points;
	
	public HumanPointView() {
		super();
		DecoratorPanel contentTableDecorator = new DecoratorPanel();
		initWidget(contentTableDecorator);
		
		Label humanPoint = new Label("Human Point:");
		humanPoint.addStyleName("humanPointLabel");
		points = new Label();
		points.addStyleName("humanPointLabel");
		
		FlowPanel fPnlHumanPoint = new FlowPanel();
		fPnlHumanPoint.setWidth("140px");
		fPnlHumanPoint.add(humanPoint);
		fPnlHumanPoint.add(points);
		
		contentTableDecorator.add(fPnlHumanPoint);
	}
	
	public void setPoint(int points) {
		this.points.setText(Integer.toString(points));
	}
	
	public Widget asWidget() {
		return this;
	}
}
