package com.paranoid.runordie.models;

public class State {
    private User user;
    private String token;
    private boolean isLoading;

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
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
