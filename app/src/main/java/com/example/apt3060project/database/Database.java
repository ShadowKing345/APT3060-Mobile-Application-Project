package com.example.apt3060project.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.apt3060project.database.dao.HobbyDao;
import com.example.apt3060project.database.dao.HobbyHistoryDao;

@androidx.room.Database(entities = {Hobby.class, HobbyHistory.class}, exportSchema = false, version = 1)
public abstract class Database extends RoomDatabase {
    private static final String DB_NAME = "apt3060_project_hobbies_db";
    private static Database instance;

    public static synchronized Database getInstance(Context context){
        if(instance ==null)
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, DB_NAME).build();

        return instance;
    }

    public abstract HobbyDao hobbyDao();
    public abstract HobbyHistoryDao hobbyHistoryDao();
}
