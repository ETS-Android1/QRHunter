package com.example.qrhunter;

public class LeaderBoardHolder implements Comparable<LeaderBoardHolder>{
    private String userName;
    private String userScore;
    private int userRank;

    public LeaderBoardHolder(String userName, String userScore) {
        this.userName = userName;
        this.userScore = userScore;
    }

    /**
     * gets the user name
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * sets the user score
     */
    public void setUserScore(String userScore) {
        this.userScore= userScore;
    }

    /**
     * gets the user score
     * @return the user score
     */
    public String getUserScore() {
        return userScore;
    }

    /**
     * gets the user rank
     * @return user rank
     */
    public int getUserRank() {
        return userRank;
    }

    /**
     * sets the user rank
     * @param userRank
     */
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
        return Double.valueOf(this.getUserScore()).compareTo(Double.valueOf(leaderBoardHolder.getUserScore()));
    }
}
