package app.com.customviews.models;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class SongModel implements Parcelable {
    private String songName;
    private String Artist;
    private Bitmap bitmap;
    private String Album;
    private String Track;
    private String Data;
    private Long AlbumId;
    private int Duration;
    private String AlbumArtUri;

    public SongModel() {

    }

    public SongModel(Parcel in) {

        songName = in.readString();
        Artist = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        Album = in.readString();
        Track = in.readString();
        Data = in.readString();
        AlbumId = in.readLong();
        Duration = in.readInt();
        AlbumArtUri = in.readString();


    }

    public String getArtist() {
        return Artist;
    }

    public Bitmap getBitmap() {
        return bitmap;
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

    public String getAlbumArtUri() {
        return AlbumArtUri;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
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

    public void setAlbumArtUri(String albumArtUri) {
        AlbumArtUri = albumArtUri;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(songName);
        parcel.writeString(Artist);
        parcel.writeValue(bitmap);
        parcel.writeString(Album);
        parcel.writeString(Track);
        parcel.writeString(Data);
        parcel.writeLong(AlbumId);
        parcel.writeInt(Duration);
        parcel.writeString(AlbumArtUri);
    }

    public static final Parcelable.Creator<SongModel> CREATOR = new Parcelable.Creator<SongModel>() {

        @Override
        public SongModel createFromParcel(Parcel parcel) {
            return new SongModel(parcel);
        }

        @Override
        public SongModel[] newArray(int size) {
            return new SongModel[size];
        }
    };
}
