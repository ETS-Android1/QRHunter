package com.example.qrhunter;

import static org.junit.Assert.assertEquals;

import com.google.firebase.firestore.DocumentReference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * This Class tests the CustomQRList control Class to check if the array adapter is working
 */
public class LeaderboardTest {

    private LeaderBoardAdapter list;
    @Before
    public void createList() {
        list = new LeaderBoardAdapter( new ArrayList<>(), null, new LeaderBoardAdapter.OnItemListener() {
            @Override
            public void OnItemClick(int position) {

            }
        });
    }

    @Test
    public void checkEmpty(){
        //change this to QR add testcase
        assertEquals(0,list.getItemCount());
    }

    @Test
    public void checkOne(){
        //change this to QR add testcase
        ArrayList<LeaderBoardHolder> holders = new ArrayList<>();
        holders.add(new LeaderBoardHolder("owen", "12"));
        LeaderBoardAdapter list2 = new LeaderBoardAdapter( holders, null, new LeaderBoardAdapter.OnItemListener() {
            @Override
            public void OnItemClick(int position) {

            }
        });
        assertEquals(1,list2.getItemCount());
    }

    @Test
    public void checkCompare() {
        LeaderBoardHolder h1 = new LeaderBoardHolder("owen", "12");
        LeaderBoardHolder h2 = new LeaderBoardHolder("owen", "13");
        int compare = h1.compareTo(h2);
        assertEquals(-1,compare);

    }

    @Test
    public void checkComparePositive() {
        LeaderBoardHolder h1 = new LeaderBoardHolder("owen", "12");
        LeaderBoardHolder h2 = new LeaderBoardHolder("owen", "13");
        int compare = h2.compareTo(h1);
        assertEquals(1,compare);
    }

    @Test
    public void setUserRank() {
        LeaderBoardHolder h1 = new LeaderBoardHolder("owen", "12");
        h1.setUserRank(0);
        assertEquals(0,h1.getUserRank());
    }

    @Test
    public void setUserScore() {
        LeaderBoardHolder h1 = new LeaderBoardHolder("owen", "12");
        h1.setUserScore("323");
        assertEquals("323",h1.getUserScore());
    }
}
