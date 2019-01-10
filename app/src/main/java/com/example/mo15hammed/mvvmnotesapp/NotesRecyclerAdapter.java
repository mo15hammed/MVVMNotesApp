package com.example.mo15hammed.mvvmnotesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mo15hammed.mvvmnotesapp.room.entities.Note;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class NotesRecyclerAdapter extends ListAdapter<Note, NotesRecyclerAdapter.NotesViewHolder> {

    private OnNoteClickListener mNoteClickListener;

    public NotesRecyclerAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getUid() == newItem.getUid();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item_layout, parent, false);
        return new NotesViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Note note = getItem(position);

        holder.mTitle.setText(note.getTitle());
        holder.mDescription.setText(note.getDescription());
        holder.mPriority.setText(String.valueOf(note.getPriority()));

    }

    public Note getNoteAt(int position) {
        return getItem(position);
    }

    class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitle, mDescription, mPriority;

        NotesViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.txt_title);
            mDescription = itemView.findViewById(R.id.txt_description);
            mPriority = itemView.findViewById(R.id.txt_priority);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mNoteClickListener.onNoteClick(getItem(getAdapterPosition()));
        }
    }


    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    public void setOnNoteClickListener(OnNoteClickListener listener) {
        this.mNoteClickListener = listener;
    }

}
