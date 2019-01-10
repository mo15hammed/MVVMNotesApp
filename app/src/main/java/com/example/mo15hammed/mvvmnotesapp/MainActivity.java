package com.example.mo15hammed.mvvmnotesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mo15hammed.mvvmnotesapp.room.entities.Note;
import com.example.mo15hammed.mvvmnotesapp.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements NotesRecyclerAdapter.OnNoteClickListener {

    private static final String TAG = "MainActivity";
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    private NoteViewModel noteViewModel;

    private View mParentLayout;
    private RecyclerView mNotesRecycler;
    private NotesRecyclerAdapter notesAdapter;
    private FloatingActionButton mAddNote;

    private Note deletedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mParentLayout = findViewById(android.R.id.content);
        mAddNote = findViewById(R.id.btn_add_note);

        mNotesRecycler = findViewById(R.id.notes_recycler);
        mNotesRecycler.setLayoutManager(new LinearLayoutManager(this));
        mNotesRecycler.setHasFixedSize(true);
        notesAdapter = new NotesRecyclerAdapter();
        notesAdapter.setOnNoteClickListener(this);
        mNotesRecycler.setAdapter(notesAdapter);


        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, notes -> {
            notesAdapter.submitList(notes);
        });

        mAddNote.setOnClickListener(view -> {
            Intent addNoteIntent = new Intent(MainActivity.this, AddEditNoteActivity.class);
            startActivityForResult(addNoteIntent, ADD_NOTE_REQUEST);
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                Note note = notesAdapter.getNoteAt(viewHolder.getAdapterPosition());
                noteViewModel.delete(note);

                deletedNote = note;

                Snackbar mySnackbar = Snackbar.make(mParentLayout, "Item Removed !", Snackbar.LENGTH_LONG);
                mySnackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noteViewModel.insert(deletedNote);
                    }
                });
                mySnackbar.show();
            }
        }).attachToRecyclerView(mNotesRecycler);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, desc, priority);
            noteViewModel.insert(note);

            Toast.makeText(this, "Note Saved !", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {

            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Note Cannot Be Updated !", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, desc, priority);
            note.setUid(id);
            noteViewModel.update(note);
            Toast.makeText(this, "Note Updated !", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Changes Discarded !", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_delete_all:

                noteViewModel.deleteAll();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onNoteClick(Note note) {
        Intent editIntent = new Intent(MainActivity.this, AddEditNoteActivity.class);
        editIntent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getUid());
        editIntent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
        editIntent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
        editIntent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority());
        startActivityForResult(editIntent, EDIT_NOTE_REQUEST);
    }
}

