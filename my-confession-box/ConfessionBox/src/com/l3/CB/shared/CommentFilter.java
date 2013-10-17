package com.l3.CB.shared;

import java.io.Serializable;
import java.util.List;

import com.l3.CB.shared.TO.Comment;

public class CommentFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long confId;
 
    private int page;
    
    private int pageSize;
    
    private List<Comment> comments;
    
    private int totalNumberOfComments;
    
    /*
     * Vote    
     */
    private Long commentId;
    
    private boolean voteReport = false;
    
    private boolean voteSecond = false;
    
    public Long getConfId() {
        return confId;
    }

    public void setConfId(Long confId) {
        this.confId = confId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public boolean isVoteReport() {
        return voteReport;
    }

    public void setVoteReport(boolean voteReport) {
        this.voteReport = voteReport;
    }

    public boolean isVoteSecond() {
        return voteSecond;
    }

    public void setVoteSecond(boolean voteSecond) {
        this.voteSecond = voteSecond;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getTotalNumberOfComments() {
        return totalNumberOfComments;
    }

    public void setTotalNumberOfComments(int totalNumberOfComments) {
        this.totalNumberOfComments = totalNumberOfComments;
    }
}