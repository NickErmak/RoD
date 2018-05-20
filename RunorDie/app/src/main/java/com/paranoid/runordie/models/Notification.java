package com.paranoid.runordie.models;

public class Notification {
    public enum CRUD_STATUS{
        NONE, INSERT, UPDATE
    }

    public static final String DB_ID = "_id";
    public static final String EXEC_TIME = "executionTime";
    public static final String TITLE = "title";

    private Long id;
    private long executionTime;
    private String title;
    private CRUD_STATUS crudStatus;

    public Notification(long executionTime, String title) {
        this(null, executionTime, title);
    }

    public Notification(Long id, long executionTime, String title) {
        this.id = id;
        this.executionTime = executionTime;
        this.title = title;
        crudStatus = CRUD_STATUS.NONE;
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

    public CRUD_STATUS getCrudStatus() {
        return crudStatus;
    }

    public void setCrudStatus(CRUD_STATUS crudStatus) {
        this.crudStatus = crudStatus;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", executionTime=" + executionTime +
                ", title='" + title + '\'' +
                ", crudStatus=" + crudStatus +
                '}';
    }
}
