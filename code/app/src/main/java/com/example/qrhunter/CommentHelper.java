package com.example.qrhunter;



import java.util.List;

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
