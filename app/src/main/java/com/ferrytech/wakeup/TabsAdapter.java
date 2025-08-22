package com.ferrytech.wakeup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ferrytech.wakeup.alarm.AlarmFragment;
import com.ferrytech.wakeup.stopwatch.StopwatchFragment;


public class TabsAdapter extends FragmentStateAdapter {
    public TabsAdapter(@NonNull FragmentActivity fa) { super(fa); }

    @NonNull @Override
    public Fragment createFragment(int position) {
        if (position == 0) return new AlarmFragment();
        return new StopwatchFragment();
    }

    @Override public int getItemCount() { return 2; }
}
