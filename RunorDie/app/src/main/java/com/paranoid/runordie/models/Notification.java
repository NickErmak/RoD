package com.paranoid.runordie.models;

public class Notification {

    public static final String _ID = "_id";
    public static final String EXEC_TIME = "executionTime";
    public static final String TITLE = "title";

    private long id;
    private long executionTime;
    private String title;

    public Notification(long id, long executionTime, String title) {
        this.id = id;
        this.executionTime = executionTime;
        this.title = title;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getExecutionTime() {
        return executionTime;
    }
    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
