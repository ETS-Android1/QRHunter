package com.example.qrhunter;

public class LeaderBoardHolder implements Comparable<LeaderBoardHolder>{
    private String userName;
    private String userScore;

    public LeaderBoardHolder(String userName, String userScore) {
        this.userName = userName;
        this.userScore = userScore;
    }

    public String getUserName() {
        return userName;
    }


    public String getUserScore() {
        return userScore;
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
