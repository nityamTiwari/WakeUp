package com.ferrytech.wakeup.alarm;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AlarmStore {
    private static final String PREF = "alarms_pref";
    private static final String KEY = "alarms";

    public static List<Alarm> load(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String raw = sp.getString(KEY, "[]");
        List<Alarm> list = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(raw);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                list.add(new Alarm(
                        o.getInt("id"),
                        o.getInt("hour12"),
                        o.getInt("minute"),
                        o.getBoolean("isPm")
                ));
            }
        } catch (Exception ignored) {}
        return list;
    }

    public static void save(Context ctx, List<Alarm> alarms) {
        JSONArray arr = new JSONArray();
        try {
            for (Alarm a : alarms) {
                JSONObject o = new JSONObject();
                o.put("id", a.id);
                o.put("hour12", a.hour12);
                o.put("minute", a.minute);
                o.put("isPm", a.isPm);
                arr.put(o);
            }
        } catch (Exception ignored) {}
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit()
                .putString(KEY, arr.toString())
                .apply();
    }
}

