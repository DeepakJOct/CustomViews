package app.com.customviews.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;
import androidx.lifecycle.Observer;
import androidx.palette.graphics.Palette;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ListIterator;

import app.com.customviews.R;
import app.com.customviews.adapters.SongsAdapter;
import app.com.customviews.fragments.FloatingPlayer;
import app.com.customviews.interfaces.CurrentSongDataListener;
import app.com.customviews.models.AudioListModel;
import app.com.customviews.models.Song;
import app.com.customviews.models.SongModel;
import app.com.customviews.utils.CommonUtils;
import app.com.customviews.views.CustomFontTextView;
import app.com.customviews.views.CustomImageView;

public class MusicPlayerActivity extends AppCompatActivity {

    ListView musicListView;
    String[] items;
    RelativeLayout rlMain, rlNoViews;
    TextView toolbarTitle;
    ArrayList<Song> songsList;
    //    private ArrayList<SongModel> audioArrayList = new ArrayList<SongModel>();
    private ArrayList<AudioListModel> audioArrayList = new ArrayList<AudioListModel>();
    private AudioListModel nowPlaying;
    RelativeLayout rlPlayer;

    CustomImageView ivAlbumArt;
    RelativeLayout rlNowPlaying;
    CustomFontTextView songName;
    CustomFontTextView tvArtist;
    RelativeLayout rlNavigation;
    Button mPlay, mNext, mPrevious;
    private MediaPlayer miniPlayer;
    private int musicCounter = 0;
    ProgressDialog dialog;

    AudioListModel nowPlayingSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        musicListView = findViewById(R.id.musicListView);
        rlMain = findViewById(R.id.rl_main);
        rlNoViews = findViewById(R.id.rl_no_view);
        toolbarTitle = findViewById(R.id.tv_toolbar_title);
        toolbarTitle.setText("harmony");

        ivAlbumArt = findViewById(R.id.iv_album_art);
        rlNowPlaying = findViewById(R.id.rl_player);
        songName = findViewById(R.id.song_name);
        tvArtist = findViewById(R.id.tv_artist);
        rlNavigation = findViewById(R.id.rl_navigation);
        mPlay = findViewById(R.id.fl_play);
        mNext = findViewById(R.id.fl_next);
        mPrevious = findViewById(R.id.fl_previous);
        miniPlayer = new MediaPlayer();
        dialog = new ProgressDialog(this);

        requestPermissions();
        rlNowPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity();
            }
        });
    }

    private void requestPermissions() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        dialog.setMessage("Loading media...");
                        dialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getAllSongs();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        setAdapter();
                                        onListClick();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        }).start();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private ArrayList<File> findFile(File file) {

        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();
        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                arrayList.addAll(findFile(singleFile));
            } else {
                if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
                    arrayList.add(singleFile);
                }
            }
        }
        return arrayList;
    }

    void display() {
        final ArrayList<File> mySongs = findFile(Environment.getExternalStorageDirectory());

        /*songsList = new ArrayList<>();
        for (int i = 0; i < mySongs.size(); i++) {
            String songName = mySongs.get(i).getName().replace(".mp3", "").replace(".wav", "");
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(mySongs.get(i).getAbsolutePath());
            byte[] albumImg = mmr.getEmbeddedPicture();
            Bitmap bitmap = BitmapFactory.decodeByteArray(albumImg, 0, albumImg.length);
            Song song = new Song(songName, bitmap);
            songsList.add(song);
        }*/

        items = new String[mySongs.size()];
        for (int i = 0; i < mySongs.size(); i++) {
            items[i] = mySongs.get(i).getName().replace(".mp3", "").replace(".wav", "");
        }
        if (items != null && items.length > 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_song_with_album_art, items);
            musicListView.setAdapter(adapter);
        } else {
            rlMain.setVisibility(View.GONE);
            rlNoViews.setVisibility(View.VISIBLE);
        }
        if (mySongs != null && mySongs.size() > 0) {
            /*SongsAdapter adapter = new SongsAdapter(mySongs, getApplicationContext());
            musicListView.setAdapter(adapter);
            musicListView.setScrollingCacheEnabled(false);*/
        } else {
            rlMain.setVisibility(View.GONE);
            rlNoViews.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        PlayerActivity.getLiveSong().observe(this, new Observer<AudioListModel>() {
            @Override
            public void onChanged(AudioListModel audioListModel) {
                if (PlayerActivity.mediaPlayer != null && PlayerActivity.mediaPlayer.isPlaying()) {
                    nowPlayingSong = audioListModel;
                    rlNowPlaying.setVisibility(View.VISIBLE);
                    Log.d("LiveSong--->Changes-->", audioListModel.getSongName());
                    songName.setText(audioListModel.getSongName());
                    tvArtist.setText(audioListModel.getArtist());
                    mPlay.setOnClickListener(view -> {
                        if (PlayerActivity.mediaPlayer.isPlaying()) {
                            mPlay.setBackgroundResource(R.drawable.ic_play);
                            PlayerActivity.mediaPlayer.pause();
                            getDrawable(R.drawable.play).setTint(getResources().getColor(R.color.colorPrimary));
                        } else {
                            mPlay.setBackgroundResource(R.drawable.ic_pause);
                            PlayerActivity.mediaPlayer.start();
                            getDrawable(R.drawable.ic_pause).setTint(getResources().getColor(R.color.colorPrimary));
                        }
                    });
                    /*mNext.setOnClickListener(view -> new PlayerActivity().playNext());
                    mPrevious.setOnClickListener(view -> new PlayerActivity().playPrevious());*/
                    Bitmap bitmap = null;

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(
                                getContentResolver(), Uri.parse(audioListModel.getBitmapUri()));
                        bitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, false);

                    } catch (FileNotFoundException exception) {
                        exception.printStackTrace();
                        bitmap = BitmapFactory.decodeResource(getResources(),
                                R.drawable.ic_music);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                    ivAlbumArt.setImageBitmap(bitmap);
                    Palette p = CommonUtils.createPaletteSync(bitmap);
                    rlNowPlaying.setBackgroundColor(p.getDarkMutedColor(Color.WHITE));
                    if (ColorUtils.calculateLuminance(p.getDarkMutedColor(Color.WHITE)) < 0.5) {
                        songName.setTextColor(Color.parseColor("#FFF8C6"));
                        tvArtist.setTextColor(Color.parseColor("#95B9C7"));
                    }
                } else if (PlayerActivity.mediaPlayer == null) {
                    rlNowPlaying.setVisibility(View.GONE);
                }
            }
        });

        mNext.setOnClickListener(view -> playNext());
        mPrevious.setOnClickListener(view -> playPrevious());

    }

    private void playNext() {
        miniPlayer.stop();
        miniPlayer.release();
        Uri u = Uri.parse(getNext().getData());
        miniPlayer = MediaPlayer.create(getApplicationContext(), u);
        songName.setText(getNext().getSongName());
        tvArtist.setText(getNext().getArtist());
        ivAlbumArt.setImageBitmap(getBitmap(this, Uri.parse(getPrevious().getBitmapUri())));
        Bitmap bitmap = getBitmap(this, Uri.parse(getPrevious().getBitmapUri()));
        /*Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette p) {
                rlNowPlaying.setBackgroundColor(p.getDarkMutedColor(Color.WHITE));
                if (ColorUtils.calculateLuminance(p.getDarkMutedColor(Color.WHITE)) < 0.5) {
                    songName.setTextColor(Color.parseColor("#FFF8C6"));
                    tvArtist.setTextColor(Color.parseColor("#95B9C7"));
                }
            }
        });*/
    }

    private void playPrevious() {
        miniPlayer.stop();
        miniPlayer.release();
        Uri u = Uri.parse(getPrevious().getData());
        miniPlayer = MediaPlayer.create(getApplicationContext(), u);
        songName.setText(getPrevious().getSongName());
        tvArtist.setText(getPrevious().getArtist());
        ivAlbumArt.setImageBitmap(getBitmap(this, Uri.parse(getPrevious().getBitmapUri())));
        Bitmap bitmap = getBitmap(this, Uri.parse(getPrevious().getBitmapUri()));
        /*Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette p) {
                rlNowPlaying.setBackgroundColor(p.getDarkMutedColor(Color.WHITE));
                if (ColorUtils.calculateLuminance(p.getDarkMutedColor(Color.WHITE)) < 0.5) {
                    songName.setTextColor(Color.parseColor("#FFF8C6"));
                    tvArtist.setTextColor(Color.parseColor("#95B9C7"));
                }
            }
        });*/

    }

    AudioListModel getNext() {
        AudioListModel nextSong = null;
        if (musicCounter < audioArrayList.size()) {
            nextSong = audioArrayList.get(musicCounter + 1);
            musicCounter++;
        }
        return nextSong;
    }

    AudioListModel getPrevious() {
        if (musicCounter == 0) {
            musicCounter = audioArrayList.size();
        }
        AudioListModel previousSong = audioArrayList.get(musicCounter + -1);
        musicCounter--;
        return previousSong;
    }

    private void getAllSongs() {
        final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final String[] cursor_cols = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION};
        final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
        final Cursor cursor = getContentResolver().query(uri,
                cursor_cols, where, null, null);
        while (cursor.moveToNext()) {
            final String songName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            final String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            final String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            final long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
            final String data = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            /*String track = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK));*/
            int duration = cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));


            Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");
            Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), albumArtUri);
                bitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, false);

            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_music);
            } catch (IOException e) {

                e.printStackTrace();
            }

            Log.d("MusicPlayerActivity--> ", "Bitmap-->" + bitmap);

            AudioListModel audioListModel = new AudioListModel();
            audioListModel.setSongName(songName);
            audioListModel.setArtist(artist);
            audioListModel.setBitmap(bitmap);
            audioListModel.setBitmapUri(albumArtUri.toString());
            Log.d("MusicPlayerActivity--> ", "albumArtUri-->" + albumArtUri);
            audioListModel.setAlbum(album);
//            audioListModel.setTrack(track);
            audioListModel.setData(data);
            audioListModel.setAlbumId(albumId);
            audioListModel.setDuration(duration);


            /*SongModel audioListModel = new SongModel();
            audioListModel.setSongName(songName);
            audioListModel.setArtist(artist);
            audioListModel.setBitmap(bitmap);
            audioListModel.setAlbum(album);
//            audioListModel.setTrack(track);
            audioListModel.setData(data);
            audioListModel.setAlbumId(albumId);
            audioListModel.setDuration(duration);
            audioListModel.setAlbumArtUri(albumArtUri.toString());*/

            audioArrayList.add(audioListModel);
            Log.d("MusicPlayer-->", "modeldetails" + audioListModel);

        }
    }

    private void setAdapter() {
        if (audioArrayList != null) {
            SongsAdapter adapter = new SongsAdapter(audioArrayList, getApplicationContext());
            musicListView.setAdapter(adapter);
            musicListView.setScrollingCacheEnabled(false);
        }
    }

    private void onListClick() {
        musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AudioListModel song = audioArrayList.get(i);
                startActivity(new Intent(getApplicationContext(), PlayerActivity.class)
                        .putExtra("mySongs", audioArrayList)
                        .putExtra("song", song)
                        .putExtra("pos", i));

            }
        });
    }

    void startActivity() {
        int nowPlayingSongPos = 0;
        int seekPosition = PlayerActivity.mediaPlayer.getCurrentPosition();
        for (int i = 0; i < audioArrayList.size(); i++) {
            if (audioArrayList.get(i).getSongName().equals(nowPlayingSong.getSongName())) {
                nowPlayingSongPos = i;
            }
        }
        startActivity(new Intent(getApplicationContext(), PlayerActivity.class)
                .putExtra("mySongs", audioArrayList)
                .putExtra("song", nowPlayingSong)
                .putExtra("pos", nowPlayingSongPos)
                .putExtra("seekPoint", seekPosition));
    }

    /*@Override
    public void onSongChanged(Object object, boolean isSongChanged) {
        if(isSongChanged) {
            nowPlaying = (AudioListModel) object;
        } else {
            nowPlaying = (AudioListModel) object;
        }
        Log.d("SongChanges--> ", nowPlaying.getSongName());
    }*/

    Bitmap getBitmap(Context context, Uri albumArtUri) {
        Bitmap bitmap = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(
                    context.getContentResolver(), albumArtUri);
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);

        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ic_music);
        } catch (IOException e) {

            e.printStackTrace();
        }
        Log.d("MusicPlayerActivity--> ", "Bitmap-->" + bitmap);
        return bitmap;
    }
}
