package com.example.recyclerviewsqliteroomcrud.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.recyclerviewsqliteroomcrud.BR;
import com.example.recyclerviewsqliteroomcrud.BaseActivity;
import com.example.recyclerviewsqliteroomcrud.R;
import com.example.recyclerviewsqliteroomcrud.databinding.ActivityMainBinding;
import com.example.recyclerviewsqliteroomcrud.persistance.NotesModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import javax.inject.Inject;

public class MainActivity extends BaseActivity<ActivityMainBinding,MainActivityVM> {

    private ActivityMainBinding binding;
    private NotesAdapter notesAdapter;
    private AlertDialog alertDialog;

    @Inject
    MainActivityVM mainActivityVM;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainActivityVM getViewModel() {
        return mainActivityVM;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=getViewDataBinding();

        binding.noteItemsList.setLayoutManager(new LinearLayoutManager(this));

        mainActivityVM.mNotesRepository.getAllNotes().observe(this,articleModels -> {
            if (articleModels!=null) {
                notesAdapter=new NotesAdapter(this, new ArrayList<>(articleModels), note_id -> mainActivityVM.mNotesRepository.deleteNote(note_id));
                binding.noteItemsList.setAdapter(notesAdapter);
            }
        });

        binding.fab.setOnClickListener(v -> addNewItem());
    }

    public interface NotesInteractionListener {
        public void onNoteDeleted(int note_id);
    }

    private void addNewItem() {
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_with_btns_view, null);
        dialogBuilder.setView(dialogView);
        AppCompatTextView dialogHeader=dialogView.findViewById(R.id.dialogHeader);
        Button ok_button=dialogView.findViewById(R.id.ok_button);
        Button cancel_button=dialogView.findViewById(R.id.cancel_button);
        EditText desc_ed=dialogView.findViewById(R.id.desc_ed);
        EditText title_ed=dialogView.findViewById(R.id.title_ed);
        alertDialog = dialogBuilder.create();
        if (alertDialog.getWindow()!=null) {
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        }

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(title_ed.getText()) && !TextUtils.isEmpty(desc_ed.getText())) {
                    NotesModel notesModel=new NotesModel(title_ed.getText().toString(),desc_ed.getText().toString());
                    mainActivityVM.mNotesRepository.insert(notesModel);
                    alertDialog.dismiss();
                }else {
                    if (TextUtils.isEmpty(title_ed.getText()))
                        title_ed.setError("Field can't be empty");
                    if (TextUtils.isEmpty(desc_ed.getText()))
                        desc_ed.setError("Field can't be empty");
                }
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow()!=null) {
//            WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();
//            wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
//            wmlp.gravity = Gravity.TOP;
//            wmlp.y = 150;
        }
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }
}
