package com.ferrytech.wakeup.alarm;

public class Alarm {
    public int id;        // unique requestCode
    public int hour12;    // 1..12
    public int minute;    // 0..59
    public boolean isPm;  // true if PM

    public Alarm(int id, int hour12, int minute, boolean isPm) {
        this.id = id;
        this.hour12 = hour12;
        this.minute = minute;
        this.isPm = isPm;
    }

    public String display() {
        String mm = (minute < 10 ? "0" : "") + minute;
        return hour12 + ":" + mm + (isPm ? " PM" : " AM");
    }
}

