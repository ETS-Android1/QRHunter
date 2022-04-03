package com.example.qrhunter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;

/**
 * This class is responsible for testing the comment list which is used in UserQRInfoActivity
 */
public class CommentTest {
    /**
     * We create an initial instance of the commentList used for testing
     * @return returns the commentList
     */
    private ArrayList<Comment> mockCommentList() {
        ArrayList<Comment> commentListData = new ArrayList<Comment>();
        commentListData.add(new Comment("Some example comment."));
        return commentListData;
    }

    /**
     * Testing adding a comment to the list
     */
    @Test
    public void testAddComment() {
        ArrayList<Comment> commentList = mockCommentList();
        assertEquals(1, commentList.size());
        commentList.add(new Comment("Another example comment"));
        assertEquals(2, commentList.size()); // check if size changed
    }

    /**
     * Testing deleting a comment to the list
     */
    @Test
    public void testDelComment() {
        ArrayList<Comment> commentList = mockCommentList();
        assertEquals(1, commentList.size());
        commentList.remove(0);
        assertEquals(0, commentList.size()); // check if size changed
    }
}
