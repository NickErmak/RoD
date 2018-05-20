package com.paranoid.runordie.models.httpResponses;

import com.paranoid.runordie.models.Track;

import java.util.List;

public class TrackResponse extends AbstractResponse {

    private List<Track> tracks;

    public List<Track> getTracks() {
        return tracks;
    }
    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}
