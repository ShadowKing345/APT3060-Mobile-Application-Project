package com.example.apt3060project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuCompat;

import com.example.apt3060project.database.Database;
import com.example.apt3060project.database.Hobby;
import com.example.apt3060project.database.dao.HobbyDao;
import com.example.apt3060project.database.HobbyHistory;
import com.example.apt3060project.database.dao.HobbyHistoryDao;
import com.example.apt3060project.utils.DialogManager;
import com.example.apt3060project.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class DetailedViewActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private HobbyDao hobbyDao;
    private HobbyHistoryDao hobbyHistoryDao;
    private Hobby hobby;
    private int id;

    private TextView name;
    private TextView description;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_view);

        id = getIntent().getIntExtra("_id", 0);
        if (id == 0) {
            DialogManager.okayDialog(this, R.string.error, R.string.null_point_error, R.string.okay, View -> finish()).show();
            return;
        }


        Database db = Database.getInstance(this);
        hobbyDao = db.hobbyDao();
        hobbyHistoryDao = db.hobbyHistoryDao();

        name = (TextView) findViewById(R.id.detailed_hobby_name_TV);
        description = (TextView) findViewById(R.id.detailed_hobby_description_TV);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Thread(this::loadPage).start();
    }

    public void loadPage() {
        if (id == 0)
            DialogManager.okayDialog(this, R.string.error, R.string.null_point_error, R.string.okay, View -> finish()).show();   if (id == 0) {
            DialogManager.okayDialog(this, R.string.error, R.string.null_point_error, R.string.okay, View -> finish()).show();
            return;
        }


        new Thread(() -> {
            hobby = hobbyDao.getHobbyById(id);
            handler.post(() ->

            {
                name.setText(hobby.getName());
                description.setText(hobby.getDescription());
            });
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailed_view_menu, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
    }

    public void onEditButtonClicked(View view){
        Intent intent = new Intent(this, EditHobbyActivity.class);
        intent.putExtra("_id", id);
        startActivity(intent);
    }

    public void onReportButtonClicked(View view){
        Intent intent = new Intent(this, ReportActivity.class);
        intent.putExtra("_id", id);
        startActivity(intent);
    }

    public void onDeleteButtonClicked(View view) {
        DialogManager.yesNoDialog(this, R.string.delete, R.string.delete_hobby_message, R.string.yes, R.string.no, View -> {
            new Thread(() -> hobbyDao.deleteHobby(hobby)).start();
            finish();
        }, null).show();
    }

    public void onAddDateStampButtonClicked(View view){

        Date minDateStamp = null;
        try {
            minDateStamp = DateFormat.getDateInstance(DateFormat.DEFAULT).parse(hobby.getDateCreated());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        AlertDialog a = DialogManager.hobbyHistoryDialog(this, R.string.add_hobby_history, null, minDateStamp.getTime(), null,null, R.string.add, R.string.cancel, null, null);
        a.show();

        ((Button) a.findViewById(R.id.add_hobby_history_save)).setOnClickListener(View -> {
            if(((NumberPicker) a.findViewById(R.id.add_hobby_history_hour_NP)).getValue() == 0 &&
                    ((NumberPicker) a.findViewById(R.id.add_hobby_history_minute_NP)).getValue() == 0)
                        return;

            Date day = new Date(((CalendarView) a.findViewById(R.id.add_hobby_history_calender)).getDate());
            int duration = ((NumberPicker) a.findViewById(R.id.add_hobby_history_hour_NP)).getValue() * 60 + ((NumberPicker) a.findViewById(R.id.add_hobby_history_minute_NP)).getValue();

            hobby.setTotalTime(hobby.getTotalTime() + duration);

            new Thread(() -> {
                hobbyDao.updateHobby(hobby);
                hobbyHistoryDao.insertHobbyHistoryEntry(new HobbyHistory(hobby.getName(), DateFormat.getDateInstance(DateFormat.DEFAULT).format(day), duration));
            }).start();

            this.loadPage();

            a.dismiss();
        });
    }

    public void onAddDateStampButtonClicked(MenuItem item) {
        onAddDateStampButtonClicked(item.getActionView());
    }

    public void onEditButtonClicked(MenuItem item) {
        onEditButtonClicked(item.getActionView());
    }

    public void onReportButtonClicked(MenuItem item) {
        onReportButtonClicked(item.getActionView());
    }

    public void onDeleteButtonClicked(MenuItem item) {
        onDeleteButtonClicked(item.getActionView());
    }
}
