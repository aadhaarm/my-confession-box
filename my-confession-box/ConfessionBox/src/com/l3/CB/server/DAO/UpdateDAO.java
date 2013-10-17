package com.l3.CB.server.DAO;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.ObjectifyService;
import com.l3.CB.server.DO.ConfessionUpdateDO;
import com.l3.CB.shared.TO.ConfessionUpdate;

public class UpdateDAO {

    static Logger logger = Logger.getLogger("CBLogger");

    static {
	ObjectifyService.register(ConfessionUpdateDO.class);
    }

    public static List<ConfessionUpdate> getConfessionUpdates(Long confId) {

	List<ConfessionUpdate> confessionUpdates = new ArrayList<ConfessionUpdate>();

	com.googlecode.objectify.cmd.Query<ConfessionUpdateDO> queryOfy = ofy()
		.load().type(ConfessionUpdateDO.class).filter("confId", confId)
		.order("timeStamp");

	for (ConfessionUpdateDO confessionUpdateDO : queryOfy) {
	    ConfessionUpdate confUpdate = getConfessionUpdate(confessionUpdateDO);
	    if(confUpdate != null) {
		confessionUpdates.add(confUpdate);
	    }
	}

	return confessionUpdates;

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	    Query query = pm.newQuery(ConfessionUpdateDO.class);
	//	    query.setFilter("confId == id");
	//	    query.declareParameters("String id");
	//	    query.setOrdering("timeStamp asc");
	//	    @SuppressWarnings("unchecked")
	//	    List<ConfessionUpdateDO> result = (List<ConfessionUpdateDO>) query.execute(confId);
	//
	//	    if (result != null && !result.isEmpty()) {
	//		confessionUpdates = new ArrayList<ConfessionUpdate>();
	//		Iterator<ConfessionUpdateDO> it = result.iterator();
	//		while (it.hasNext()) {
	//		    ConfessionUpdateDO confessionUpdateDO = it.next();
	//		    ConfessionUpdate confUpdate = getConfessionUpdate(confessionUpdateDO);
	//		    if(confUpdate != null) {
	//			confessionUpdates.add(confUpdate);
	//		    }
	//		}
	//	    }
    }


    /**
     * Persists confession update
     * 
     * @param confessionUpdate
     */
    public static void saveUdate(ConfessionUpdate confessionUpdate) {

	ConfessionUpdateDO confessionUpdatesDO = getConfessionUpdateDO(confessionUpdate);
	ofy().save().entities(confessionUpdatesDO).now();

	//	PersistenceManager pm = PMF.get().getPersistenceManager();
	//	try {
	//	    ConfessionUpdateDO confessionUpdatesDO = getConfessionUpdateDO(confessionUpdate);
	//	    pm.makePersistent(confessionUpdatesDO);
	//	} catch (Exception e) {
	//	    logger.log(Level.SEVERE, "Error while registering confession update:" + e.getMessage());
	//	} finally {
	//	    pm.close();
	//	}
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