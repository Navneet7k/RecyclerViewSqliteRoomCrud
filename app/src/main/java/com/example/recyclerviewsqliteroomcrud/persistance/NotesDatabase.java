package com.example.recyclerviewsqliteroomcrud.persistance;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {NotesModel.class}, version = 1, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {

    public abstract NotesDao notesDao();
    private static volatile NotesDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static NotesDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NotesDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NotesDatabase.class, "notes_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            databaseWriteExecutor.execute(() -> {

                NotesDao dao = INSTANCE.notesDao();
                dao.deleteAll();
                dao.insert(new NotesModel("This is a sample article title","This is a sample article description This is a sample article description This is a sample article description This is a sample article description"));
                dao.insert(new NotesModel("This is a sample article title","This is a sample article description This is a sample article description This is a sample article description This is a sample article description"));
                dao.insert(new NotesModel("This is a sample article title","This is a sample article description This is a sample article description This is a sample article description This is a sample article description"));
                dao.insert(new NotesModel("This is a sample article title","This is a sample article description This is a sample article description This is a sample article description This is a sample article description"));
                dao.insert(new NotesModel("This is a sample article title","This is a sample article description This is a sample article description This is a sample article description This is a sample article description"));
            });
        }
    };
}
