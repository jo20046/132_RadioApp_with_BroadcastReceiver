package de.jo20046.a132_radioapp_with_broadcastreceiver;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class RadioService extends Service {

    private final IBinder myBinder = new MyBinder();
    private MediaPlayer mp;
    private String stream;
    private boolean mpPaused = false;
    private boolean mpStopped = false;
    String tag = "mytag";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(tag, "onStartCommand() - Service");
        stream = intent.getExtras().getString("Stream");
        startMediaPlayer();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d(tag, "onCreate() - Service");
        super.onCreate();

//        Intent notificationIntent = new Intent(this, MainActivity.class);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//                notificationIntent, 0);
//
//        Notification notification = new NotificationCompat.Builder(this)
////                .setSmallIcon(R.mipmap.app_icon)
//                .setContentTitle("My Awesome App")
//                .setContentText("Doing some work...")
//                .setContentIntent(pendingIntent).build();
//
//        startForeground(1337, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(tag, "onBind() - Service");
        return myBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseMediaplayer();
    }

    public void startMediaPlayer() {
//        releaseMediaplayer();
        mp = new MediaPlayer();
        try {
            mp.setDataSource(stream);

            // Version "blocking"
//            mp.prepare();
//            mp.start();

            // Version non-blocking
            mp.setOnPreparedListener(MediaPlayer::start);
            mp.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopMediaPlayer() {
        mp.stop();
        mpStopped = true;
    }

    public void pauseMediaPlayer() {
        mp.pause();
        mpPaused = true;
    }

    public void restartMediaPlayer() {
        if (!mp.isPlaying()) {
            if (mpPaused) {
                Log.d(tag, "Restart Mediaplayer from Paused state");
                mp.start();
                mpPaused = false;
            } else if (mpStopped) {
                Log.d(tag, "Restart Mediaplayer from Stopped state");
                mp.prepareAsync();
                mpStopped = false;
            }
        }
    }

    public void releaseMediaplayer() {
        if (mp != null) mp.release();
    }

    public class MyBinder extends Binder {
        RadioService getService() {
            Log.d(tag, "getService() - Binder");
            return RadioService.this;
        }
    }
}

