package com.l3.CB.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class UpdateHPEvent extends GwtEvent<UpdateHPEventHandler> {
	public static Type<UpdateHPEventHandler> TYPE = new Type<UpdateHPEventHandler>();

	int pointsToBeAdded = 0;
	
	public UpdateHPEvent(int pointsToBeAdded) {
		super();
		this.pointsToBeAdded = pointsToBeAdded;
	}

	@Override
	public Type<UpdateHPEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(UpdateHPEventHandler handler) {
		handler.updateHPContact(this);
	}

	public int getUpdatedCount() {
		return pointsToBeAdded;
	}

	public void setUpdatedCount(int updatedCount) {
		this.pointsToBeAdded = updatedCount;
	}
}
