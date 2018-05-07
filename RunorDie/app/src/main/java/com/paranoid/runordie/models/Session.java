package com.paranoid.runordie.models;

import com.paranoid.runordie.models.User;

public class Session {

    private User user;
    private String token;

    public Session(User user, String token) {
        this.user = user;
        this.token = token;
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
