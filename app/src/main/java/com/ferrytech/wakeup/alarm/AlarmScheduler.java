package com.ferrytech.wakeup.alarm;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;



import java.util.Calendar;

public class AlarmScheduler {

    @SuppressLint("ScheduleExactAlarm")
    public static void schedule(Context ctx, Alarm a) {
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = pending(ctx, a.id);

        Calendar cal = Calendar.getInstance();
        // Convert 12h + AM/PM to 24h
        int h24 = (a.hour12 % 12) + (a.isPm ? 12 : 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, h24);
        cal.set(Calendar.MINUTE, a.minute);

        // If time already passed today, schedule for tomorrow
        if (cal.getTimeInMillis() <= System.currentTimeMillis()) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Use AlarmClockInfo to ensure exact firing without special permission
        Intent showIntent = new Intent(ctx, com.ferrytech.wakeup.MainActivity.class);//////j j
        PendingIntent showPi = PendingIntent.getActivity(
                ctx, a.id, showIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager.AlarmClockInfo info =
                new AlarmManager.AlarmClockInfo(cal.getTimeInMillis(), showPi);

        if (am != null) {
            am.setAlarmClock(info, pi);
        }
    }

    public static void cancel(Context ctx, int id) {
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        if (am != null) am.cancel(pending(ctx, id));
    }

    private static PendingIntent pending(Context ctx, int id) {
        Intent i = new Intent(ctx, AlarmReceiver.class);
        i.putExtra("alarm_id", id);
        return PendingIntent.getBroadcast(
                ctx, id, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    @SuppressLint("ScheduleExactAlarm")
    public static void snoozeInMinutes(Context ctx, int id, int minutes) {
        long triggerAt = System.currentTimeMillis() + minutes * 60_000L;
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(ctx, AlarmReceiver.class);
        i.putExtra("alarm_id", id);
        PendingIntent pi = PendingIntent.getBroadcast(
                ctx, id, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (Build.VERSION.SDK_INT >= 23) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pi);
        } else {
            am.setExact(AlarmManager.RTC_WAKEUP, triggerAt, pi);
        }
    }
}
