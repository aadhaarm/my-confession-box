package com.l3.CB.server.manager;

import java.util.Date;
import java.util.List;

import com.l3.CB.server.DAO.CommentDAO;
import com.l3.CB.server.DAO.ConfessionBasicDAO;
import com.l3.CB.shared.CommentFilter;
import com.l3.CB.shared.TO.Comment;
import com.l3.CB.shared.TO.Confession;
import com.l3.CB.shared.TO.PardonCondition;
import com.l3.CB.shared.TO.UserInfo;

public class CommentManager {

    public static void saveComment(Comment comment) {
	CommentDAO.saveComment(comment);

	Confession confession = ConfessionBasicDAO.getConfession(comment.getConfId());
	if(confession != null) {
	    // Get the confession by user details
	    UserInfo confessionByUser = UserManager.getUserByUserId(confession.getUserId());

	    // Get confessed to user
	    UserInfo confessionToUser = null;
	    long pardonByUserId = 0;
	    // Fetch pardon conditions
	    List<PardonCondition> pardonConditions = ConfessionBasicDAO.getConfessionCondition(comment.getConfId());
	    if(pardonConditions != null && !pardonConditions.isEmpty()) {
		for (PardonCondition pardonCondition : pardonConditions) {
		    pardonByUserId = pardonCondition.getUserId();
		    break;
		}
		// Get the confession To user details
		confessionToUser = UserManager.getUserByUserId(pardonByUserId);
	    }

	    // Mark confession as updated
	    ConfessionManager.updateConfessionTimeStamp(comment.getConfId(), new Date());

	    MailManager.sendCommentReceivedEmail(confessionToUser, confessionByUser, comment.getConfId());

	}
    }

    public static CommentFilter getComments(CommentFilter filter) {
	return CommentDAO.getComments(filter);
    }

    public static void vote(CommentFilter filter) {
	CommentDAO.vote(filter);
    }
}