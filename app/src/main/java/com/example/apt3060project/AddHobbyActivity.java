package com.example.apt3060project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddHobbyActivity extends AppCompatActivity {
    private HobbyDao hobbyDao;

    private EditText nameET;
    private EditText descriptionET;
    private TimePicker timeTP;
    private CheckBox monCB;
    private CheckBox tueCB;
    private CheckBox wedCB;
    private CheckBox thrCB;
    private CheckBox friCB;
    private CheckBox satCB;
    private CheckBox sunCB;
    private CheckBox shouldRepeatCB;
    private TimePicker durationTP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_hobby_layout);

        nameET = (EditText) findViewById(R.id.Add_Hobby_Name_EditText);
        descriptionET = (EditText) findViewById(R.id.Add_Hobby_Description_EditText);
        timeTP = (TimePicker) findViewById(R.id.Add_Hobby_Time);
        monCB = (CheckBox) findViewById(R.id.Add_Hobby_Days_Mon);
        tueCB = (CheckBox) findViewById(R.id.Add_Hobby_Days_Tue);
        wedCB = (CheckBox) findViewById(R.id.Add_Hobby_Days_Wed);
        thrCB = (CheckBox) findViewById(R.id.Add_Hobby_Days_Thr);
        friCB = (CheckBox) findViewById(R.id.Add_Hobby_Days_Fri);
        satCB = (CheckBox) findViewById(R.id.Add_Hobby_Days_Sat);
        sunCB = (CheckBox) findViewById(R.id.Add_Hobby_Days_Sun);
        shouldRepeatCB = (CheckBox) findViewById(R.id.Add_Hobby_Should_Repeat);
        durationTP = (TimePicker) findViewById(R.id.Add_Hobby_Duration_Time_Picker);

        Database db = Database.getInstance(this);
        hobbyDao = db.hobbyDao();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onStart() {
        super.onStart();
        durationTP.setIs24HourView(true);
        durationTP.setHour(1);
        durationTP.setMinute(0);
    }

    @SuppressLint("NewApi")
    public void onSaveClick(View view){
        final String name = nameET.getText().toString();
        final String description = descriptionET.getText().toString();
        final Calendar time = Calendar.getInstance(Locale.getDefault());
        time.set(0, 0, 0, timeTP.getHour(), timeTP.getMinute(), 0);
        final JSONObject days = new JSONObject();
        try {
            days.put("mon", monCB.isChecked());
            days.put("tue", tueCB.isChecked());
            days.put("wed", wedCB.isChecked());
            days.put("thr", thrCB.isChecked());
            days.put("fri", friCB.isChecked());
            days.put("sat", satCB.isChecked());
            days.put("sun", sunCB.isChecked());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final boolean shouldRepeat = shouldRepeatCB.isChecked();
        final Calendar duration = Calendar.getInstance(Locale.getDefault());
        duration.set(0, 0, 0, durationTP.getHour(), durationTP.getMinute(), 0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                DateFormat df = DateFormat.getTimeInstance(DateFormat.DEFAULT);
                Hobby hobby = new Hobby(1, name, description, df.format(time.getTime()), days.toString(), shouldRepeat, df.format(duration.getTime()));
                hobbyDao.insertHobby(hobby);
            }
        }).start();

        finish();
    }

    public void onBackButtonClick(View view){
        finish();
    }
}
