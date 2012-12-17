package com.l3.CB.client.ui.widgets;

import com.google.gwt.user.client.ui.Image;
import com.kiouri.sliderbar.client.view.SliderBarHorizontal;

public class HorizontalSliderWidget extends SliderBarHorizontal{

    public HorizontalSliderWidget(int maxValue, String width) {
	setLessWidget(new Image("/images/kdehless.png"));
	setMoreWidget(new Image("/images/kdehmore.png"));
	setScaleWidget(new Image("/images/kdehscale.png"), 25);
	setDragWidget(new Image("/images/kdehdrag.png"));
	this.setWidth(width);
	this.setMaxValue(maxValue);         
    }
}