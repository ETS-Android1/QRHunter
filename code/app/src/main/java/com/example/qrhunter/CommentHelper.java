package com.example.qrhunter;



import java.util.List;

/**
 * This class is responsible for representing the data in comments document in order to find information
 */
public class CommentHelper {
    private List<String> comments;

    public CommentHelper() {
    }

    public CommentHelper(List<String> comments) {
        this.comments = comments;
    }

    public List<String> getComments() {
        return this.comments;
    }
}
