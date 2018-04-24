package com.paranoid.runordie.models;

public class State {
    private User user;
    private String token;
    private boolean isRunning;

    public boolean isRunning() {
        return isRunning;
    }

    public void setShowProgress(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
