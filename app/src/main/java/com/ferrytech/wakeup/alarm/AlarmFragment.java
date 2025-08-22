package com.ferrytech.wakeup.alarm;



import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.*;
import android.widget.*;

import com.ferrytech.wakeup.R;

import java.util.*;

public class AlarmFragment extends Fragment {
    private TimePicker timePicker;
    private Button btnAdd;
    private ListView listView;

    private final List<Alarm> alarms = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alarm, container, false);
    }

    @Override public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        timePicker = v.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(false);
        btnAdd = v.findViewById(R.id.btnAddAlarm);
      //  listView = v.findViewById(R.id.listAlarms);
        listView = v.findViewById(R.id.listAlarms);
// Set to 12-hour view (false = 12h, true = 24h)
        timePicker.setIs24HourView(false);

        alarms.clear();
        alarms.addAll(AlarmStore.load(requireContext()));

        adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, toStrings());
        listView.setAdapter(adapter);

        btnAdd.setOnClickListener(view -> {
            int hour, minute;
            if (Build.VERSION.SDK_INT >= 23) {
                hour = timePicker.getHour();
                minute = timePicker.getMinute();
            } else {
                hour = timePicker.getCurrentHour();
                minute = timePicker.getCurrentMinute();
            }
            // Convert 24h from TimePicker to 12h display + AM/PM
            boolean isPm = hour >= 12;
            int hour12 = hour % 12;
            if (hour12 == 0) hour12 = 12;

            int id = generateId();
            Alarm a = new Alarm(id, hour12, minute, isPm);
            alarms.add(a);
            AlarmStore.save(requireContext(), alarms);
            AlarmScheduler.schedule(requireContext(), a);
            refreshList();
            Toast.makeText(getContext(), "Alarm set for " + a.display(), Toast.LENGTH_SHORT).show();
        });

        // Long-press to delete an alarm
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Alarm a = alarms.get(position);
            AlarmScheduler.cancel(requireContext(), a.id);
            alarms.remove(position);
            AlarmStore.save(requireContext(), alarms);
            refreshList();
            Toast.makeText(getContext(), "Alarm deleted", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private List<String> toStrings() {
        List<String> s = new ArrayList<>();
        for (Alarm a : alarms) s.add(a.display());
        return s;
    }

    private void refreshList() {
        adapter.clear();
        adapter.addAll(toStrings());
        adapter.notifyDataSetChanged();
    }

    private int generateId() {
        // simple unique ID
        return (int) (System.currentTimeMillis() & 0x7FFFFFFF);
    }
}

