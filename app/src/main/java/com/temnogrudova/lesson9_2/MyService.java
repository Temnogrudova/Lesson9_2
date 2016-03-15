package com.temnogrudova.lesson9_2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MyService extends Service {
    private static final String ACTION_PLAY = "com.temnogrudova.lesson9_2.PLAY" ;
    private static final String ACTION_STOP = "com.temnogrudova.lesson9_2.STOP" ;
    MediaPlayer mPlayer;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Служба создана",
                Toast.LENGTH_SHORT).show();

        NotificationManager nm = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = null;
        RemoteViews mRemoteViews = null;
        if (mRemoteViews == null) {
            mRemoteViews = new RemoteViews(getPackageName(),
                    R.layout.notificion_layout);
        }

        intent = new Intent(ACTION_PLAY);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),
                1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //In R.layout.notification_control_bar,there is a button view identified by bar_btn_stop
        //We bind a pendingIntent with this button view so when user click the button,it will excute the intent action.
        mRemoteViews.setOnClickPendingIntent(R.id.btnPlay,
                pendingIntent);


        intent = new Intent(ACTION_STOP);
        PendingIntent pendingIntent1 = PendingIntent.getService(getApplicationContext(),
                1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btnStop,
                pendingIntent1);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentInfo("qqq")
                .setContentTitle("Mario Song")
                .setTicker("Mario Song")//status bar
                .setContentText("qwerty")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(Notification.PRIORITY_MIN)
                .setOngoing(true)
                .setContent(mRemoteViews)
                .build();
        nm.notify(1, notification);
        play();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            if (intent != null) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)) {
                if (action.equals(ACTION_PLAY)) {
                    if (!mPlayer.isPlaying())
                        play();
                }
                if (action.equals(ACTION_STOP)) {
                    mPlayer.stop();
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void play() {
        mPlayer = MediaPlayer.create(this, R.raw.mario);
        mPlayer.setLooping(false);
        mPlayer.start();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Служба остановлена",
                Toast.LENGTH_SHORT).show();
        if (mPlayer.isPlaying()){
            mPlayer.stop();
        }
        super.onDestroy();

    }
}