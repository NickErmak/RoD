package com.paranoid.runordie.models.httpResponses;

public abstract class AbstractResponse {

    public static final String INVALID_TOKEN = "INVALID_TOKEN";

    public enum STATUS {
        ok, error;
    }

    private STATUS status;
    private String code;

    public AbstractResponse() {    }

    public STATUS getStatus() {
        return status;
    }
    public void setStatus(STATUS status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "AbstractResponse{" +
                "status=" + status +
                ", code='" + code + '\'' +
                '}';
    }
}
