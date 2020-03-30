package com.example.apt3060project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.apt3060project.adapters.HobbyArrayAdapter;
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
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private HobbyDao hobbyDao;
    private HobbyHistoryDao hobbyHistoryDao;
    private List<Hobby> hobbyList;

    private TextView dateTV;
    private TextView timeTV;
    private Thread dateTimeTVUpdateThread;
    private boolean isActivityRunning = false;

    private ListView hobbyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        isActivityRunning = true;

        dateTV = (TextView) findViewById(R.id.DateLayoutDateTV);
        timeTV = (TextView) findViewById(R.id.DateLayoutTimeTV);

        hobbyListView = (ListView) findViewById(R.id.main_activity_hobbies_list);
        registerForContextMenu(hobbyListView);

        Database db = Database.getInstance(this);
        hobbyDao = db.hobbyDao();
        hobbyHistoryDao = db.hobbyHistoryDao();

        dateTimeTVUpdateThread = new Thread(() -> {
            while(isActivityRunning) {
                final Date date = new Date();
                runOnUiThread(() -> {
                    dateTV.setText(DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault()).format(date));
                    timeTV.setText(DateFormat.getTimeInstance(DateFormat.DEFAULT, Locale.getDefault()).format(date));
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!dateTimeTVUpdateThread.isAlive())
            dateTimeTVUpdateThread.start();

        hobbyListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, DetailedViewActivity.class);
            intent.putExtra("_id", hobbyList.get(position).getId());

            startActivity(intent);
        });

        loadPage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActivityRunning = false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_hobby_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.main_activity_hobby_context_menu_add:
                addDateStampButton(hobbyList.get(info.position));
                return true;
            case R.id.main_activity_hobby_context_menu_edit:
                editButton(hobbyList.get(info.position).getId());
                return true;
            case R.id.main_activity_hobby_context_menu_report:
                reportButton(hobbyList.get(info.position).getId());
                return true;
            case R.id.main_activity_hobby_context_menu_delete:
                deleteButton(hobbyList.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void loadPage() {
        new Thread(() -> {
            hobbyList = hobbyDao.getHobbies();
            handler.post(() -> hobbyListView.setAdapter(new HobbyArrayAdapter(this, hobbyList)));
        }).start();
    }

    public void onAddButtonClick(View view){
        Intent intent = new Intent(this, AddHobbyActivity.class);
        startActivity(intent);
    }

    public void showPopup(View view) {
        int position = (int) view.getTag();
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.main_activity_hobby_context_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener((item) -> {
            switch (item.getItemId()){
                case R.id.main_activity_hobby_context_menu_add:
                    addDateStampButton(hobbyList.get(position));
                    return true;
                case R.id.main_activity_hobby_context_menu_edit:
                    editButton(hobbyList.get(position).getId());
                    return true;
                case R.id.main_activity_hobby_context_menu_report:
                    reportButton(hobbyList.get(position).getId());
                    return true;
                case R.id.main_activity_hobby_context_menu_delete:
                    deleteButton(hobbyList.get(position));
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        });
        popupMenu.show();
    }

    public void editButton(int id){
        Intent intent = new Intent(this, EditHobbyActivity.class);
        intent.putExtra("_id", id);
        startActivity(intent);
    }

    public void reportButton(int id){
        Intent intent = new Intent(this, ReportActivity.class);
        intent.putExtra("_id", id);
        startActivity(intent);
    }

    public void deleteButton(Hobby hobby) {
        AlertDialog a = DialogManager.yesNoDialog(this, R.string.delete, R.string.delete_hobby_message, R.string.yes, R.string.no, null, null);
        a.show();
        a.findViewById(R.id.yes_no_dialog_box_yes).setOnClickListener(View -> {
            new Thread(() -> {
                hobbyDao.deleteHobby(hobby);
                this.loadPage();
            }).start();
            a.dismiss();
        });
    }

    public void addDateStampButton(Hobby hobby){

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

            a.dismiss();
        });
    }
}
