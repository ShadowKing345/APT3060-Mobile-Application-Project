package com.example.apt3060project.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.apt3060project.database.HobbyHistory;

import java.util.List;

@Dao
public interface HobbyHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHobbyHistoryEntry(HobbyHistory... hobbyHistoryEntries);
    @Update()
    void updateHobbyHistoryEntry(HobbyHistory... hobbyHistoryEntries);
    @Delete()
    void deleteHobbyHistoryEntry(HobbyHistory... hobbyHistoryEntries);

    @Query("Select * from HobbyHistories where reference = :reference")
    List<HobbyHistory> getHobbyHistoryEntriesByReference(String reference);
}
