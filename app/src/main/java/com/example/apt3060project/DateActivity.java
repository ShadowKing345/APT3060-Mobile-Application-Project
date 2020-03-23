package com.example.apt3060project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateActivity extends AppCompatActivity {

    private TextView dateTV;
    private TextView timeTV;
    private Thread dateTimeTVUpdateThread;
    private boolean isActivityRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date);
        isActivityRunning = true;

        dateTV = (TextView) findViewById(R.id.DateLayoutDateTV);
        timeTV = (TextView) findViewById(R.id.DateLayoutTimeTV);

        dateTimeTVUpdateThread = new Thread(new Runnable(){
            @Override
            public void run() {
                while(isActivityRunning) {
                    final Date date = new Date();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dateTV.setText(DateFormat.getDateInstance(DateFormat.DATE_FIELD, Locale.getDefault()).format(date));
                            timeTV.setText(DateFormat.getTimeInstance(DateFormat.DEFAULT, Locale.getDefault()).format(date));
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        dateTimeTVUpdateThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActivityRunning = false;
    }
}
