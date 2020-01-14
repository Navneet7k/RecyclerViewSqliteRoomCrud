package com.example.recyclerviewsqliteroomcrud.view;

import com.example.recyclerviewsqliteroomcrud.BaseViewModel;
import com.example.recyclerviewsqliteroomcrud.persistance.NotesRepository;

import javax.inject.Inject;

public class MainActivityVM extends BaseViewModel {
    final NotesRepository mNotesRepository;

    @Inject
    public MainActivityVM(NotesRepository mNotesRepository) {
        super(mNotesRepository);
        this.mNotesRepository=mNotesRepository;
    }
}
