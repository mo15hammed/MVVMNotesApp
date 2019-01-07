package com.example.mo15hammed.mvvmnotesapp.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.mo15hammed.mvvmnotesapp.room.dao.NoteDao;
import com.example.mo15hammed.mvvmnotesapp.room.database.NoteDatabase;
import com.example.mo15hammed.mvvmnotesapp.room.entities.Note;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;
    private Executor executor;

    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.selectAll();
        executor = Executors.newSingleThreadExecutor();
    }

    public void insert(Note note) {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void update(Note note) {

        executor.execute(() -> noteDao.update(note));

    }

    public void delete(Note note) {
        executor.execute(() -> noteDao.delete(note));
    }

    public void deleteAll() {
        executor.execute(() -> noteDao.deleteAll());
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }
}
