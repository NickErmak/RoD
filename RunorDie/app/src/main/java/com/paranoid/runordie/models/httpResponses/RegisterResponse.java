package com.paranoid.runordie.models.httpResponses;

public class RegisterResponse extends AbstractResponse{

    private String token;

    public RegisterResponse() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "RegisterResponse{" +
                "token='" + token + '\'' +
                "} " + super.toString();
    }
}
