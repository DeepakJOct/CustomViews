package app.com.customviews.services;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.IOException;

public class AudioServiceBinder extends Binder {
    private Uri audioFileUri;
    private MediaPlayer audioplayer;
    private Context context;
    private Handler audioProgressUpdateHandler;
    public final int UPDATE_AUDIO_PROGRESS_BAR = 1;

    public Uri getAudioFileUri() {
        return audioFileUri;
    }

    public void setAudioFileUri(Uri audioFileUri) {
        this.audioFileUri = audioFileUri;
    }

    public MediaPlayer getAudioplayer() {
        return audioplayer;
    }

    public void setAudioplayer(MediaPlayer audioplayer) {
        this.audioplayer = audioplayer;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Handler getAudioProgressUpdateHandler() {
        return audioProgressUpdateHandler;
    }

    public void setAudioProgressUpdateHandler(Handler audioProgressUpdateHandler) {
        this.audioProgressUpdateHandler = audioProgressUpdateHandler;
    }

    public void startAudio() {
        initAudioPlayer();
        if (audioplayer != null) {
            audioplayer.start();
        }
    }

    public void pauseAudio() {
        if (audioplayer != null) {
            audioplayer.pause();
        }
    }

    public void stopAudio() {
        if (audioplayer != null) {
            audioplayer.stop();
            destroyAudioPlayer();
        }
    }

    public void seekAudio(int seekProgress) {
        if (audioplayer != null) {
            audioplayer.seekTo(seekProgress);
        }
    }

    public boolean isPlaying() {
        if(audioplayer != null && audioplayer.isPlaying()) {
            return true;
        }
        return false;
    }

    private void initAudioPlayer() {
        try {
            if (audioplayer == null) {
                audioplayer = new MediaPlayer();
                audioplayer.setDataSource(getContext(), getAudioFileUri());

                audioplayer.prepare();
                Thread updateAudioProgressThread = new Thread() {
                    @Override
                    public void run() {
                        while (true) {
                            // Create update audio progress message.
                            Message updateAudioProgressMsg = new Message();
                            updateAudioProgressMsg.what = UPDATE_AUDIO_PROGRESS_BAR;

                            // Send the message to caller activity's update audio prgressbar Handler object.
                            audioProgressUpdateHandler.sendMessage(updateAudioProgressMsg);

                            // Sleep one second.
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                };
                updateAudioProgressThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Destroy audio player.
    private void destroyAudioPlayer() {
        if (audioplayer != null) {
            if (audioplayer.isPlaying()) {
                audioplayer.stop();
            }

            audioplayer.release();

            audioplayer = null;
        }
    }

    // Return current audio play position
    public int getCurrentAudioPosition() {
        int ret = 0;
        if(audioplayer != null) {
            ret = audioplayer.getCurrentPosition();
        }
        return ret;
    }

    // Return total audio file duration.
    public int getTotalAudioDuration()
    {
        int ret = 0;
        if(audioplayer != null)
        {
            ret = audioplayer.getDuration();
        }
        return ret;
    }

    // Return current audio player progress value.
    public int getAudioProgress()
    {
        int ret = 0;
        int currAudioPosition = getCurrentAudioPosition();
        int totalAudioDuration = getTotalAudioDuration();
        if(totalAudioDuration > 0) {
            ret = (currAudioPosition * 100) / totalAudioDuration;
        }
        return ret;
    }
}
