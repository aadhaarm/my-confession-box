package com.l3.CB.client.util;

import com.l3.CB.client.ConfessionBox;
import com.l3.CB.client.event.UpdateHPEvent;

public class EventUtils {

    public static void raiseUpdateHPEvent(int pointsToBeAdded) {
	ConfessionBox.eventBus.fireEvent(new UpdateHPEvent(pointsToBeAdded));
    }
}
