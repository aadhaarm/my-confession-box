package com.l3.CB.server.manager;

import java.util.List;

import com.l3.CB.server.DAO.UpdateDAO;
import com.l3.CB.shared.TO.ConfessionUpdate;

public class UpdateManager {

    public static void registerConfessionUpdate(ConfessionUpdate confessionUpdate) {
	UpdateDAO.saveUdate(confessionUpdate);	
    }
 
    public static List<ConfessionUpdate> getConfessionUpdates(Long confId) {
	return UpdateDAO.getConfessionUpdates(confId);
    }
}