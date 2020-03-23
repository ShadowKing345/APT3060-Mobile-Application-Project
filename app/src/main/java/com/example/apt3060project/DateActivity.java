package com.example.apt3060project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateActivity extends AppCompatActivity {

    private HobbyDao dao;

    private TextView dateTV;
    private TextView timeTV;
    private Thread dateTimeTVUpdateThread;
    private boolean isActivityRunning = false;

    private ListView hobbyListView;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date);
        isActivityRunning = true;

        dateTV = (TextView) findViewById(R.id.DateLayoutDateTV);
        timeTV = (TextView) findViewById(R.id.DateLayoutTimeTV);

        hobbyListView = (ListView) findViewById(R.id.DateHobbiesList);

        Database db = Database.getInstance(this);
        dao = db.hobbyDao();

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

        if(!dateTimeTVUpdateThread.isAlive())
            dateTimeTVUpdateThread.start();
        loadHobbies(new Date());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActivityRunning = false;
    }

    private void loadHobbies(final Date date){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(dao.getHobbies().size());
                String dayOfWeek = new SimpleDateFormat("E").format(date);
                final Cursor cursor = dao.getHobbiesCursor();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                                DateActivity.this,
                                R.layout.day_hobby_list_item,
                                cursor,
                                new String[]{"name", "time", "duration"},
                                new int[]{R.id.dayHobbyLIName, R.id.dayHobbyLITime, R.id.dayHobbyLIDuration}
                        );
                        hobbyListView.setAdapter(adapter);
                    }
                });

//                for(Hobby h: dao.getHobbies()){
//                    dao.deleteHobby(h);
//                }
            }
        }).start();
    }

    public void onAddButtonClick(View view){
        Intent intent = new Intent(this, AddHobbyActivity.class);
        startActivity(intent);
    }

}
