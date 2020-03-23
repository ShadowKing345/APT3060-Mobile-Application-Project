package com.example.apt3060project;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public abstract class HobbyDao{
    @Insert
    public abstract void insertHobby(Hobby hobby);
    @Update()
    public abstract void updateHobby(Hobby hobby);
    @Delete
    public abstract void deleteHobby(Hobby hobby);

    @Query("Select * from hobbies")
    public abstract List<Hobby> getHobbies();
    @Query("Select * from hobbies where hobbies._id = :id")
    public abstract List<Hobby> getHobbiesById(int id);
    @Query("Select * from hobbies where hobbies.dates = :date")
    public abstract List<Hobby> getHobbiesByDate(String date);
    @Query("select * from hobbies where hobbies.dates = :date")
    public abstract Cursor getHobbiesByDateCursor(String date);
    @Query("select * from hobbies")
    public abstract Cursor getHobbiesCursor();
}