package com.example.recyclerviewsqliteroomcrud.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerviewsqliteroomcrud.R;
import com.example.recyclerviewsqliteroomcrud.databinding.NotesItemViewBinding;
import com.example.recyclerviewsqliteroomcrud.persistance.NotesModel;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private ArrayList<NotesModel> notesModels;
    private Context context;
    MainActivity.NotesInteractionListener notesInteractionListener;

    public NotesAdapter(Context context, ArrayList<NotesModel> articleModels, MainActivity.NotesInteractionListener notesInteractionListener) {
        this.notesModels=articleModels;
        this.context=context;
        this.notesInteractionListener=notesInteractionListener;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item_view,parent,false);
        return new NotesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {
        final NotesModel notesModel=notesModels.get(position);
        holder.bind(notesModel);
    }

    @Override
    public int getItemCount() {
        return notesModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private NotesItemViewBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }

        public void bind(NotesModel notesModel) {
            binding.setItemModel(notesModel);

            binding.deleteBtn.setOnClickListener(v -> notesInteractionListener.onNoteDeleted(notesModel.getNotesId()));
        }
    }
}
