package com.example.qrhunter;

public class LeaderBoardHolder implements Comparable<LeaderBoardHolder>{
    private String userName;
    private String userScore;
    private int userRank;

    public LeaderBoardHolder(String userName, String userScore) {
        this.userName = userName;
        this.userScore = userScore;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserScore(String userScore) {
        this.userScore= userScore;
    }
    public String getUserScore() {
        return userScore;
    }

    public int getUserRank() {
        return userRank;
    }

    public void setUserRank(int userRank) {
        this.userRank = userRank;
    }

    @Override
    public String toString() {
        return "LeaderBoardHolder{" +
                "userScore='" + userScore + '\'' +
                '}';
    }

    @Override
    public int compareTo(LeaderBoardHolder leaderBoardHolder) {
        return toString().compareTo(leaderBoardHolder.toString());
    }
}
