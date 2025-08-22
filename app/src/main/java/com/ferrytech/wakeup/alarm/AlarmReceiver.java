package com.ferrytech.wakeup.alarm;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("alarm_id", -1);
        Intent svc = new Intent(context, MusicService.class);
        svc.putExtra("alarm_id", id);
        // Start service to play sound (foreground)
        context.startForegroundService(svc);
    }
}
