package app.com.customviews.models;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;

public class AudioListModel implements Serializable {
    private String songName;
    private String Artist;
    private String bitmapUri;
    private transient Bitmap bitmap;
    private String Album;
    private String Track;
    private String Data;
    private Long AlbumId;
    private int Duration;
    private transient Uri AlbumArtUri;

    public AudioListModel() {

    }

    public String getBitmapUri() {
        return bitmapUri;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getArtist() {
        return Artist;
    }

    public String getAlbum() {
        return Album;
    }

    public String getTrack() {
        return Track;
    }

    public String getData() {
        return Data;
    }

    public Long getAlbumId() {
        return AlbumId;
    }

    public int getDuration() {
        return Duration;
    }

    public Uri getAlbumArtUri() {
        return AlbumArtUri;
    }

    public void setBitmapUri(String bitmapUri) {
        this.bitmapUri = bitmapUri;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public void setAlbum(String album) {
        Album = album;
    }

    public void setTrack(String track) {
        Track = track;
    }

    public void setData(String data) {
        Data = data;
    }

    public void setAlbumId(Long albumId) {
        AlbumId = albumId;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public void setAlbumArtUri(Uri albumArtUri) {
        AlbumArtUri = albumArtUri;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
}
