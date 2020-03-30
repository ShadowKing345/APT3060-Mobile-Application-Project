package com.example.apt3060project.activities;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuCompat;

import com.example.apt3060project.adapters.HobbyHistoryArrayAdapter;
import com.example.apt3060project.database.Database;
import com.example.apt3060project.database.Hobby;
import com.example.apt3060project.database.dao.HobbyDao;
import com.example.apt3060project.database.HobbyHistory;
import com.example.apt3060project.database.dao.HobbyHistoryDao;
import com.example.apt3060project.utils.DialogManager;
import com.example.apt3060project.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private HobbyDao hobbyDao;
    private HobbyHistoryDao hobbyHistoryDao;
    private Hobby hobby;
    private List<HobbyHistory> hobbyHistories;
    private int id;

    private TextView nameTV;
    private TextView accumulatedTimeTV;
    private ListView hobbyHistoriesList;
    private GraphView graphView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);

        id = getIntent().getIntExtra("_id", 0);
        if (id == 0) {
            DialogManager.okayDialog(this, R.string.error, R.string.null_point_error, R.string.okay, View -> finish()).show();
            return;
        }

        Database db = Database.getInstance(this);
        hobbyDao = db.hobbyDao();
        hobbyHistoryDao = db.hobbyHistoryDao();

        nameTV = (TextView) findViewById(R.id.report_name_TE);
        accumulatedTimeTV = (TextView) findViewById(R.id.report_total_time_TE);
        hobbyHistoriesList = (ListView) findViewById(R.id.report_hobby_history_list);
        graphView = (GraphView) findViewById(R.id.report_graphview);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(this::loadPage).start();
        hobbyHistoriesList.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> onHobbyHistoryClick(position));

        graphView.getGridLabelRenderer().setHorizontalAxisTitle("Day");
        graphView.getGridLabelRenderer().setLabelHorizontalHeight(30);
        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graphView.getGridLabelRenderer().setTextSize(30);
        graphView.getGridLabelRenderer().setNumHorizontalLabels(3);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            graphView.getGridLabelRenderer().setGridColor(getColor(R.color.white));
            graphView.getGridLabelRenderer().setVerticalLabelsColor(getColor(R.color.white));
            graphView.getGridLabelRenderer().setHorizontalAxisTitleColor(getColor(R.color.white));
            graphView.getGridLabelRenderer().setHorizontalLabelsColor(getColor(R.color.white));
        }

        graphView.getViewport().setScrollable(true);
        graphView.getViewport().setYAxisBoundsManual(true);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        graphView.getViewport().setMinX(calendar.getTime().getTime());
        calendar.add(Calendar.DATE, 2);
        graphView.getViewport().setMaxX(calendar.getTime().getTime());
        graphView.getViewport().setXAxisBoundsManual(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.report_menu, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
    }

    private void loadPage() {
        if (id == 0) {
            DialogManager.okayDialog(this, R.string.error, R.string.null_point_error, R.string.okay, View -> finish()).show();
            return;
        }


        new Thread(() -> {
            hobby = hobbyDao.getHobbyById(id);
            hobbyHistories = hobbyHistoryDao.getHobbyHistoryEntriesByReference(hobby.getName());

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(getDates());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                series.setColor(getColor(R.color.shade_5));

            handler.post(() ->
            {
                nameTV.setText(hobby.getName());
                accumulatedTimeTV.setText(String.valueOf(hobby.getTotalTime()));

                hobbyHistoriesList.setAdapter(new HobbyHistoryArrayAdapter(this, hobbyHistories));

                graphView.getViewport().setMaxY(hobby.getTotalTime());
                graphView.removeAllSeries();
                graphView.addSeries(series);
            });
        });
    }

    private DataPoint[] getDates() {
        List<DataPoint> points = new ArrayList<>();
        try {
            points.add(new DataPoint(DateFormat.getDateInstance(DateFormat.DEFAULT).parse(hobby.getDateCreated()).getTime(), 0));
            for (HobbyHistory history : hobbyHistories) {
                System.out.println(DateFormat.getDateInstance(DateFormat.DEFAULT).parse(history.getDateStamp()).getHours());
                points.add(new DataPoint(DateFormat.getDateInstance(DateFormat.DEFAULT).parse(history.getDateStamp()).getTime(), history.getDuration()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Collections.sort(points, (p1, p2) -> (int) (p1.getX() - p2.getX()));
        DataPoint[] result = new DataPoint[points.size()];
        points.toArray(result);
        return result;
    }

    private void onHobbyHistoryClick(int position) {
        HobbyHistory hobbyHistory = hobbyHistories.get(position);

        Date dateStamp = null;
        Date minDateStamp = null;
        try {
            dateStamp = DateFormat.getDateInstance(DateFormat.DEFAULT).parse(hobbyHistory.getDateStamp());
            minDateStamp = DateFormat.getDateInstance(DateFormat.DEFAULT).parse(hobby.getDateCreated());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        AlertDialog a = DialogManager.hobbyHistoryDialog(this, R.string.add_hobby_history, dateStamp.getTime(), minDateStamp.getTime(), null, hobbyHistory.getDuration(), R.string.save, R.string.cancel, null, null);
        a.show();

        ((Button) a.findViewById(R.id.add_hobby_history_save)).setOnClickListener(View -> {
            AlertDialog proceedDialog = DialogManager.yesNoDialog(this, R.string.save, R.string.edit_hobby_save_changes, R.string.yes, R.string.no, null, null);
            proceedDialog.show();
            proceedDialog.findViewById(R.id.yes_no_dialog_box_yes).setOnClickListener(VIEW -> {

                proceedDialog.dismiss();

                if (((NumberPicker) a.findViewById(R.id.add_hobby_history_hour_NP)).getValue() == 0 &&
                        ((NumberPicker) a.findViewById(R.id.add_hobby_history_minute_NP)).getValue() == 0) {
                    AlertDialog deletionConfirmationDialog = DialogManager.yesNoDialog(this, R.string.delete, R.string.delete_hobby_history_message, R.string.yes, R.string.no, null, null);
                    deletionConfirmationDialog.show();
                    deletionConfirmationDialog.findViewById(R.id.yes_no_dialog_box_yes).setOnClickListener(V -> {
                        new Thread(() -> hobbyHistoryDao.deleteHobbyHistoryEntry(hobbyHistory)).start();
                        deletionConfirmationDialog.dismiss();
                        a.cancel();
                        new Thread(this::loadPage).start();
                    });
                    return;
                }

                Date day = new Date(((CalendarView) a.findViewById(R.id.add_hobby_history_calender)).getDate());
                int duration = ((NumberPicker) a.findViewById(R.id.add_hobby_history_hour_NP)).getValue() * 60 + ((NumberPicker) a.findViewById(R.id.add_hobby_history_minute_NP)).getValue();

                hobby.setTotalTime(hobby.getTotalTime() - hobbyHistory.getDuration());
                hobbyHistory.setDateStamp(DateFormat.getDateInstance(DateFormat.DEFAULT).format(day));
                hobbyHistory.setDuration(duration);
                hobby.setTotalTime(hobby.getTotalTime() + duration);

                new Thread(() -> {
                    hobbyDao.updateHobby(hobby);
                    hobbyHistoryDao.updateHobbyHistoryEntry(hobbyHistory);
                }).start();

                new Thread(this::loadPage).start();

                a.dismiss();
            });
        });
    }

    public void onAddMenuItemClick(MenuItem item) {
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
}
