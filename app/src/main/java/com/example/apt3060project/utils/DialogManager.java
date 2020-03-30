package com.example.apt3060project.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.apt3060project.R;

import java.util.Calendar;
import java.util.Date;

public class DialogManager {
    public static @NonNull AlertDialog yesNoDialog(Activity a, @Nullable Integer title, @Nullable Integer message, @Nullable Integer yesText, @Nullable Integer noText, @Nullable View.OnClickListener yesOnClickListener, @Nullable View.OnClickListener noOnClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(a);
        View view = LayoutInflater.from(a).inflate(R.layout.yes_no_dailog_box, (ConstraintLayout) a.findViewById(R.id.yes_no_dialog_box_layout));

        TextView titleTV = (TextView) view.findViewById(R.id.yes_no_dialog_box_title);
        TextView messageTV = (TextView) view.findViewById(R.id.yes_no_dialog_box_message);
        Button yes = (Button) view.findViewById(R.id.yes_no_dialog_box_yes);
        Button no = (Button) view.findViewById(R.id.yes_no_dialog_box_no);

        if(title != null)
            titleTV.setText(title);
        if(message != null)
            messageTV.setText(message);
        if(yesText != null)
            yes.setText(yesText);
        if(noText != null)
            no.setText(noText);

        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        yes.setOnClickListener(yesOnClickListener == null ? View -> alertDialog.dismiss() : yesOnClickListener);
        no.setOnClickListener(noOnClickListener == null ? View -> alertDialog.cancel() : noOnClickListener);

        return alertDialog;
    }

    public static @NonNull AlertDialog okayDialog(Activity a, @Nullable Integer title, @Nullable Integer message, @Nullable Integer okayText, @Nullable View.OnClickListener okayOnClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(a);
        View view = LayoutInflater.from(a).inflate(R.layout.okay_dialog_box, (ConstraintLayout) a.findViewById(R.id.okay_dialog_box_layout));

        TextView titleTV = (TextView) view.findViewById(R.id.okay_dialog_box_title);
        TextView messageTV = (TextView) view.findViewById(R.id.okay_dialog_box_message);
        Button okay = (Button) view.findViewById(R.id.okay_dialog_box_okay);

        if(title != null)
            titleTV.setText(title);
        if(message != null)
            messageTV.setText(message);
        if(okayText != null)
            okay.setText(okayText);

        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        okay.setOnClickListener(okayOnClickListener == null ? View -> alertDialog.dismiss() : okayOnClickListener);

        return alertDialog;
    }

    public static @NonNull AlertDialog hobbyHistoryDialog(Activity a, @Nullable Integer title, @Nullable Long date, @Nullable Long minDate, @Nullable Long maxDate, @Nullable Integer duration, @Nullable Integer saveText, @Nullable Integer cancelText, @Nullable View.OnClickListener saveOnClickListener, @Nullable View.OnClickListener cancelOnClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(a);
        View view = LayoutInflater.from(a).inflate(R.layout.add_hobby_history_dialog, (ConstraintLayout) a.findViewById(R.id.add_hobby_history_dialog_layout));

        TextView titleTV = (TextView) view.findViewById(R.id.add_hobby_history_title);
        CalendarView calendarView = (CalendarView) view.findViewById(R.id.add_hobby_history_calender);
        NumberPicker hour = (NumberPicker) view.findViewById(R.id.add_hobby_history_hour_NP);
        NumberPicker minute = (NumberPicker) view.findViewById(R.id.add_hobby_history_minute_NP);
        Button save = (Button) view.findViewById(R.id.add_hobby_history_save);
        Button cancel = (Button) view.findViewById(R.id.add_hobby_history_cancel);

        if(title != null)
            titleTV.setText(title);

        calendarView.setDate(date == null ? new Date().getTime() : date);
        calendarView.setOnDateChangeListener((View, year, month, day) -> {
            Calendar hold = Calendar.getInstance();
            hold.set(year, month, day);
            ((CalendarView) view.findViewById(R.id.add_hobby_history_calender)).setDate(hold.getTimeInMillis());
        });
        calendarView.setMaxDate(maxDate == null ? new Date().getTime() : maxDate);
        calendarView.setMinDate(minDate == null ? 0 : minDate);

        hour.setMinValue(0);
        hour.setMaxValue(24);
        hour.setValue(duration == null ? 0 : duration / 60);
        minute.setMinValue(0);
        minute.setMaxValue(59);
        minute.setValue(duration == null ? 30: duration % 60);

        if(saveText != null)
            save.setText(saveText);
        if(cancelText != null)
            cancel.setText(cancelText);

        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        save.setOnClickListener(saveOnClickListener == null ? View -> alertDialog.dismiss() : saveOnClickListener);
        cancel.setOnClickListener(saveOnClickListener == null ? View -> alertDialog.cancel() : cancelOnClickListener);

        return alertDialog;
    }
}
