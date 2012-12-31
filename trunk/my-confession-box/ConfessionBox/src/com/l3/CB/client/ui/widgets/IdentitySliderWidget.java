package com.l3.CB.client.ui.widgets;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.kiouri.sliderbar.client.solution.adv.MAdvancedPanel;
import com.kiouri.sliderbar.client.solution.adv.MAdvancedTextPanel;
import com.kiouri.sliderbar.client.view.SliderBarHorizontal;

public class IdentitySliderWidget extends SliderBarHorizontal{

    public IdentitySliderWidget() {
	Widget drag = new MAdvancedPanel();
	drag.setPixelSize(10, 10);
	
	Widget less = new MAdvancedTextPanel(new Image("/images/hidden.png"), new Image("/images/hidden.png"));
	
	Widget more = new MAdvancedTextPanel(new Image("/images/open.png"), new Image("/images/open.png"));
	
	this.setLessWidget(less);
	this.setScaleWidget(new Image("/images/scale.png"), 2);
	this.setMoreWidget(more);		
	this.setDragWidget(drag);
	this.setMaxValue(1);
	this.setWidth("230px");		 
    }
}