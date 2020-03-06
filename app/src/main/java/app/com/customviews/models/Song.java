package app.com.customviews.models;

import android.graphics.Bitmap;

public class Song {
    String songName;
    Bitmap albumImg;

    public Song() {

    }

    public Song(String songName, Bitmap albumImg) {
        this.songName = songName;
        this.albumImg = albumImg;
    }

    public String getSongName() {
        return songName;
    }

    public Bitmap getAlbumImg() {
        return albumImg;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setAlbumImg(Bitmap albumImg) {
        this.albumImg = albumImg;
    }
}
