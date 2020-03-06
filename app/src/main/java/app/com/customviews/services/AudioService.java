package app.com.customviews.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class AudioService extends Service {

    private final IBinder audioServiceBinder = new AudioServiceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return audioServiceBinder;
    }


}
