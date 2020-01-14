package com.example.recyclerviewsqliteroomcrud.persistance;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes_model")
public class NotesModel {
    @PrimaryKey(autoGenerate = true)
    private int notesId;
    @ColumnInfo(name = "notesTitle")
    private String notesTitle;
    @ColumnInfo(name = "notesDesc")
    private String notesDesc;

    public NotesModel(String notesTitle, String notesDesc) {
        this.notesTitle = notesTitle;
        this.notesDesc = notesDesc;
    }

    public String getNotesTitle() {
        return notesTitle;
    }

    public void setNotesTitle(String notesTitle) {
        this.notesTitle = notesTitle;
    }

    public String getNotesDesc() {
        return notesDesc;
    }

    public void setNotesDesc(String notesDesc) {
        this.notesDesc = notesDesc;
    }

    public int getNotesId() {
        return notesId;
    }

    public void setNotesId(int notesId) {
        this.notesId = notesId;
    }
}
