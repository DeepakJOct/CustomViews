package app.com.customviews.activities;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.ColorUtils;
import androidx.lifecycle.MutableLiveData;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import app.com.customviews.R;
import app.com.customviews.interfaces.CurrentSongDataListener;
import app.com.customviews.models.AudioListModel;
import app.com.customviews.services.AudioService;
import app.com.customviews.services.AudioServiceBinder;
import app.com.customviews.utils.CommonUtils;
import app.com.customviews.views.CustomFontTextView;
import app.com.customviews.views.CustomImageView;

public class PlayerActivityNew extends AppCompatActivity implements View.OnClickListener {

    private static AudioListModel liveSong;
    static Button next, play, previous;
    SeekBar seekBar;
    CustomImageView ivAlbumArt;
    CustomFontTextView tvSongName;
    CustomFontTextView tvDurationElasp, tvDurationRemain, tvArtistName;
    LinearLayout llMain;

    //    static MediaPlayer mediaPlayer;
    int position;

    ArrayList<AudioListModel> mySongs;
    Thread updateSeekbar;
    AudioListModel song;
    TextView toolbarText;
    private String whiteTint = "#FFE4E1";

    //new
    private AudioServiceBinder audioServiceBinder;
    public static Handler audioProgressUpdateHandler;
    // This service connection object is the bridge between activity and background service.
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("PlayerActivityNew-->", "binderServiceConnected");
            audioServiceBinder = (AudioServiceBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        bindAudioService();
        Log.d("binderCheck--> ", audioServiceBinder + "");

        next = findViewById(R.id.next);
        play = findViewById(R.id.play);
        previous = findViewById(R.id.previous);
        seekBar = findViewById(R.id.seek_bar);
        ivAlbumArt = findViewById(R.id.iv_album_art);
        tvSongName = findViewById(R.id.tv_song_name);
        tvDurationElasp = findViewById(R.id.tv_duration_elasp);
        tvDurationRemain = findViewById(R.id.tv_duration_remain);
        tvArtistName = findViewById(R.id.tv_artist_name);
        llMain = findViewById(R.id.ll_main);
//        currentSongDataListener = new CurrentSongDataListener();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Now Playing");
        //to show back button in the toolbar for going back to home
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        next.setOnClickListener(this);
        play.setOnClickListener(this);
        previous.setOnClickListener(this);

        updateSeekbar = new Thread() {
            @Override
            public void run() {
                int totalDuration = audioServiceBinder.getTotalAudioDuration();
                int currentPosition = 0;

                while (currentPosition < totalDuration) {
                    try {
                        sleep(500);
                        currentPosition = audioServiceBinder.getCurrentAudioPosition();
                        seekBar.setProgress(currentPosition);

                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        /*if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }*/

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mySongs = (ArrayList<AudioListModel>) getIntent().getSerializableExtra("mySongs");
//        mySongs = (ArrayList) bundle.getParcelableArrayList("mySongs");
        song = (AudioListModel) bundle.getSerializable("song");
        liveSong = song;
        Log.d("PlayerActivity--> ", "One Song-->" + song.getSongName() + "\nUri-->" + song.getBitmapUri() + "\nAlbum" + song.getAlbum());
        position = bundle.getInt("pos");
        Uri u = Uri.parse(song.getData());


//        mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
        //replace with service
        Log.d("AudioFileUri--> ", u + "");
        audioServiceBinder.setContext(getApplicationContext());
        audioServiceBinder.setAudioFileUri(u);
        createAudioPlayProgressUpdater();
        audioServiceBinder.setAudioProgressUpdateHandler(audioProgressUpdateHandler);

        tvSongName.setText(song.getSongName());
        tvArtistName.setText(song.getArtist());
        Glide.with(this).load(getBitmap(this, Uri.parse(song.getBitmapUri()))).into(ivAlbumArt);

        //Get color based on bitmap
        Palette p = CommonUtils.createPaletteSync(getBitmap(this, Uri.parse(song.getBitmapUri())));
//        llMain.setBackgroundColor(p.getVibrantColor(getResources().getColor(R.color.white)));
//        llMain.setBackgroundColor(p.getLightVibrantColor(getResources().getColor(R.color.white)));
        llMain.setBackgroundColor(p.getDarkMutedColor(getResources().getColor(R.color.white)));
        if (isDark(p.getDarkMutedColor(getResources().getColor(R.color.white)))) {
            getResources().getDrawable(R.drawable.ic_next).setTint(Color.parseColor(whiteTint));
            getResources().getDrawable(R.drawable.ic_previous).setTint(Color.parseColor(whiteTint));
            getResources().getDrawable(R.drawable.ic_play).setTint(Color.parseColor(whiteTint));
            getResources().getDrawable(R.drawable.ic_pause).setTint(Color.parseColor(whiteTint));
            tvDurationElasp.setTextColor(Color.parseColor(whiteTint));
            tvDurationRemain.setTextColor(Color.parseColor(whiteTint));
            tvSongName.setTextColor(Color.parseColor("#FFF8C6"));
            tvArtistName.setTextColor(Color.parseColor("#95B9C7"));
            seekBar.getProgressDrawable().setTint(Color.parseColor(whiteTint));
            seekBar.getThumb().setTint(Color.parseColor("#00b0ff"));
        }
//        ivAlbumArt.setImageBitmap(song.getBitmap());
        Log.d("PlayerActivity--> ", "Bitmap-->" + song.getBitmap());
        audioServiceBinder.startAudio();
        updateSeekbar.start();
        //set seekbar positions
        seekBar.setMax(audioServiceBinder.getTotalAudioDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                audioServiceBinder.seekAudio(seekBar.getProgress());
            }
        });
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                /*@SuppressLint("DefaultLocale") String elapsed = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getCurrentPosition()), TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getCurrentPosition())) ;
                @SuppressLint("DefaultLocale") String remaining =  String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getDuration()-mediaPlayer.getCurrentPosition()),
                        TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getDuration()-mediaPlayer.getCurrentPosition()));*/
                long duration = audioServiceBinder.getTotalAudioDuration();
                long currentDuration = audioServiceBinder.getCurrentAudioPosition();
                long remainingDuration = audioServiceBinder.getTotalAudioDuration() - audioServiceBinder.getCurrentAudioPosition();
                @SuppressLint("DefaultLocale") String elapsed = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(currentDuration),
                        TimeUnit.MILLISECONDS.toSeconds(currentDuration) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentDuration)));
                @SuppressLint("DefaultLocale") String remaining = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(remainingDuration),
                        TimeUnit.MILLISECONDS.toSeconds(remainingDuration) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingDuration)));
                tvDurationElasp.setText(elapsed);
                tvDurationRemain.setText(remaining);
                handler.postDelayed(this, 500);
            }
        });
//        currentSongDataListener.onSongChanged(song, false);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view == play) {
            if (audioServiceBinder.isPlaying()) {
                play.setBackgroundResource(R.drawable.ic_play);
                audioServiceBinder.pauseAudio();
            } else {
                play.setBackgroundResource(R.drawable.ic_pause);
                audioServiceBinder.startAudio();
            }
        } else if (view == next) {
            audioServiceBinder.stopAudio();
            play.setBackgroundResource(R.drawable.ic_play);
            position = ((position + 1) % mySongs.size());
            Uri u = Uri.parse(mySongs.get(position).getData());


//            mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
            audioServiceBinder.setAudioFileUri(u);
            audioServiceBinder.setContext(getApplicationContext());
            createAudioPlayProgressUpdater();
            audioServiceBinder.setAudioProgressUpdateHandler(audioProgressUpdateHandler);

            tvSongName.setText(mySongs.get(position).getSongName());
            Glide.with(this).load(getBitmap(this, Uri.parse(mySongs.get(position).getBitmapUri()))).into(ivAlbumArt);
            play.setBackgroundResource(R.drawable.ic_pause);
            //Get color based on bitmap
            Palette p = CommonUtils.createPaletteSync(getBitmap(this, Uri.parse(song.getBitmapUri())));
//        llMain.setBackgroundColor(p.getVibrantColor(getResources().getColor(R.color.white)));
//        llMain.setBackgroundColor(p.getLightVibrantColor(getResources().getColor(R.color.white)));
            llMain.setBackgroundColor(p.getDarkMutedColor(getResources().getColor(R.color.white)));
            if (isDark(p.getDarkMutedColor(getResources().getColor(R.color.white)))) {
                getResources().getDrawable(R.drawable.ic_next).setTint(Color.parseColor(whiteTint));
                getResources().getDrawable(R.drawable.ic_previous).setTint(Color.parseColor(whiteTint));
                getResources().getDrawable(R.drawable.ic_play).setTint(Color.parseColor(whiteTint));
                getResources().getDrawable(R.drawable.ic_pause).setTint(Color.parseColor(whiteTint));
                tvDurationElasp.setTextColor(Color.parseColor(whiteTint));
                tvDurationRemain.setTextColor(Color.parseColor(whiteTint));
                tvSongName.setTextColor(Color.parseColor("#FFF8C6"));
                tvArtistName.setTextColor(Color.parseColor("#95B9C7"));
                seekBar.getProgressDrawable().setTint(Color.parseColor(whiteTint));
                seekBar.getThumb().setTint(Color.parseColor("#00b0ff"));
            }
//            ivAlbumArt.setImageBitmap(mySongs.get(position).getBitmap());
            startPlaying();
            liveSong = mySongs.get(position);
//            currentSongDataListener.onSongChanged(song, true);

        } else if (view == previous) {
            stopPlaying();
            play.setBackgroundResource(R.drawable.ic_play);
            position = ((position - 1) < 0) ? (mySongs.size() - 1) : (position - 1);
            Uri u = Uri.parse(mySongs.get(position).getData());
//            mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
            setUriAndContext(u);
            tvSongName.setText(mySongs.get(position).getSongName());
            play.setBackgroundResource(R.drawable.ic_pause);
            Glide.with(this).load(getBitmap(this, Uri.parse(mySongs.get(position).getBitmapUri()))).into(ivAlbumArt);

            //Get color based on bitmap
            Palette p = CommonUtils.createPaletteSync(getBitmap(this, Uri.parse(song.getBitmapUri())));
//        llMain.setBackgroundColor(p.getVibrantColor(getResources().getColor(R.color.white)));
//        llMain.setBackgroundColor(p.getLightVibrantColor(getResources().getColor(R.color.white)));
            llMain.setBackgroundColor(p.getDarkMutedColor(getResources().getColor(R.color.white)));
            if (isDark(p.getDarkMutedColor(getResources().getColor(R.color.white)))) {
                getResources().getDrawable(R.drawable.ic_next).setTint(Color.parseColor(whiteTint));
                getResources().getDrawable(R.drawable.ic_previous).setTint(Color.parseColor(whiteTint));
                getResources().getDrawable(R.drawable.ic_play).setTint(Color.parseColor(whiteTint));
                getResources().getDrawable(R.drawable.ic_pause).setTint(Color.parseColor(whiteTint));
                tvDurationElasp.setTextColor(Color.parseColor(whiteTint));
                tvDurationRemain.setTextColor(Color.parseColor(whiteTint));
                tvSongName.setTextColor(Color.parseColor("#FFF8C6"));
                tvArtistName.setTextColor(Color.parseColor("#95B9C7"));
                seekBar.getProgressDrawable().setTint(Color.parseColor(whiteTint));
                seekBar.getThumb().setTint(Color.parseColor("#00b0ff"));
            }

//            ivAlbumArt.setImageBitmap(mySongs.get(position).getBitmap());
//            mediaPlayer.start();
            startPlaying();
            liveSong = mySongs.get(position);
//            currentSongDataListener.onSongChanged(song, true);

        }
    }

    /*public void playNext() {
        mediaPlayer.stop();
        mediaPlayer.release();
        play.setBackgroundResource(R.drawable.ic_play);
        position = ((position + 1) % mySongs.size());
        Uri u = Uri.parse(mySongs.get(position).getData());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
        tvSongName.setText(mySongs.get(position).getSongName());
        Glide.with(this).load(getBitmap(this, Uri.parse(mySongs.get(position).getBitmapUri()))).into(ivAlbumArt);
        play.setBackgroundResource(R.drawable.ic_pause);
        //Get color based on bitmap
        Palette p = CommonUtils.createPaletteSync(getBitmap(this, Uri.parse(song.getBitmapUri())));
//        llMain.setBackgroundColor(p.getVibrantColor(getResources().getColor(R.color.white)));
//        llMain.setBackgroundColor(p.getLightVibrantColor(getResources().getColor(R.color.white)));
        llMain.setBackgroundColor(p.getDarkMutedColor(getResources().getColor(R.color.white)));
        if (isDark(p.getDarkMutedColor(getResources().getColor(R.color.white)))) {
            getResources().getDrawable(R.drawable.ic_next).setTint(Color.parseColor(whiteTint));
            getResources().getDrawable(R.drawable.ic_previous).setTint(Color.parseColor(whiteTint));
            getResources().getDrawable(R.drawable.ic_play).setTint(Color.parseColor(whiteTint));
            getResources().getDrawable(R.drawable.ic_pause).setTint(Color.parseColor(whiteTint));
            tvDurationElasp.setTextColor(Color.parseColor(whiteTint));
            tvDurationRemain.setTextColor(Color.parseColor(whiteTint));
            tvSongName.setTextColor(Color.parseColor("#FFF8C6"));
            tvArtistName.setTextColor(Color.parseColor("#95B9C7"));
            seekBar.getProgressDrawable().setTint(Color.parseColor(whiteTint));
            seekBar.getThumb().setTint(Color.parseColor("#00b0ff"));
        }
//            ivAlbumArt.setImageBitmap(mySongs.get(position).getBitmap());
        mediaPlayer.start();
        liveSong = mySongs.get(position);
//            currentSongDataListener.onSongChanged(song, true);
    }*/

    /*public void playPrevious() {
        mediaPlayer.stop();
        mediaPlayer.release();
        play.setBackgroundResource(R.drawable.ic_play);
        position = ((position - 1) < 0) ? (mySongs.size() - 1) : (position - 1);
        Uri u = Uri.parse(mySongs.get(position).getData());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
        tvSongName.setText(mySongs.get(position).getSongName());
        play.setBackgroundResource(R.drawable.ic_pause);
        Glide.with(this).load(getBitmap(this, Uri.parse(mySongs.get(position).getBitmapUri()))).into(ivAlbumArt);

        //Get color based on bitmap
        Palette p = CommonUtils.createPaletteSync(getBitmap(this, Uri.parse(song.getBitmapUri())));
//        llMain.setBackgroundColor(p.getVibrantColor(getResources().getColor(R.color.white)));
//        llMain.setBackgroundColor(p.getLightVibrantColor(getResources().getColor(R.color.white)));
        llMain.setBackgroundColor(p.getDarkMutedColor(getResources().getColor(R.color.white)));
        if (isDark(p.getDarkMutedColor(getResources().getColor(R.color.white)))) {
            getResources().getDrawable(R.drawable.ic_next).setTint(Color.parseColor(whiteTint));
            getResources().getDrawable(R.drawable.ic_previous).setTint(Color.parseColor(whiteTint));
            getResources().getDrawable(R.drawable.ic_play).setTint(Color.parseColor(whiteTint));
            getResources().getDrawable(R.drawable.ic_pause).setTint(Color.parseColor(whiteTint));
            tvDurationElasp.setTextColor(Color.parseColor(whiteTint));
            tvDurationRemain.setTextColor(Color.parseColor(whiteTint));
            tvSongName.setTextColor(Color.parseColor("#FFF8C6"));
            tvArtistName.setTextColor(Color.parseColor("#95B9C7"));
            seekBar.getProgressDrawable().setTint(Color.parseColor(whiteTint));
            seekBar.getThumb().setTint(Color.parseColor("#00b0ff"));
        }

//            ivAlbumArt.setImageBitmap(mySongs.get(position).getBitmap());
        mediaPlayer.start();
        liveSong = mySongs.get(position);
//            currentSongDataListener.onSongChanged(song, true);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    Bitmap getBitmap(Context context, Uri albumArtUri) {
        Bitmap bitmap = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(
                    context.getContentResolver(), albumArtUri);
            bitmap = Bitmap.createScaledBitmap(bitmap, 512, 512, false);

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

    boolean isDark(int color) {
        return ColorUtils.calculateLuminance(color) < 0.5;
    }

    void setWhiteish(View view) {
        view.setBackgroundColor(Color.parseColor(whiteTint));
    }

    public static MutableLiveData<AudioListModel> getLiveSong() {
        MutableLiveData<AudioListModel> result = new MutableLiveData<>();
        result.setValue(liveSong);
        return result;
    }

    private void setUriAndContext(Uri u) {
        audioServiceBinder.setAudioFileUri(u);
        audioServiceBinder.setContext(getApplicationContext());
        createAudioPlayProgressUpdater();
        audioServiceBinder.setAudioProgressUpdateHandler(audioProgressUpdateHandler);
    }

    private void startPlaying() {
        audioServiceBinder.startAudio();
    }

    private void stopPlaying() {
        audioServiceBinder.stopAudio();
    }

    // Bind background service with caller activity. Then this activity can use
    // background service's AudioServiceBinder instance to invoke related methods.
    private void bindAudioService() {
        Intent intent = new Intent(PlayerActivityNew.this, AudioService.class);
        // Below code will invoke serviceConnection's onServiceConnected method.
        getApplicationContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        CommonUtils.showToast(this, "Binding Done");
    }

    // Unbound background audio service with caller activity.
    private void unBoundAudioService() {
        if (audioServiceBinder != null) {
            getApplicationContext().unbindService(serviceConnection);
        }
    }

    @Override
    protected void onDestroy() {
        // Unbound background audio service when activity is destroyed.
        unBoundAudioService();
        super.onDestroy();
    }

    // Create audio player progressbar updater.
    // This updater is used to update progressbar to reflect audio play process.
    @SuppressLint("HandlerLeak")
    private void createAudioPlayProgressUpdater() {
        /*Initialize audio progress handler */
        if (audioProgressUpdateHandler == null) {
            audioProgressUpdateHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // The update process message is sent from AudioServiceBinder class's thread object.
                    if (msg.what == audioServiceBinder.UPDATE_AUDIO_PROGRESS_BAR) {

                        if (audioServiceBinder != null) {
                            // Calculate the percentage.
                            int currProgress = audioServiceBinder.getAudioProgress();

                            // Update progressbar. Make the value 10 times to show more clear UI change.
//                            backgroundAudioProgress.setProgress(currProgress * 10);
                        }
                    }
                }
            };
        }
    }
}
