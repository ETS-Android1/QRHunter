package com.example.qrhunter;

public class Profile {
    private String userName;

    public Profile(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
