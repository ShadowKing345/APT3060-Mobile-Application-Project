package com.example.apt3060project.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.apt3060project.database.Hobby;

import java.util.List;

@Dao
public interface HobbyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHobby(Hobby... hobbies);

    @Update()
    void updateHobby(Hobby... hobbies);

    @Delete()
    void deleteHobby(Hobby... hobbies);

    @Query("Select * from Hobbies")
    List<Hobby> getHobbies();

    @Query("Select * from hobbies where _id = :id")
    Hobby getHobbyById(int id);
}

