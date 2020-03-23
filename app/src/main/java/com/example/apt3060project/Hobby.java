package com.example.apt3060project;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONObject;

@Entity(tableName = "Hobbies")
public class Hobby {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "details")
    private String details;
    @ColumnInfo(name = "time")
    private String time;
    @ColumnInfo(name = "dates")
    private String dates;
    @ColumnInfo(name = "shouldRepeat")
    private boolean shouldRepeat;
    @ColumnInfo(name = "duration")
    private String duration;
    @ColumnInfo(name = "timeSpent")
    private int timeSpent;
    @ColumnInfo(name = "historyReference")
    private String historyReference;

    public Hobby(int id, String name, String details, String time, String dates, boolean shouldRepeat, String duration) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.time = time;
        this.dates = dates;
        this.shouldRepeat = shouldRepeat;
        this.duration = duration;
        this.timeSpent = 0;
        this.historyReference = name+"_history";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public boolean isShouldRepeat() {
        return shouldRepeat;
    }

    public void setShouldRepeat(boolean shouldRepeat) {
        this.shouldRepeat = shouldRepeat;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getHistoryReference() {
        return historyReference;
    }

    public void setHistoryReference(String historyReference) {
        this.historyReference = historyReference;
    }
}
