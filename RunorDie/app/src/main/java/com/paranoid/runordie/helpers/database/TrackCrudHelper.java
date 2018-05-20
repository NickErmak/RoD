package com.paranoid.runordie.helpers.database;

import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.models.Track;
import com.paranoid.runordie.utils.JsonConverter;

import java.util.LinkedList;
import java.util.List;

public class TrackCrudHelper {

    public static Cursor loadTrackCursor() {
        return App.getInstance().getDb().rawQuery(
                App.getInstance().getString(R.string.sql_select_tracks),
                null
        );
    }

    public static Cursor getTrack(long trackId) {
        return App.getInstance().getDb().rawQuery(
                App.getInstance().getString(R.string.sql_select_track),
                new String[]{String.valueOf(trackId)}
        );
    }

    public static List<Track> loadTracksNoServerId() {
        List<Track> tracks = new LinkedList<>();

        Cursor cursor = App.getInstance().getDb().rawQuery(
                App.getInstance().getString(R.string.sql_select_tracks_no_serverId),
                null
        );
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int dbIdIndex = cursor.getColumnIndex(Track.DB_ID);
                int distanceIndex = cursor.getColumnIndexOrThrow(Track.DISTANCE);
                int startTimeIndex = cursor.getColumnIndexOrThrow(Track.START_TIME);
                int runTimeIndex = cursor.getColumnIndexOrThrow(Track.RUN_TIME);
                int pointsIndex = cursor.getColumnIndexOrThrow(Track.POINTS);

                do {
                    long dbId = cursor.getLong(dbIdIndex);
                    int distance = cursor.getInt(distanceIndex);
                    long startTime = cursor.getLong(startTimeIndex);
                    long runTime = cursor.getLong(runTimeIndex);
                    String pointsJson = cursor.getString(pointsIndex);

                    tracks.add(new Track(
                            dbId,
                            startTime,
                            runTime,
                            distance,
                            JsonConverter.convertJson(pointsJson)
                    ));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return tracks;
    }

    public static List<Track> getTracksServerIdOnly() {
        List<Track> tracks = new LinkedList<>();

        Cursor cursor = App.getInstance().getDb().rawQuery(
                App.getInstance().getString(R.string.sql_select_tracks_serverId),
                null
        );
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int serverIdIndex = cursor.getColumnIndexOrThrow(Track.ID);
                do {
                    long serverId = cursor.getLong(serverIdIndex);
                    tracks.add(new Track(serverId));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return tracks;
    }

    public static void updateTrackServerId(Track track) {
        Log.d("TAG", "update track serverId: " + track);
        App.getInstance().getDb().execSQL(
                App.getInstance().getString(R.string.sql_update_track_serverId),
                new String[]{
                        String.valueOf(track.getServerId()),
                        String.valueOf(track.getDbId())
                }
        );
    }

    public static Long insertTrackWithServerId(Track track) {
        Log.d("TAG", "insert track: " + track);
        String[] args = {
                String.valueOf(track.getBeginsAt()),
                String.valueOf(track.getServerId()),
                String.valueOf(track.getDistance()),
                String.valueOf(track.getTime()),
                new Gson().toJson(track.getPoints())
        };

        return DbCrudHelper.insert(
                App.getInstance().getString(R.string.sql_insert_track),
                args
        );
    }

    public static Long insertTrackNoServerId(Track track) {
        Log.d("TAG", "insert track: " + track);
        String[] args = {
                String.valueOf(track.getBeginsAt()),
                String.valueOf(track.getDistance()),
                String.valueOf(track.getTime()),
                new GsonBuilder().create().toJson(track.getPoints())
        };

        return DbCrudHelper.insert(
                App.getInstance().getString(R.string.sql_insert_track_no_serverId),
                args
        );
    }
}
