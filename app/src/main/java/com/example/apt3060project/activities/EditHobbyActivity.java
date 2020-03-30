package com.example.apt3060project.activities;

import android.os.Bundle;
import android.os.Handler;
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

public class EditHobbyActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private HobbyDao hobbyDao;
    private int id;
    private Hobby hobby;

    private EditText nameET;
    private EditText descriptionET;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_hobby);

        id = getIntent().getIntExtra("_id", 0);
        if (id == 0) {
            DialogManager.okayDialog(this, R.string.error, R.string.null_point_error, R.string.okay, View -> finish()).show();
            return;
        }


        Database db = Database.getInstance(this);
        hobbyDao = db.hobbyDao();

        nameET = (EditText) findViewById(R.id.edit_hobby_name_ET);
        descriptionET = (EditText) findViewById(R.id.edit_hobby_description_ET);
        findViewById(R.id.edit_hobby_save_B).setOnClickListener(this::onSaveButtonClicked);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (id == 0) {
            DialogManager.okayDialog(this, R.string.error, R.string.null_point_error, R.string.okay, View -> finish()).show();
            return;
        }

        new Thread(() -> {
            if (id == 0) return;
            hobby = hobbyDao.getHobbyById(id);
            handler.post(() -> {
                nameET.setText(hobby.getName());
                descriptionET.setText(hobby.getDescription());
            });
        }).start();
    }

    private void onSaveButtonClicked(View view){
        final String name = nameET.getText().toString();
        if (!Utils.validateString(name)) {
            DialogManager.okayDialog(this, R.string.information, R.string.verification_name, R.string.okay, null).show();
            return;
        }
        hobby.setName(name);

        final String description = descriptionET.getText().toString();
        if (!Utils.validateString(description)) {
            DialogManager.okayDialog(this, R.string.information, R.string.verification_description, R.string.okay, null).show();
            return;
        }
        hobby.setDescription(description);

        new Thread(() -> hobbyDao.updateHobby(hobby)).start();

        finish();
    }
}
