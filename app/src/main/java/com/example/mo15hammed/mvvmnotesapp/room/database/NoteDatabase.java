package com.example.mo15hammed.mvvmnotesapp.room.database;

import android.content.Context;
import android.os.AsyncTask;

import com.example.mo15hammed.mvvmnotesapp.room.dao.NoteDao;
import com.example.mo15hammed.mvvmnotesapp.room.entities.Note;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context.getApplicationContext(), NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    public abstract NoteDao noteDao();

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            new PopulateDbAsyncTask(instance.noteDao()).execute();

        }
    };

    public static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        PopulateDbAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            noteDao.insert(new Note("Add", "You can tab the add button\nat the bottom-right corner\nto add a new note.", 1));
            noteDao.insert(new Note("Edit", "You can click any note to edit it", 2));
            noteDao.insert(new Note("Delete", "You can swipe right or left\nto delete any note.", 3));
            noteDao.insert(new Note("Delete All", "You can delete all notes\nfrom the top-right menu button", 4));

            return null;
        }
    }

}
