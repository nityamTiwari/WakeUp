package com.ferrytech.wakeup.alarm;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SnoozeReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("alarm_id", -1);
        context.stopService(new Intent(context, MusicService.class));
        // Snooze for 5 minutes
        AlarmScheduler.snoozeInMinutes(context, id, 5);
    }
}

