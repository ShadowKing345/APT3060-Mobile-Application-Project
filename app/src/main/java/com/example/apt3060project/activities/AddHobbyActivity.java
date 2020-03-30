package com.example.apt3060project.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.apt3060project.database.Database;
import com.example.apt3060project.database.Hobby;
import com.example.apt3060project.database.dao.HobbyDao;
import com.example.apt3060project.utils.DialogManager;
import com.example.apt3060project.R;
import com.example.apt3060project.utils.Utils;

import java.text.DateFormat;
import java.util.Date;

public class AddHobbyActivity extends AppCompatActivity {
    private HobbyDao hobbyDao;
    private EditText nameET;
    private EditText descriptionET;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_hobby);

        Database db = Database.getInstance(this);
        hobbyDao = db.hobbyDao();

        nameET = (EditText) findViewById(R.id.edit_hobby_name_ET);
        descriptionET = (EditText) findViewById(R.id.edit_hobby_description_ET);
        findViewById(R.id.edit_hobby_save_B).setOnClickListener(this::onSaveButtonClicked);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void onSaveButtonClicked(View view){
        final String name = nameET.getText().toString();
        if (!Utils.validateString(name)) {
            DialogManager.okayDialog(this, R.string.information, R.string.verification_name, R.string.okay, null).show();
            return;
        }

        final String description = descriptionET.getText().toString();
        if (!Utils.validateString(description)) {
            DialogManager.okayDialog(this, R.string.information, R.string.verification_description, R.string.okay, null).show();
            return;
        }

        new Thread(() -> hobbyDao.insertHobby(new Hobby(name, description, DateFormat.getDateInstance(DateFormat.DEFAULT).format(new Date())))).start();

        finish();

    }
}
