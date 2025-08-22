package com.ferrytech.wakeup.alarm;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DismissReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        context.stopService(new Intent(context, MusicService.class));
    }
}

