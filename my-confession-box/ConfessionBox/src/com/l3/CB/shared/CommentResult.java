package com.l3.CB.shared;

import java.io.Serializable;
import java.util.List;

import com.l3.CB.shared.TO.Comment;

public class CommentResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Comment> comments;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
