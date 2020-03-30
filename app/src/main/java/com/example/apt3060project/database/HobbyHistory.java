package com.example.apt3060project.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "HobbyHistories", foreignKeys = @ForeignKey(entity = Hobby.class, parentColumns = "name", childColumns = "reference", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE))
public class HobbyHistory {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int id;
    @ColumnInfo(name = "reference")
    private String reference;
    @ColumnInfo(name = "dateStamp")
    private String dateStamp;
    @ColumnInfo(name = "duration")
    private int duration;

    public HobbyHistory(String reference, String dateStamp, int duration) {
        this.reference = reference;
        this.dateStamp = dateStamp;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(String dateStamp) {
        this.dateStamp = dateStamp;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
