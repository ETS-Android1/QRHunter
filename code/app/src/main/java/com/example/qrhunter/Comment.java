// A comment representation which will be used for showing comments and their user

package com.example.qrhunter;

/**
 * This class is responsible for representing a comment
 */
public class Comment {
    private String user;
    private String comment;

    public Comment(String comment) {
        this.comment = comment;
    }

    /**
     * Getter for comment data
     * @return the comment itself
     */
    public String getComment() {
        return this.comment;
    }


    /**
     * Setter for comment data
     * @param comment the comment to be set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}
