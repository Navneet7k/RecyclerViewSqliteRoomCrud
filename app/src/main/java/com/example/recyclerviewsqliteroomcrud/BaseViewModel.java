
package com.example.recyclerviewsqliteroomcrud;

import androidx.lifecycle.ViewModel;

import com.example.recyclerviewsqliteroomcrud.persistance.NotesRepository;

public abstract class BaseViewModel extends ViewModel {

    private final NotesRepository mNotesRepository;


    public BaseViewModel(NotesRepository mNotesRepository) {
        this.mNotesRepository = mNotesRepository;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public NotesRepository getNotesRepository() {
        return mNotesRepository;
    }
}
