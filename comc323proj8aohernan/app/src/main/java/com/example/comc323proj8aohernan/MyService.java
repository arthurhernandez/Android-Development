package com.example.comc323proj8aohernan;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.Nullable;

import java.security.Provider;

public class MyService extends Service {
    MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mediaPlayer = MediaPlayer.create(this, R.raw.sunny);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        return START_STICKY;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
}
