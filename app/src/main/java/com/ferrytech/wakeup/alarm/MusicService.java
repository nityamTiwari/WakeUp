package com.ferrytech.wakeup.alarm;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.ferrytech.wakeup.MainActivity;
import com.ferrytech.wakeup.R;


public class MusicService extends Service {
    private static final String CH_ID = "alarms_channel";
    private MediaPlayer mp;
    private int alarmId;

    @Override public void onCreate() { super.onCreate(); createChannel(); }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        alarmId = intent.getIntExtra("alarm_id", -1);

        // Foreground notification with Dismiss + Snooze actions
        Intent open = new Intent(this, MainActivity.class);
        PendingIntent content = PendingIntent.getActivity(this, alarmId, open,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent dismissI = new Intent(this, DismissReceiver.class).putExtra("alarm_id", alarmId);
        PendingIntent dismiss = PendingIntent.getBroadcast(this, alarmId, dismissI,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent snoozeI = new Intent(this, SnoozeReceiver.class).putExtra("alarm_id", alarmId);
        PendingIntent snooze = PendingIntent.getBroadcast(this, alarmId, snoozeI,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification n = new NotificationCompat.Builder(this, CH_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Alarm")
                .setContentText("Alarm is ringing")
                .setContentIntent(content)
                .setOngoing(true)
                .addAction(0, "Dismiss", dismiss)
                .addAction(0, "Snooze 5 min", snooze)
                .build();

        startForeground(1000 + alarmId, n);

        try {
            Uri tone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (tone == null) tone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            mp = MediaPlayer.create(this, tone);
            mp.setLooping(true);
            mp.start();
        } catch (Exception ignored) {}

        return START_NOT_STICKY;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        if (mp != null) { mp.stop(); mp.release(); mp = null; }
    }

    @Nullable @Override public IBinder onBind(Intent intent) { return null; }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel ch = new NotificationChannel(CH_ID, "Alarms",
                    NotificationManager.IMPORTANCE_HIGH);
            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(ch);
        }
    }
}

