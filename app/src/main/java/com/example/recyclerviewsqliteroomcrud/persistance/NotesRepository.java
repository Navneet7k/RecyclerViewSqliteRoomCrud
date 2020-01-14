package com.example.recyclerviewsqliteroomcrud.persistance;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

public class NotesRepository {
    private NotesDao mNotesDao;
    private LiveData<List<NotesModel>> mAllNotes;

    @Inject
    public NotesRepository(Context application) {
        NotesDatabase db = NotesDatabase.getDatabase(application);
        mNotesDao = db.notesDao();
        mAllNotes = mNotesDao.getAlphabetizedWords();
    }

    public LiveData<List<NotesModel>> getAllNotes() {
        return mAllNotes;
    }

    public void insert(NotesModel notesModel) {
        NotesDatabase.databaseWriteExecutor.execute(() -> {
            mNotesDao.insert(notesModel);
        });
    }

    public void deleteNote(int noteId) {
        NotesDatabase.databaseWriteExecutor.execute(() -> {
            mNotesDao.delete(noteId);
        });
    }
}
