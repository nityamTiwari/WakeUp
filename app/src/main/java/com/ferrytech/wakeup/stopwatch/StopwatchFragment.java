package com.ferrytech.wakeup.stopwatch;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ferrytech.wakeup.R;

import android.os.Handler;  // ✅ Correct import

public class StopwatchFragment extends Fragment {
    private TextView stopwatchText;
    private Button startButton, stopButton, resetButton;

    private long startTime = 0L;
    private long timeBuff = 0L;
    private long updateTime = 0L;
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            long millisecondTime = SystemClock.elapsedRealtime() - startTime;
            updateTime = timeBuff + millisecondTime;

            int seconds = (int) (updateTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (updateTime % 1000);

            // Show Minutes:Seconds:Milliseconds
            stopwatchText.setText(
                    String.format("%02d:%02d:%03d", minutes, seconds, milliseconds)
            );

            // ✅ Correct way to update every 10ms
            handler.postDelayed(this, 10);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        stopwatchText = view.findViewById(R.id.stopwatchText);
        startButton = view.findViewById(R.id.startButton);
        stopButton = view.findViewById(R.id.stopButton);
        resetButton = view.findViewById(R.id.resetButton);

        startButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.GONE);
        resetButton.setVisibility(View. VISIBLE);

        startButton.setOnClickListener(v -> {
            startTime = SystemClock.elapsedRealtime();
            handler.postDelayed(runnable, 0);

            startButton.setVisibility(View.GONE);
            stopButton.setVisibility(View.VISIBLE);
            resetButton.setVisibility(View. VISIBLE);

        });

        stopButton.setOnClickListener(v -> {
            timeBuff += SystemClock.elapsedRealtime() - startTime;
            handler.removeCallbacks(runnable);

            startButton.setVisibility(View.GONE);
            stopButton.setVisibility(View.GONE);
            resetButton.setVisibility(View. VISIBLE);
        });

        resetButton.setOnClickListener(v -> {
            startTime = 0L;
            timeBuff = 0L;
            updateTime = 0L;
            stopwatchText.setText("00:00:000");
            handler.removeCallbacks(runnable);
            startButton.setVisibility(View.VISIBLE);
            stopButton.setVisibility(View.GONE);
            resetButton.setVisibility(View. VISIBLE);
        });

        if(startButton == null){

        }

        return view;
    }
}
