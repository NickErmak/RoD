package com.paranoid.runordie.models.httpResponses;

public class SaveTrackResponse extends AbstractResponse{
    private long id;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}
