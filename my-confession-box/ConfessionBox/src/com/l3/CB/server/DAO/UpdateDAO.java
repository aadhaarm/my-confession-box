package com.l3.CB.server.DAO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Text;
import com.l3.CB.server.DO.ConfessionUpdateDO;
import com.l3.CB.shared.TO.ConfessionUpdate;

public class UpdateDAO {

    static Logger logger = Logger.getLogger("CBLogger");

    public static List<ConfessionUpdate> getConfessionUpdates(Long confId) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	List<ConfessionUpdate> confessionUpdates = null;
	    Query query = pm.newQuery(ConfessionUpdateDO.class);
	    query.setFilter("confId == id");
	    query.declareParameters("String id");
	    query.setOrdering("timeStamp desc");
	    @SuppressWarnings("unchecked")
	    List<ConfessionUpdateDO> result = (List<ConfessionUpdateDO>) query.execute(confId);

	    if (result != null && !result.isEmpty()) {
		confessionUpdates = new ArrayList<ConfessionUpdate>();
		Iterator<ConfessionUpdateDO> it = result.iterator();
		while (it.hasNext()) {
		    ConfessionUpdateDO confessionUpdateDO = it.next();
		    ConfessionUpdate confUpdate = getConfessionUpdate(confessionUpdateDO);
		    if(confUpdate != null) {
			confessionUpdates.add(confUpdate);
		    }
		}
	    }
	return confessionUpdates;
    }
    
    
    /**
     * Persists confession update
     * 
     * @param confessionUpdate
     */
    public static void saveUdate(ConfessionUpdate confessionUpdate) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	try {
	    ConfessionUpdateDO confessionUpdatesDO = getConfessionUpdateDO(confessionUpdate);
	    	    pm.makePersistent(confessionUpdatesDO);
	} catch (Exception e) {
	    logger.log(Level.SEVERE, "Error while registering confession update:" + e.getMessage());
	} finally {
	    pm.close();
	}
    }

    private static ConfessionUpdateDO getConfessionUpdateDO(ConfessionUpdate confessionUpdate) {
	ConfessionUpdateDO confessionUpdateDO = null;
	if(confessionUpdate != null) {
	    confessionUpdateDO = new ConfessionUpdateDO(
		    confessionUpdate.getConfId(),
		    confessionUpdate.getTimeStamp(),
		    new Text(confessionUpdate.getUpdate()),
		    confessionUpdate.getCommentAs(),
		    confessionUpdate.getUserId());
	}
	return confessionUpdateDO;
    }

    private static ConfessionUpdate getConfessionUpdate(ConfessionUpdateDO confessionUpdateDO) {
 	ConfessionUpdate confessionUpdate = null;
 	if(confessionUpdateDO != null) {
 	    confessionUpdate = new ConfessionUpdate(
 		    confessionUpdateDO.getConfId(),
 		    confessionUpdateDO.getTimeStamp(),
 		    confessionUpdateDO.getUpdate().getValue(),
 		    confessionUpdateDO.getCommentAs(),
 		    confessionUpdateDO.getUserId());
 	}
 	return confessionUpdate;
     }
}