package com.paranoid.runordie.models;

import java.util.List;

public class TrackListResult {

    public enum TYPE_FROM {
        SERVER, DATABASE
    }

    private TYPE_FROM type;
    private List<Track> tracks;

    public TrackListResult(TYPE_FROM type, List<Track> tracks) {
        this.type = type;
        this.tracks = tracks;
    }

    public TYPE_FROM getType() {
        return type;
    }

    public void setType(TYPE_FROM type) {
        this.type = type;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}
