package com.example.apt3060project;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Hobby.class}, exportSchema = false, version = 1)
public abstract class Database extends RoomDatabase {
    private static final String DB_NAME = "apt3060_project_hobbies_db";
    public static Database instance;

    public static synchronized Database getInstance(Context context){
        if(instance ==null)
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, DB_NAME).build();

        return instance;
    }

    public abstract HobbyDao hobbyDao();
}
