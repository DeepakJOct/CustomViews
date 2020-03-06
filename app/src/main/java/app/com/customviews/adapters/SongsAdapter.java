package app.com.customviews.adapters;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import app.com.customviews.R;
import app.com.customviews.models.AudioListModel;
import app.com.customviews.models.SongModel;
import app.com.customviews.views.CustomFontTextView;
import app.com.customviews.views.CustomImageView;
import wseemann.media.FFmpegMediaMetadataRetriever;

public class SongsAdapter extends BaseAdapter {

    ArrayList<AudioListModel> songsArrayList;
    Context context;
    Thread albumThread;

    public SongsAdapter(ArrayList<AudioListModel> songsArrayList, Context context) {
        this.songsArrayList = songsArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return songsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CustomImageView albumArt;
        CustomFontTextView songName, artist;

        if (convertView == null) {
            row = inflater.inflate(R.layout.item_song_with_album_art, parent, false);
        } else {
            row = convertView;
        }

        songName = row.findViewById(R.id.song_name);
        albumArt = row.findViewById(R.id.album_art);
        artist = row.findViewById(R.id.tv_artist);

        if (songsArrayList.get(position).getBitmap() == null) {
            albumArt.setImageResource(R.drawable.ic_music);
        } else {
            albumArt.setImageBitmap(songsArrayList.get(position).getBitmap());
        }
        songName.setText(songsArrayList.get(position).getSongName());
        artist.setText(songsArrayList.get(position).getArtist());




        /*Bitmap bitmap = null;
        FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
        FileInputStream input = null;

        try {
            input = new FileInputStream(songsArrayList.get(position).getAbsolutePath());
            mmr.setDataSource(input.getFD());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        byte[] albumImg = mmr.getEmbeddedPicture();
        if (albumImg != null) {

            bitmap = BitmapFactory.decodeByteArray(albumImg, 0, albumImg.length);
            albumArt = row.findViewById(R.id.album_art);
            Glide.with(context).
                    load(bitmap).override(120, 120).
                    into(albumArt);
            //albumArt.setImageBitmap(bitmap);
        } else {
            albumArt = row.findViewById(R.id.album_art);
            albumArt.setImageResource(R.drawable.ic_music);
        }
        albumArt.setAdjustViewBounds(true);
        mmr.release();

        CustomFontTextView songName = row.findViewById(R.id.song_name);
        songName.setText(songsArrayList.get(position).getName().replace(".mp3", "").replace(".wav", ""));*/

        return row;
    }

}
