package com.paranoid.runordie.models;

import com.paranoid.runordie.utils.DateConverter;

public class Notification {

    public static final String _ID = "_id";
    public static final String EXEC_TIME = "executionTime";
    public static final String TITLE = "title";

    private Long id;
    private long executionTime;
    private String title;

    public Notification(long executionTime, String title) {
        this(null, executionTime, title);
    }

    public Notification(Long id, long executionTime, String title) {
        this.id = id;
        this.executionTime = executionTime;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", executionTime=" + DateConverter.parseTimeToString(executionTime) +
                ", title='" + title + '\'' +
                '}';
    }
}
