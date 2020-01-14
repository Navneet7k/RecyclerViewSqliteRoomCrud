package com.example.recyclerviewsqliteroomcrud.persistance;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(NotesModel word);

    @Query("DELETE FROM notes_model")
    void deleteAll();

    @Query("DELETE FROM notes_model WHERE notesId = :id")
    void delete(int id);

    @Query("SELECT * from notes_model ORDER BY notesId DESC")
    LiveData<List<NotesModel>> getAlphabetizedWords();
}
