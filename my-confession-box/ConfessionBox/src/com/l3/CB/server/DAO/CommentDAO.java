package com.l3.CB.server.DAO;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;
import com.l3.CB.server.DO.CommentDO;
import com.l3.CB.server.utils.ServerUtils;
import com.l3.CB.shared.CommentFilter;
import com.l3.CB.shared.FacebookUtil;
import com.l3.CB.shared.TO.Comment;

public class CommentDAO {

    static {
	ObjectifyService.register(CommentDO.class);
    }

    public static void saveComment(Comment comment) {
	if(comment != null) {
	    CommentDO commentDO = new CommentDO(comment);
	    ofy().save().entities(commentDO).now();
	}
    }

    public static CommentFilter getComments(CommentFilter filter) {
	List<Comment> comments = null;
	if(filter != null) {
	    Query<CommentDO> query = ofy().load().type(CommentDO.class)
		    .filter("confId", filter.getConfId());

	    int numberOfComments = query.count();
	    filter.setTotalNumberOfComments(numberOfComments);
	    
	    query = query.offset(filter.getPageSize() * filter.getPage())
		    .limit(filter.getPageSize())
		    .order("-timeStamp");

	    comments = new ArrayList<Comment>();

	    for (CommentDO commentDO : query) {
		Comment comment = commentDO.toCommentTO();

		if(!commentDO.isShareAsAnyn()) {
		    String userDetailsJSON = ServerUtils.fetchURL(FacebookUtil.getUserByIdUrl(commentDO.getFbId()));
		    comment.setUserDetailsJSON(userDetailsJSON);
		} else {
		    // MOST IMPORTANT CODE!!
		    comment.setFbId(null);
		}
		comments.add(comment);
	    }
	    
	    filter.setComments(comments);
	}
	return filter;
    }

    public static void vote(CommentFilter filter) {
	if(filter !=  null) {
	    CommentDO commentDO = ofy().load().type(CommentDO.class).id(filter.getCommentId()).get();
	    if(commentDO != null) {
		if(filter.isVoteReport()) {
		    commentDO.incNumOfAbuseVotes();
		} else if(filter.isVoteSecond()) {
		    commentDO.incNumOfSecondVotes();
		}
		ofy().save().entities(commentDO).now();
	    }
	}
    }
}
