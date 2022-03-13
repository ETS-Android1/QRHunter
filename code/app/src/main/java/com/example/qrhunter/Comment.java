package com.example.qrhunter;

/**
 * This class is responsible for representing a comment
 */
public class Comment {
    private String user;
    private String comment;

    public Comment(String user, String comment) {
        this.user = user;
        this.comment = comment;
    }

    /**
     * Getter for username
     * @return the username
     */
    public String getUser() {
        return this.user;
    }
    /**
     * Getter for comment data
     * @return the comment itself
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * Setter for username
     * @param user the user to be set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Setter for comment data
     * @param comment the comment to be set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}
