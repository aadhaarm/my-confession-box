package com.l3.CB.server.manager;

import java.util.List;

import com.l3.CB.server.DAO.CommentDAO;
import com.l3.CB.shared.CommentFilter;
import com.l3.CB.shared.TO.Comment;

public class CommentManager {

    public static void saveComment(Comment comment) {
	CommentDAO.saveComment(comment);
    }

    public static List<Comment> getComments(CommentFilter filter) {
	return CommentDAO.getComments(filter);
    }

    public static void vote(CommentFilter filter) {
	CommentDAO.vote(filter);
    }
}