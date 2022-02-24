package com.example.qrhunter;

public class Comment {
    private String user;
    private String comment;

    public Comment(String user, String comment) {
        this.user = user;
        this.comment = comment;
    }

    public String getUser() {
        return this.user;
    }
    public String getComment() {
        return this.comment;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
